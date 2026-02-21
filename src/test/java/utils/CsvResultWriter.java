package utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CsvResultWriter {

    private static final String[] HEADER = new String[]{
            "runId","testId","testName","usernameInput","expectedOutcome","actualOutcome",
            "expectedMessageMode","expectedMessage","actualMessage","status","reason",
            "timestamp","screenshotPath","logPath"
    };

    public static synchronized void appendRow(
            String resultsCsvPath,
            String runId,
            String testId,
            String testName,
            String usernameInput,
            String expectedOutcome,
            String actualOutcome,
            String expectedMessageMode,
            String expectedMessage,
            String actualMessage,
            String status,
            String reason,
            String screenshotPath,
            String logPath
    ) {
        try {
            Path p = Path.of(resultsCsvPath);
            Files.createDirectories(p.getParent());
            boolean exists = Files.exists(p) && Files.size(p) > 0;

            try (Writer out = new BufferedWriter(new FileWriter(resultsCsvPath, true));
                 CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {

                if (!exists) printer.printRecord((Object[]) HEADER);

                String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                printer.printRecord(
                        safe(runId),
                        safe(testId),
                        safe(testName),
                        safe(usernameInput),
                        safe(expectedOutcome),
                        safe(actualOutcome),
                        safe(expectedMessageMode),
                        safe(expectedMessage),
                        safe(actualMessage),
                        safe(status),
                        safe(reason),
                        safe(ts),
                        safe(screenshotPath),
                        safe(logPath)
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to write results CSV: " + resultsCsvPath, e);
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace("\n", " ").replace("\r", " ").trim();
    }
}
