package data_ingestion;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;

public class BitcoinPriceFetcherTest {
    public static void main(String[] args) {
        testLogging();
    }

    public static void testLogging() {
        // Captures the log output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        try {
            // The method to test the logging for
            BitcoinPriceFetcher.PriceTime priceTime = BitcoinPriceFetcher.getCurrentPriceAndTime();
            // Verify logging
            String logOutput = outputStream.toString();
            assert logOutput.contains("Starting request to fetch Bitcoin price and time.");
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "IOException occurred during testing.";
        } finally {
            System.setOut(originalOut);
        }

        // Captures the log output
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        PrintStream originalOut2 = System.out;
        System.setOut(new PrintStream(outputStream2));
        try {
            // The method to test the logging for
            BitcoinPriceFetcher.PriceTime priceTime = BitcoinPriceFetcher.getCurrentPriceAndTime();
            // Verify logging
            String logOutput = outputStream2.toString();
            assert logOutput.contains("Received response code:");
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "IOException occurred during testing.";
        } finally {
            System.setOut(originalOut2);
        }

        // Captures the log output
        ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
        PrintStream originalOut3 = System.out;
        System.setOut(new PrintStream(outputStream3));
        try {
            // The method to test the logging for
            BitcoinPriceFetcher.PriceTime priceTime = BitcoinPriceFetcher.getCurrentPriceAndTime();
            // Verify logging
            String logOutput = outputStream3.toString();
            assert logOutput.contains("Response received:");
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "IOException occurred during testing.";
        } finally {
            System.setOut(originalOut3);
        }

        // Captures the log output
        ByteArrayOutputStream outputStream4 = new ByteArrayOutputStream();
        PrintStream originalOut4 = System.out;
        System.setOut(new PrintStream(outputStream4));
        try {
            // The method to test the logging for
            BitcoinPriceFetcher.PriceTime priceTime = BitcoinPriceFetcher.getCurrentPriceAndTime();
            // Verify logging
            String logOutput = outputStream4.toString();
            assert logOutput.contains("Fetched price: " + priceTime.getPrice() + " and time: " + priceTime.getTime());
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "IOException occurred during testing.";
        } finally {
            System.setOut(originalOut4);
        }


        System.out.println("Logging test passed.");
    }
}
