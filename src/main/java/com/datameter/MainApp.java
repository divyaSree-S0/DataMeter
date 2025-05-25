// MainApp.java
package com.datameter;

import com.datameter.processor.FileProcessor;

/**
 * Entry point for the Data Meter application.
 * Expects one argument: path to the directory containing input data files.
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
