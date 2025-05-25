// MobileUsage.java
package com.datameter.model;

/**
 * Aggregates total usage for a single mobile number across all categories.
 */
public class MobileUsage {
    private final String mobileNumber;
    private long data4GHome;
    private long data5GHome;
    private long data4GRoaming;
    private long data5GRoaming;

    public MobileUsage(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void addUsage(int data4G, int data5G, boolean isRoaming) {
        if (isRoaming) {
            this.data4GRoaming += data4G;
            this.data5GRoaming += data5G;
        } else {
            this.data4GHome += data4G;
            this.data5GHome += data5G;
        }
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public long getData4GHome() {
        return data4GHome;
    }

    public long getData5GHome() {
        return data5GHome;
    }

    public long getData4GRoaming() {
        return data4GRoaming;
    }

    public long getData5GRoaming() {
        return data5GRoaming;
    }

    public long getTotalUsage() {
        return data4GHome + data5GHome + data4GRoaming + data5GRoaming;
    }

    @Override
    public String toString() {
        return mobileNumber + "|" +
                data4GHome + "|" +
                data5GHome + "|" +
                data4GRoaming + "|" +
                data5GRoaming;
    }
}
