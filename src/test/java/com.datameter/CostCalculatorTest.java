package com.datameter;

import com.datameter.model.MobileUsage;
import com.datameter.service.CostCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CostCalculatorTest {

    private CostCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new CostCalculator("config.properties");
    }

    @Test
    void testBasicHomeCostCalculation() {
        MobileUsage usage = new MobileUsage("9000000000");
        usage.addUsage(1000, 2000, false);  // Home data

        double cost = calculator.calculateCost(usage);

        // With 4G = 0.01, 5G = 0.02
        assertEquals(1000 * 0.01 + 2000 * 0.02, cost, 0.01);
    }

    @Test
    void testRoamingCostCalculation() {
        MobileUsage usage = new MobileUsage("9000000001");
        usage.addUsage(500, 0, true);  // 4G roaming
        usage.addUsage(0, 300, true);  // 5G roaming

        double cost = calculator.calculateCost(usage);

        // 4G @ 0.01 * 1.1, 5G @ 0.02 * 1.15
        double expected = (500 * 0.01 * 1.1) + (300 * 0.02 * 1.15);
        assertEquals(expected, cost, 0.01);
    }

    @Test
    void testThresholdSurchargeApplied() {
        MobileUsage usage = new MobileUsage("9000000002");
        usage.addUsage(90000, 15000, false);  // 105000 KB

        double costWithoutSurcharge = 90000 * 0.01 + 15000 * 0.02;
        double expected = costWithoutSurcharge * 1.05;

        double cost = calculator.calculateCost(usage);
        assertEquals(expected, cost, 0.1);
    }
}
