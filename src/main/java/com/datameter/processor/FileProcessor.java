// FileProcessor.java
package com.datameter.processor;

import com.datameter.model.DataRecord;
import com.datameter.model.MobileUsage;
import com.datameter.service.CostCalculator;
import com.datameter.service.DataAggregator;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Handles reading input files, processing usage data, logging malformed lines,
 * and writing the final usage report.
 */
public class FileProcessor {

    private final DataAggregator aggregator = new DataAggregator();
    private final CostCalculator costCalculator = new CostCalculator("config.properties");
    private final List<String> malformedLines = new ArrayList<>();
    private static final String OUTPUT_DIR = "output";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HH-mm-ss");


    public void processDirectory(String inputDirPath) {
        try {
            Files.walk(Paths.get(inputDirPath))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt") || p.toString().endsWith(".log"))
                    .sorted().forEach(this::processFile);

            Map<String, MobileUsage> usageMap = aggregator.getAggregatedData();
//            System.out.println("read file data"+usageMap);
//            printOutput(usageMap);
            writeOutputFile(usageMap);
            writeMalformedLines();

        } catch (IOException e) {
            System.err.println("Error reading directory:" + e.getMessage());
        }
    }

    private void processFile(Path filePath) {
        System.out.println("\"Processing file: \"" + filePath);
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    DataRecord record = aggregator.parseLine(line);
                    aggregator.aggregate(record);
                } catch (IllegalArgumentException e) {
//                    System.out.println("Skipping malformed line: " + line);
                    malformedLines.add("Skipping malformed line: " + line + " ; Reason: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read file:" + filePath + ", Reason: " + e.getMessage());
        }
    }

//    private void printOutput(Map<String, MobileUsage> usageMap) {
//        System.out.println("\"Mobile Number|4G|5G|4G Roaming|5G Roaming|Cost\"");
//        for (MobileUsage usage : usageMap.values()) {
////            System.out.println("inside");
//            double cost = costCalculator.calculateCost(usage);
//            System.out.printf("%s|%d|%d|%d|%d|%.0f%n",
//                    usage.getMobileNumber(),
//                    usage.getData4GHome(),
//                    usage.getData5GHome(),
//                    usage.getData4GRoaming(),
//                    usage.getData5GRoaming(),
//                    cost);
//        }
//    }
    private void writeOutputFile(Map<String, MobileUsage> usageMap) {
        try{
            Files.createDirectories(Paths.get(OUTPUT_DIR));
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String outputFileName = OUTPUT_DIR + "/data_usage_report_" + timestamp + ".txt";
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFileName))) {
                writer.write("Mobile Number|4G|5G|4G Roaming|5G Roaming|Cost\n");
                for (MobileUsage usage : usageMap.values()) {
                    double cost = costCalculator.calculateCost(usage);
                    writer.write(String.format("%s|%d|%d|%d|%d|%.2f%n",
                            usage.getMobileNumber(),
                            usage.getData4GHome(),
                            usage.getData5GHome(),
                            usage.getData4GRoaming(),
                            usage.getData5GRoaming(),
                            cost));
                }
                System.out.println("Output report written to: " + outputFileName);
            }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }

    private void writeMalformedLines() {
        if (malformedLines.isEmpty()) return;
        try {
            Files.createDirectories(Paths.get(OUTPUT_DIR));
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String errorFileName = OUTPUT_DIR + "/skipped_lines_" + timestamp + ".log";
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(errorFileName))) {
                for (String line : malformedLines) {
                    writer.write(line + "\n");
                }
                System.out.println("Error lines logged to: " + errorFileName);
            }
        } catch (IOException e) {
            System.err.println("Error writing malformed lines: " + e.getMessage());
        }
    }
}
