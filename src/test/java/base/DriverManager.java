package base;

import org.openqa.selenium.WebDriver;

public class DriverManager {
    private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

    public static void setDriver(WebDriver driver) {
        TL_DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        return TL_DRIVER.get();
    }

    public static void unload() {
        TL_DRIVER.remove();
    }
}
