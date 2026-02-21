package utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private final Properties props = new Properties();

    public ConfigReader(String path) {
        try (InputStream is = new FileInputStream(path)) {
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config: " + path, e);
        }
    }

    private String get(String key) {
        String v = props.getProperty(key);
        return v == null ? "" : v.trim();
    }

    public String getBaseUrl() { return get("base.url"); }
    public String getSignInPath() {
        String v = get("signin.path");
        return v.isBlank() ? "/ap/signin" : v;
    }
    public String getBrowser() { return get("browser"); }
    public boolean isHeadless() { return "true".equalsIgnoreCase(get("headless")); }
    public String getWindowSize() { return get("window.size"); }

    public long getImplicitWaitSeconds() { return parseLong("implicit.wait.seconds"); }
    public long getExplicitWaitSeconds() { return parseLong("explicit.wait.seconds"); }
    public long getPageLoadTimeoutSeconds() { return parseLong("page.load.timeout.seconds"); }

    public String getLoginDataCsv() { return get("login.data.csv"); }
    public String getResultsCsv() { return get("results.csv"); }

    public String getScreenshotsBaseDir() { return get("screenshots.base.dir"); }
    public String getLogsBaseDir() { return get("logs.base.dir"); }
    public String getRunIdPrefix() { return get("run.id.prefix"); }

    public boolean useHomepageSignIn() { return "true".equalsIgnoreCase(get("use.homepage.signin")); }
    public boolean attemptDismissOverlays() { return "true".equalsIgnoreCase(get("attempt.dismiss.overlays")); }

    private long parseLong(String key) {
        String v = get(key);
        if (v.isBlank()) return 0;
        try { return Long.parseLong(v); } catch (Exception e) { return 0; }
    }
}
