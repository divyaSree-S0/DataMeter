// DataAggregator.java
package com.datameter.service;

import com.datameter.model.DataRecord;
import com.datameter.model.MobileUsage;

import java.util.HashMap;
import java.util.Map;

/**
 * Parses input lines into records and aggregates them by mobile number.
 */
public class DataAggregator {

    private final Map<String, MobileUsage> usageMap = new HashMap<>();

    /**
     * Parses a single line of input into a DataRecord object.
     *
     * @throws IllegalArgumentException if the line is malformed.
     */
    public DataRecord parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid number of fields");
        }

        String mobile = parts[0].trim();
        String tower = parts[1].trim();
        int data4G = parseIntSafe(parts[2].trim());
        int data5G = parseIntSafe(parts[3].trim());
        boolean roaming = parts[4].trim().equalsIgnoreCase("Yes");

        return new DataRecord(mobile, tower, data4G, data5G, roaming);
    }

    /**
     * Aggregates a single record into the usage map.
     */
    public void aggregate(DataRecord record) {
        usageMap
                .computeIfAbsent(record.getMobileNumber(), MobileUsage::new)
                .addUsage(record.getData4G(), record.getData5G(), record.isRoaming());
    }

    /**
     * Returns the final map of aggregated data.
     */
    public Map<String, MobileUsage> getAggregatedData() {
        return usageMap;
    }

    private int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + s);
        }
    }
}
