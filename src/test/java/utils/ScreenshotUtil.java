package utils;

import org.openqa.selenium.*;

import java.io.File;
import java.nio.file.*;

public class ScreenshotUtil {
    public static Path takeScreenshot(WebDriver driver, String dir, String filenameBase) {
        try {
            Files.createDirectories(Path.of(dir));
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String safeName = filenameBase.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path dest = Path.of(dir, safeName + ".png");
            Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            return dest;
        } catch (Exception e) {
            return null;
        }
    }
}
