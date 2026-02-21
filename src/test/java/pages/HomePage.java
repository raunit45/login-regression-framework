package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.OverlayUtil;
import utils.WaitUtil;

public class HomePage {

    private final WebDriver driver;
    private final WaitUtil wait;

    private final By accountListLink = By.id("nav-link-accountList");

    public HomePage(WebDriver driver, WaitUtil wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void open(String baseUrl) {
        driver.get(baseUrl);
    }

    public void dismissOverlaysBestEffort(boolean enabled) {
        if (!enabled) return;
        OverlayUtil.dismissCommonOverlays(driver, wait);
    }

    public void clickSignInEntry() {
        wait.click(accountListLink);
    }
}
