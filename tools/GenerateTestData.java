package tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateTestData {
    public static void main(String[] args) throws IOException {
        String[] towers = {"TowerA", "InAir123", "Edge5G", "CityX", "ZoneZ"};
        String[] roaming = {"Yes", "No","true","false"};
        Random rand = new Random();

        int numberOfFiles = 3;
        int[] linesPerFile = {1000, 5000, 100000};
        String baseDir = "input_files/";

        for (int i = 0; i < numberOfFiles; i++) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(baseDir + "generated_input_" + (i+1) + ".txt"))) {
                double MALFORMED_RATE = 0.05;

                for (int j = 0; j < linesPerFile[i]; j++) {
                    boolean isMalformed = rand.nextDouble() < MALFORMED_RATE;

                    if (isMalformed) {
                        int type = rand.nextInt(5);
                        switch (type) {
                            case 0: // Missing field
                                writer.write("9000600600|TowerA|1234||\n");
                                break;
                            case 1: // Bad 4G value
                                writer.write("9000600600|TowerA|abc|456|No\n");
                                break;
                            case 2: // Bad roaming value
                                writer.write("9000600600|TowerA|1000|2000|MAYBE\n");
                                break;
                            case 3: // Short/long phone number
                                writer.write("12345|TowerB|100|200|Yes\n");
                                break;
                            case 4: // Empty tower
                                writer.write("9000600600||100|200|No\n");
                                break;
                        }
                    } else {
                        String mobile = String.valueOf(9000000000L + rand.nextInt(100000000));
                        String tower = towers[rand.nextInt(towers.length)];
                        int data4g = rand.nextInt(50000);
                        int data5g = rand.nextInt(50000);
                        String roam = roaming[rand.nextInt(roaming.length)];
                        writer.write(mobile + "|" + tower + "|" + data4g + "|" + data5g + "|" + roam + "\n");
                    }
                }

            }
        }

        System.out.println("Generated test files in input_files/ directory.");
    }
}
