package base;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.LogUtil;
import utils.RunContext;
import utils.ScreenshotUtil;

import java.nio.file.Path;

public class BaseTest {

    protected ConfigReader config;

    @BeforeClass(alwaysRun = true)
    @Parameters({"configPath"})
    public void beforeClass(@Optional("src/test/resources/config/config.properties") String configPathFromXml) {
        String configPathFromSys = System.getProperty("config");
        String effectivePath = (configPathFromSys != null && !configPathFromSys.isBlank()) ? configPathFromSys : configPathFromXml;

        this.config = new ConfigReader(effectivePath);
        RunContext.init(config);
        LogUtil.info("Run started. runId=" + RunContext.getRunId());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriver driver = DriverFactory.createDriver(config);
        DriverManager.setDriver(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = DriverManager.getDriver();
        try {
            if (driver != null && (result.getStatus() == ITestResult.FAILURE || result.getStatus() == ITestResult.SKIP)) {
                Path saved = ScreenshotUtil.takeScreenshot(driver, RunContext.getScreenshotsDir(), result.getName());
                LogUtil.info("Evidence screenshot=" + (saved == null ? "" : saved.toString()));
            }
        } catch (Exception ignored) {
        } finally {
            try { if (driver != null) driver.quit(); } catch (Exception ignored) {}
            DriverManager.unload();
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        LogUtil.info("Run finished. runId=" + RunContext.getRunId());
    }
}
