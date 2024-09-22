import data_ingestion.BitcoinPriceFetcher;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
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
    }
}




