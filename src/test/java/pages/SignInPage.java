package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.OverlayUtil;
import utils.LogUtil;
import utils.WaitUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SignInPage {

    private final WebDriver driver;
    private final WaitUtil wait;

        private final List<By> emailFieldCandidates = List.of(
            By.id("ap_email"),
            By.id("ap_email_login"),
            By.name("email"),
            By.cssSelector("input[type='email']"),
            By.cssSelector("input[name*='email']"),
            By.cssSelector("input[id*='email']")
        );
        private final List<By> continueButtonCandidates = List.of(
            By.id("continue"),
            By.cssSelector("input#continue"),
            By.cssSelector("button#continue"),
            By.cssSelector("input[type='submit'][name='continue']"),
            By.cssSelector("button[name='continue']")
        );

    private final By passwordField = By.id("ap_password");
    private final By signInSubmit = By.id("signInSubmit");

    private final By errorBox = By.id("auth-error-message-box");
    private final By errorText = By.xpath("//*[@id='auth-error-message-box']//*[normalize-space()]");
    private final By captchaInput = By.id("captchacharacters");
    private final By captchaImage = By.xpath("//*[@id='auth-captcha-image' or @alt='captcha']");
    private final By otpField = By.id("auth-mfa-otpcode");

    public SignInPage(WebDriver driver, WaitUtil wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public boolean isEmailStepReady() {
        if (findFirstVisible(emailFieldCandidates, 8) != null) return true;
        try {
            OverlayUtil.dismissCommonOverlays(driver, wait);
        } catch (Exception ignored) {
        }
        return findFirstVisible(emailFieldCandidates, 4) != null;
    }

    public void openDirectSignIn(String signInUrl) {
        driver.get(signInUrl);
    }

    public void dismissOverlaysBestEffort(boolean enabled) {
        if (!enabled) return;
        OverlayUtil.dismissCommonOverlays(driver, wait);
    }

    public void enterEmailOrPhone(String value) {
        WebElement field = findFirstVisible(emailFieldCandidates, 8);
        if (field == null) {
            throw new IllegalStateException("Email input not found on sign-in page");
        }
        field.click();
        field.clear();
        if (value != null) field.sendKeys(value);
        String typed = field.getAttribute("value");
        LogUtil.info("Email input typedLength=" + (typed == null ? 0 : typed.length()));
    }

    public void clickContinue() {
        By continueBtn = findFirstVisibleLocator(continueButtonCandidates, 5);
        if (continueBtn != null) {
            wait.click(continueBtn);
            return;
        }
        throw new IllegalStateException("Continue button not found on sign-in page");
    }

    public boolean isPasswordStepReady() {
        return wait.isVisible(passwordField, 4);
    }

    public void enterPassword(String value) {
        wait.type(passwordField, value);
    }

    public void clickSignIn() {
        wait.click(signInSubmit);
    }

    public boolean isCaptchaPresent() {
        return wait.isPresent(captchaInput, 1) || wait.isPresent(captchaImage, 1);
    }

    public boolean isTwoFaPresent() {
        return wait.isPresent(otpField, 1);
    }

    public String getErrorMessageOrEmpty() {
        if (!wait.isPresent(errorBox, 2)) return "";
        WebElement el = wait.findFirstVisible(errorText, 2);
        return el == null ? "" : el.getText().trim();
    }

    private WebElement findFirstVisible(List<By> locators, long secondsPerLocator) {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(secondsPerLocator);
        while (System.nanoTime() < deadline) {
            for (By by : locators) {
                WebElement el = wait.findFirstVisible(by, 1);
                if (el != null) return el;
            }
        }
        return null;
    }

    private By findFirstVisibleLocator(List<By> locators, long totalSeconds) {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(totalSeconds);
        while (System.nanoTime() < deadline) {
            for (By by : locators) {
                if (wait.isVisible(by, 1)) return by;
            }
        }
        return null;
    }
}
