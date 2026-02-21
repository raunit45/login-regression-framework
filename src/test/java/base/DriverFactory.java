package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import utils.ConfigReader;

import java.time.Duration;

public class DriverFactory {

    public static WebDriver createDriver(ConfigReader config) {
        String browser = config.getBrowser();
        boolean headless = config.isHeadless();
        String windowSize = config.getWindowSize();

        WebDriver driver;

        if ("edge".equalsIgnoreCase(browser)) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            if (headless) options.addArguments("--headless=new");
            if (windowSize != null && !windowSize.isBlank()) options.addArguments("--window-size=" + windowSize);
            driver = new EdgeDriver(options);
        } else {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            if (headless) options.addArguments("--headless=new");
            if (windowSize != null && !windowSize.isBlank()) options.addArguments("--window-size=" + windowSize);
            driver = new ChromeDriver(options);
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeoutSeconds()));
        long implicit = config.getImplicitWaitSeconds();
        if (implicit > 0) driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));
        return driver;
    }
}
