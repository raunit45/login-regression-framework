package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {

    public static synchronized void info(String msg) { write("INFO", msg); }
    public static synchronized void warn(String msg) { write("WARN", msg); }

    private static void write(String level, String msg) {
        try {
            String path = RunContext.getLogPath();
            if (path == null || path.isBlank()) return;

            Path p = Path.of(path);
            Files.createDirectories(p.getParent());

            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String line = ts + " [" + level + "] " + msg;

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
                bw.write(line);
                bw.newLine();
            }
        } catch (Exception ignored) {}
    }
}
