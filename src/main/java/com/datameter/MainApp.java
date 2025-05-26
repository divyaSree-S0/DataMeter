// MainApp.java
package com.datameter;

import com.datameter.processor.FileProcessor;

/**
 * Entry point for the Data Meter application.
 *
 * This program reads large input files containing mobile data usage logs,
 * aggregates the data per mobile number, classifies it into 4G/5G Home/Roaming,
 * and generates a cost report. Malformed lines are logged separately.
 *
 * Usage:
 *   java -jar DataMeter.jar <input-directory>
 *
 * Requirements:
 * - Java 21
 * - config.properties in project root with rate and threshold values
 * - Input files with format:
 *     MobileNumber|Tower|4G|5G|Roaming
 */
public class MainApp {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("No input directory given.\nTry: java -jar DataMeter.jar <input-directory>");
            System.exit(1);
        }
        String inputDir = args[0];
//        System.out.println("input argument"+args);
        FileProcessor processor = new FileProcessor();
        processor.processDirectory(inputDir);
    }
}
