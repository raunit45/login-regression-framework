package utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderUtil {

    public static List<TestData> readLoginData(String csvPath) {
        List<TestData> list = new ArrayList<>();
        try (Reader reader = new FileReader(csvPath);
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {

            for (CSVRecord r : parser) {
                list.add(new TestData(
                        r.get("testId"),
                        r.get("testName"),
                        r.get("flow"),
                        r.get("emailOrPhone"),
                        r.get("password"),
                        r.get("expectedOutcome"),
                        r.get("expectedMessageMode"),
                        r.get("expectedMessage")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV: " + csvPath, e);
        }
        return list;
    }
}
