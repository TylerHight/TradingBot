package preprocessing.needsRefactoring.dualSAMA;

import java.util.LinkedList;
import java.util.Queue;

public class Solution {
    private static double shortSum = 0; // Sum of the most recent SHORT_WINDOW prices
    private static Queue<Double> shortWindowQueue = new LinkedList<>(); // Tracks prices in short window
    private static double longSum = 0; // Sum of the most recent longWindow prices
    private static Queue<Double> longWindowQueue = new LinkedList<>(); // Tracks prices in long window

    // Validate inputs
    public static void validateInputs(int shortWindow, int longWindow) {
        if (shortWindow <= 0 || longWindow <= 0) {
            throw new IllegalArgumentException("Input window lengths must be positive integers.");
        }
        if (shortWindow >= longWindow) {
            throw new IllegalArgumentException("The input short window length must be less than the long window.");
        }
    }

    // Runs the simulation through the data points
    public static Output runSAMA(SentimentPriceData[] priceData, int shortWindow, int longWindow) {
        // Validate Inputs
        validateInputs(shortWindow, longWindow);
        // Arrays that store the SMA, SAMA, and their differences
        double[] smaValues = new double[priceData.length];
        double[] samaValues = new double[priceData.length];
        double[] differences = new double[priceData.length];
        // Run the simulation for each data point
        for (int i = 0; i < priceData.length; i++) {
            double newPrice = priceData[i].getPrice();
            double sentiment = priceData[i].getSentiment();
            // Calculate SMA
            double sma = calculateSMA(newPrice, shortWindow);
            smaValues[i] = Math.round(sma * 10000.0) / 10000.0;
            // Calculate SAMA
            double sama = calculateSAMA(newPrice, sentiment, shortWindow, longWindow);
            samaValues[i] = Math.round(sama * 10000.0) / 10000.0;
            // Calculate the difference but set to 0 if either SMA or SAMA are 0
            double difference = (sma == 0 || sama == 0) ? 0 : (sma - sama);
            differences[i] = (sma == 0 || sama == 0) ? 0 : Math.round(difference * 10000.0) / 10000.0;
        }
        return new Output(smaValues, samaValues, differences);
    }

    // Calculate the average for the short window
    public static double calculateSMA(double newPrice, int shortWindow) {
        // Add the new price to the short window sum
        shortWindowQueue.add(newPrice);
        shortSum += newPrice;
        // Remove the oldest price to maintain window size
        if (shortWindowQueue.size() > shortWindow) {
            shortSum -= shortWindowQueue.poll();
        }
        // Return the average once the window has the correct number of values, else 0
        return shortWindowQueue.size() == shortWindow ? shortSum / shortWindow : 0;
    }

    // Calculate Sentiment-Adjusted Moving Average (SAMA) for the long window
    public static double calculateSAMA(double newPrice, double sentiment, int shortWindow, int longWindow) {
        // Calculate adjusted long window size (becomes shorter with stronger sentiment to react to volatility more quickly)
        int adjustedLongWindow = calculateLongWindow(sentiment, shortWindow, longWindow);
        // Add the newest price to the long window sum
        longWindowQueue.add(newPrice);
        longSum += newPrice;
        // Remove the oldest price if to maintain the correct window size
        if (longWindowQueue.size() > adjustedLongWindow) {
            longSum -= longWindowQueue.poll();
        }
        // Return the average for the long window if the window is at the correct size
        return longWindowQueue.size() == adjustedLongWindow ? longSum / adjustedLongWindow : 0;
    }

    // Calculate the long-term window size based on sentiment
    private static int calculateLongWindow(double sentiment, int shortWindow, int longWindow) {
        // Sentiment ranges from -1.0 to 1.0
        double adjustedLongWindow = longWindow - ((longWindow - shortWindow - 1) * Math.abs(sentiment));
        return (int) Math.round(adjustedLongWindow);
    }

    // Data class to hold price and sentiment data
    static class SentimentPriceData {
        private final String time;
        private final double price;
        private final double sentiment;

        public SentimentPriceData(String time, double price, double sentiment) {
            this.time = time;
            this.price = price;
            this.sentiment = sentiment;
        }

        public String getTime() {
            return time;
        }

        public double getPrice() {
            return price;
        }

        public double getSentiment() {
            return sentiment;
        }
    }

    // Output class to hold results
    static class Output {
        public final double[] smaValues;
        public final double[] samaValues;
        public final double[] differences;

        public Output(double[] smaValues, double[] samaValues, double[] differences) {
            this.smaValues = smaValues;
            this.samaValues = samaValues;
            this.differences = differences;
        }
    }
}
