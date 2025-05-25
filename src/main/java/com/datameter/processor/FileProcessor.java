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

/**
 * Handles reading input files, processing usage data, logging malformed lines,
 * and writing the final usage report.
 */
public class FileProcessor {

    private final DataAggregator aggregator = new DataAggregator();
    private final CostCalculator costCalculator = new CostCalculator("config.properties");
    private final List<String> malformedLines = new ArrayList<>();

    public void processDirectory(String inputDirPath) {
        try {
            Files.walk(Paths.get(inputDirPath))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt") || p.toString().endsWith(".log"))
                    .sorted().forEach(this::processFile);

            Map<String, MobileUsage> usageMap = aggregator.getAggregatedData();
//            System.out.println("read file data"+usageMap);
//            printOutput(usageMap);
            writeOutputFile("data_usage_report.txt", usageMap);
            writeMalformedLines("malformed_lines.log");


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
                    malformedLines.add("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read file:" + filePath + ", Reason: " + e.getMessage());
        }
    }

    private void printOutput(Map<String, MobileUsage> usageMap) {
        System.out.println("\"Mobile Number|4G|5G|4G Roaming|5G Roaming|Cost\"");
        for (MobileUsage usage : usageMap.values()) {
//            System.out.println("inside");
            double cost = costCalculator.calculateCost(usage);
            System.out.printf("%s|%d|%d|%d|%d|%.0f%n",
                    usage.getMobileNumber(),
                    usage.getData4GHome(),
                    usage.getData5GHome(),
                    usage.getData4GRoaming(),
                    usage.getData5GRoaming(),
                    cost);
        }
    }
}
