// DataRecord.java
package com.datameter.model;

/**
 * Represents a single line of data usage from the input file.
 */
public class DataRecord {
    private final String mobileNumber;
    private final String towerId;
    private final int data4G;
    private final int data5G;
    private final boolean isRoaming;

    public DataRecord(String mobileNumber, String towerId, int data4G, int data5G, boolean isRoaming) {
        this.mobileNumber = mobileNumber;
        this.towerId = towerId;
        this.data4G = data4G;
        this.data5G = data5G;
        this.isRoaming = isRoaming;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getTowerId() {
        return towerId;
    }

    public int getData4G() {
        return data4G;
    }

    public int getData5G() {
        return data5G;
    }

    public boolean isRoaming() {
        return isRoaming;
    }

    @Override
    public String toString() {
        return String.format("Mobile: %s | Tower: %s | 4G: %d | 5G: %d | Roaming: %b",
                mobileNumber, towerId, data4G, data5G, isRoaming);
    }
}
