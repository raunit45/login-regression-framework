package utils;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RunContext {
    private static String runId;
    private static String logPath;
    private static String screenshotsDir;

    public static void init(ConfigReader config) {
        String prefix = config.getRunIdPrefix();
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        runId = prefix + "-" + ts;

        screenshotsDir = Path.of(config.getScreenshotsBaseDir(), runId).toString();
        logPath = Path.of(config.getLogsBaseDir(), runId + ".log").toString();
    }

    public static String getRunId() { return runId; }
    public static String getLogPath() { return logPath; }
    public static String getScreenshotsDir() { return screenshotsDir; }
}
