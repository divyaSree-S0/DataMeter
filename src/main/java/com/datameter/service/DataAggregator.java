// DataAggregator.java
package com.datameter.service;

import com.datameter.model.DataRecord;
import com.datameter.model.MobileUsage;

import java.util.HashMap;
import java.util.Map;

/**
 * Parses input lines into DataRecord objects and aggregates them.
 * Validates:
 * - 10-digit mobile number
 * - Non-empty tower name
 * - Non-negative 4G/5G values
 * - Roaming field must be Yes/No/True/False (case-insensitive)
 * Invalid lines throw IllegalArgumentException (to be logged).
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

        if (mobile.isEmpty() || !mobile.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid mobile number: " + mobile);
        }

        if (tower.isEmpty()) {
            throw new IllegalArgumentException("Missing tower name");
        }

        int data4G = parseIntSafe(parts[2].trim(),"4G data");
        int data5G = parseIntSafe(parts[3].trim(),"5G data");
//        boolean roaming = parts[4].trim().equalsIgnoreCase("Yes");
        String roamingRaw = parts[4].trim().toLowerCase();

        boolean roaming;
        switch (roamingRaw) {
            case "yes":
            case "true":
                roaming = true;
                break;
            case "no":
            case "false":
                roaming = false;
                break;
            default:
                throw new IllegalArgumentException("Invalid roaming value: " + parts[4].trim());
        }

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

    private int parseIntSafe(String s,String label) {
        int value;
        try {
            value = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid "+label+" " + s);
        }
        if (value < 0){
            throw new IllegalArgumentException(label+" cannot be negative "+value);
        }
        return value;
    }
}
