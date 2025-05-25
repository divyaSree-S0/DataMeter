// CostCalculator.java
package com.datameter.service;

import com.datameter.model.MobileUsage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Calculates the cost of data usage for a given mobile number.
 * Rates and thresholds are loaded from a configuration file.
 */
public class CostCalculator {

    private final double rate4G;
    private final double rate5G;
    private final double roamingMultiplier4G;
    private final double roamingMultiplier5G;
    private final long usageThreshold;
    private final double thresholdMultiplier;

    public CostCalculator(String configPath) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(configPath)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file", e);
        }

        this.rate4G = Double.parseDouble(props.getProperty("rate.4g", "0.01"));
        this.rate5G = Double.parseDouble(props.getProperty("rate.5g", "0.02"));
        this.roamingMultiplier4G = Double.parseDouble(props.getProperty("roaming.multiplier.4g", "1.1"));
        this.roamingMultiplier5G = Double.parseDouble(props.getProperty("roaming.multiplier.5g", "1.15"));
        this.usageThreshold = Long.parseLong(props.getProperty("usage.threshold", "100000"));
        this.thresholdMultiplier = Double.parseDouble(props.getProperty("threshold.multiplier", "1.05"));
    }

    /**
     * Calculates cost based on usage and config parameters.
     */
    public double calculateCost(MobileUsage usage) {
        double cost = 0.0;

        cost += usage.getData4GHome() * rate4G;
        cost += usage.getData5GHome() * rate5G;
        cost += usage.getData4GRoaming() * rate4G * roamingMultiplier4G;
        cost += usage.getData5GRoaming() * rate5G * roamingMultiplier5G;

        if (usage.getTotalUsage() > usageThreshold) {
            cost *= thresholdMultiplier;
        }

        return Math.round(cost*100.0)/100.0;
    }
}
