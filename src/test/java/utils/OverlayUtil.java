package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OverlayUtil {

    private static final By acceptCookiesButton = By.id("sp-cc-accept");
    private static final By consentAcceptButton = By.xpath("//input[@type='submit' and (@name='accept' or @value='Accept')]|//button[@type='submit' and (normalize-space()='Accept' or normalize-space()='I Agree')]");
    private static final By dismissButton = By.xpath("//button[@aria-label='Close' or @aria-label='Dismiss']");

    public static void dismissCommonOverlays(WebDriver driver, WaitUtil wait) {
        try { if (wait.isPresent(acceptCookiesButton, 1)) { wait.click(acceptCookiesButton); LogUtil.info("Dismissed cookies overlay"); } } catch (Exception ignored) {}
        try { if (wait.isPresent(consentAcceptButton, 1)) { wait.click(consentAcceptButton); LogUtil.info("Dismissed consent overlay"); } } catch (Exception ignored) {}
        try { if (wait.isPresent(dismissButton, 1)) { wait.click(dismissButton); LogUtil.info("Dismissed overlay via dismiss button"); } } catch (Exception ignored) {}
    }
}
