package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtil {
    private final WebDriver driver;
    private final long defaultSeconds;

    public WaitUtil(WebDriver driver, long seconds) {
        this.driver = driver;
        this.defaultSeconds = seconds;
    }

    public void click(By by) {
        new WebDriverWait(driver, Duration.ofSeconds(defaultSeconds))
                .until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    public void type(By by, String value) {
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(defaultSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
        el.clear();
        if (value != null) el.sendKeys(value);
    }

    public boolean isPresent(By by, long secondsOverride) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(secondsOverride))
                    .until(ExpectedConditions.presenceOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isVisible(By by, long secondsOverride) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(secondsOverride))
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement findFirstVisible(By by, long secondsOverride) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(secondsOverride))
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            return null;
        }
    }
}
