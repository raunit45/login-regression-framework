package tests;

import base.BaseTest;
import base.DriverManager;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SignInPage;
import utils.*;

import java.nio.file.Path;
import java.util.List;

public class LoginRegressionTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        List<TestData> list = CsvReaderUtil.readLoginData(config.getLoginDataCsv());
        Object[][] data = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) data[i][0] = list.get(i);
        return data;
    }

    @Test(dataProvider = "loginData")
    public void loginFormRegression(TestData td) {
        var driver = DriverManager.getDriver();
        var wait = new WaitUtil(driver, config.getExplicitWaitSeconds());

        LogUtil.info("START testId=" + td.testId + " name=" + td.testName + " flow=" + td.flow);

        if (config.useHomepageSignIn()) {
            HomePage home = new HomePage(driver, wait);
            home.open(config.getBaseUrl());
            home.dismissOverlaysBestEffort(config.attemptDismissOverlays());
            home.clickSignInEntry();
        } else {
            driver.get(config.getBaseUrl());
        }

        SignInPage signIn = new SignInPage(driver, wait);

        if (signIn.isCaptchaPresent()) skip(td, "CAPTCHA encountered on entry");
        if (signIn.isTwoFaPresent()) skip(td, "2FA encountered on entry");

        if (!signIn.isEmailStepReady()) {
            String directSignInUrl = joinUrl(config.getBaseUrl(), config.getSignInPath());
            LogUtil.warn("Email step not ready after initial navigation. Retrying direct sign-in URL: " + directSignInUrl);
            signIn.openDirectSignIn(directSignInUrl);
            signIn.dismissOverlaysBestEffort(config.attemptDismissOverlays());

            if (signIn.isCaptchaPresent()) skip(td, "CAPTCHA encountered (email step not ready)");
            if (signIn.isTwoFaPresent()) skip(td, "2FA encountered (email step not ready)");

            if (!signIn.isEmailStepReady()) {
                fail(td, "Email step not ready (navigation/overlay)", "", "UNKNOWN");
                return;
            }
        }

        signIn.enterEmailOrPhone(td.emailOrPhone);
        signIn.clickContinue();

        if (signIn.isCaptchaPresent()) skip(td, "CAPTCHA encountered after Continue");
        if (signIn.isTwoFaPresent()) skip(td, "2FA encountered after Continue");

        if ("EMAIL_ONLY".equalsIgnoreCase(td.flow)) {
            String msg = signIn.getErrorMessageOrEmpty();
            String outcome = signIn.isPasswordStepReady() ? "UNKNOWN" : "FAIL";
            writeAssert(td, msg, outcome, "EMAIL_ONLY");
            return;
        }

        if (!signIn.isPasswordStepReady()) {
            String msg = signIn.getErrorMessageOrEmpty();
            writeAssert(td, msg, "FAIL", "NO_PASSWORD_STEP");
            return;
        }

        signIn.enterPassword(td.password);
        signIn.clickSignIn();

        if (signIn.isCaptchaPresent()) skip(td, "CAPTCHA encountered after Sign-In");
        if (signIn.isTwoFaPresent()) skip(td, "2FA encountered after Sign-In");

        String msg = signIn.getErrorMessageOrEmpty();
        String outcome = "FAIL";
        if (msg.isBlank() && !signIn.isPasswordStepReady() && !signIn.isEmailStepReady()) {
            outcome = "UNKNOWN";
        }
        writeAssert(td, msg, outcome, "EMAIL_PASS");
    }

    private void writeAssert(TestData td, String actualMsg, String actualOutcome, String stage) {
        boolean outcomeOk = equalsIgnoreCase(actualOutcome, td.expectedOutcome);

        boolean msgOk;
        if ("ANY".equalsIgnoreCase(td.expectedMessageMode)) {
            msgOk = true;
        } else {
            msgOk = containsIgnoreCase(actualMsg, td.expectedMessage);
        }

        String status = (outcomeOk && msgOk) ? "PASS" : "FAIL";
        String reason = status.equals("FAIL") ? ("Mismatch at " + stage) : "";

        Path shot = null;
        if ("FAIL".equals(status)) {
            shot = ScreenshotUtil.takeScreenshot(DriverManager.getDriver(), RunContext.getScreenshotsDir(), td.testId + "_" + stage + "_FAIL");
        }

        CsvResultWriter.appendRow(
                config.getResultsCsv(),
                RunContext.getRunId(),
                td.testId,
                td.testName,
                td.emailOrPhone,
                td.expectedOutcome,
                actualOutcome,
                td.expectedMessageMode,
                td.expectedMessage,
                actualMsg,
                status,
                reason,
                shot == null ? "" : shot.toString(),
                RunContext.getLogPath()
        );

        LogUtil.info("END testId=" + td.testId + " status=" + status + " stage=" + stage);

        Assert.assertEquals(status, "PASS", "Case failed: " + td.testId + " (" + stage + ")");
    }

    private void skip(TestData td, String reason) {
        Path shot = ScreenshotUtil.takeScreenshot(DriverManager.getDriver(), RunContext.getScreenshotsDir(), td.testId + "_SKIP");
        CsvResultWriter.appendRow(
                config.getResultsCsv(),
                RunContext.getRunId(),
                td.testId,
                td.testName,
                td.emailOrPhone,
                td.expectedOutcome,
                "SKIP",
                td.expectedMessageMode,
                td.expectedMessage,
                "",
                "SKIP",
                reason,
                shot == null ? "" : shot.toString(),
                RunContext.getLogPath()
        );
        LogUtil.warn("SKIP testId=" + td.testId + " reason=" + reason);
        throw new SkipException(reason);
    }

    private void fail(TestData td, String reason, String actualMsg, String actualOutcome) {
        Path shot = ScreenshotUtil.takeScreenshot(DriverManager.getDriver(), RunContext.getScreenshotsDir(), td.testId + "_FAIL");
        CsvResultWriter.appendRow(
                config.getResultsCsv(),
                RunContext.getRunId(),
                td.testId,
                td.testName,
                td.emailOrPhone,
                td.expectedOutcome,
                actualOutcome,
                td.expectedMessageMode,
                td.expectedMessage,
                actualMsg,
                "FAIL",
                reason,
                shot == null ? "" : shot.toString(),
                RunContext.getLogPath()
        );
        LogUtil.warn("FAIL testId=" + td.testId + " reason=" + reason);
        Assert.fail(reason);
    }

    private boolean containsIgnoreCase(String haystack, String needle) {
        if (needle == null || needle.isBlank()) return true;
        if (haystack == null) return false;
        return haystack.toLowerCase().contains(needle.toLowerCase());
    }

    private boolean equalsIgnoreCase(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.trim().equalsIgnoreCase(b.trim());
    }

    private String joinUrl(String baseUrl, String path) {
        if (baseUrl == null) return path == null ? "" : path;
        if (path == null || path.isBlank()) return baseUrl;

        boolean baseEndsWithSlash = baseUrl.endsWith("/");
        boolean pathStartsWithSlash = path.startsWith("/");

        if (baseEndsWithSlash && pathStartsWithSlash) return baseUrl + path.substring(1);
        if (!baseEndsWithSlash && !pathStartsWithSlash) return baseUrl + "/" + path;
        return baseUrl + path;
    }
}
