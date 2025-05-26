package com.datameter;

import com.datameter.model.DataRecord;
import com.datameter.service.DataAggregator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataAggregatorTest {

    private DataAggregator aggregator;

    @BeforeEach
    void setUp() {
        aggregator = new DataAggregator();
    }

    @Test
    void testValidLineParsesCorrectly() {
        String line = "9000600600|TowerA|100|200|Yes";
        DataRecord record = aggregator.parseLine(line);

        assertEquals("9000600600", record.getMobileNumber());
        assertEquals("TowerA", record.getTowerId());
        assertEquals(100, record.getData4G());
        assertEquals(200, record.getData5G());
        assertTrue(record.isRoaming());
    }

    @Test
    void testInvalidMobileNumberThrows() {
        String line = "1234ABCD|TowerX|100|100|Yes";
        Exception ex = assertThrows(IllegalArgumentException.class, () -> aggregator.parseLine(line));
        assertTrue(ex.getMessage().contains("Invalid mobile number"));
    }

    @Test
    void testMissingTowerThrows() {
        String line = "9000600600||100|100|No";
        Exception ex = assertThrows(IllegalArgumentException.class, () -> aggregator.parseLine(line));
        assertTrue(ex.getMessage().contains("Missing tower"));
    }

    @Test
    void testNegativeDataThrows() {
        String line = "9000600600|TowerA|-100|200|Yes";
        Exception ex = assertThrows(IllegalArgumentException.class, () -> aggregator.parseLine(line));
        assertTrue(ex.getMessage().contains("4G data cannot be negative"));
    }

    @Test
    void testBadRoamingFieldThrows() {
        String line = "9000600600|TowerA|100|200|Maybe";
        Exception ex = assertThrows(IllegalArgumentException.class, () -> aggregator.parseLine(line));
        assertTrue(ex.getMessage().contains("Invalid roaming value"));
    }
}
