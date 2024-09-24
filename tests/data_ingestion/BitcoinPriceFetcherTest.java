package data_ingestion;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.fail;

public class BitcoinPriceFetcherTest {

    @Test
    public void testFetchingDailyHistoricalPrices() {
        try {
            DailyPriceTime historicalPrices = BitcoinPriceFetcher.getDailyHistoricalPrices("2024-09-01", "2024-09-10");
            assertFalse(historicalPrices.toString().isEmpty(), "Historical prices should not be empty");
        } catch (IOException e) {
            fail("Test failed due to IOException: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidInputForDailyHistoricalPrices() {
        assertThrows(IOException.class, () -> {
            BitcoinPriceFetcher.getDailyHistoricalPrices("invalid_date", "2024-09-10");
        }, "An IOException should be thrown for invalid date input");
    }

    @Test
    public void testMinutePriceTimeWithMockData() {
        try {
            BitcoinPriceFetcher.MinutePriceTime simulatedMinutePrices = new BitcoinPriceFetcher.MinutePriceTime();
            // Mock data
            simulatedMinutePrices.addPrice(1695634800000L, 35250.00);
            simulatedMinutePrices.addPrice(1695634860000L, 35260.00);
            assertFalse(simulatedMinutePrices.toString().isEmpty(), "Minute-level prices should contain data");
        } catch (Exception e) {
            fail("Test failed due to Exception: " + e.getMessage());
        }
    }

    @Test
    public void testFailingToGetMinuteLevelPrices() {
        assertThrows(IOException.class, () -> {
            BitcoinPriceFetcher.getMinuteLevelPrices("INVALID_SYMBOL", "1m", "Invalid API Key");
        }, "An IOException should be thrown for invalid symbol or API key");
    }
}
