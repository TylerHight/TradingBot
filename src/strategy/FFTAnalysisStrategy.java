package strategy;

import data_ingestion.BitcoinPriceFetcher;
import preprocessing.TimeSeriesAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static data_ingestion.BitcoinPriceFetcher.getMinuteLevelPrices;

public class FFTAnalysisStrategy {
    private static final Logger logger = LoggerFactory.getLogger(FFTAnalysisStrategy.class); // Initialize logger
    private TimeSeriesAnalysis timeSeriesAnalysis;

    public FFTAnalysisStrategy() {
        this.timeSeriesAnalysis = new TimeSeriesAnalysis();
    }

    /**
     * Adds a price and timestamp to the analysis.
     * @param price The price to add.
     * @param timestamp The associated timestamp.
     */
    public void addPrice(double price, long timestamp) {
        timeSeriesAnalysis.addPrice(price, timestamp);
    }

    /**
     * Analyzes the Fourier Transform results to compute a trend score.
     *
     * @return A trend score indicating market conditions.
     */
    public double analyzePrices() {
        List<Double[]> frequencyMagnitudes = timeSeriesAnalysis.calculateFourierTransform();

        // Implement your scoring logic here
        // For example, analyze the magnitudes to compute a score
        double score = 0.0;

        // Calculate score based on frequency magnitudes
        for (Double[] frequencyMagnitude : frequencyMagnitudes) {
            double magnitude = frequencyMagnitude[1];
            double frequency = frequencyMagnitude[0];

            // Example scoring logic
            // You can adjust the scoring formula based on your strategy
            score += magnitude * Math.sin(frequency); // Use magnitude weighted by the sine of the frequency
        }

        // Normalize the score based on the number of frequency bins
        if (!frequencyMagnitudes.isEmpty()) {
            score /= frequencyMagnitudes.size();
        }

        return score; // Return the computed trend score
    }

    public static void main(String[] args) {
        FFTAnalysisStrategy strategy = new FFTAnalysisStrategy(); // Create an instance of the strategy
        try {
            BitcoinPriceFetcher.MinutePriceTime minutePrices = getMinuteLevelPrices("BTCUSDT", "1m", System.getenv("BINANCE_API_KEY"));
            System.out.println(minutePrices);
        } catch (IOException e) {
            logger.error("Error occurred while fetching minute prices: {}", e.getMessage(), e);
        }
    }
}
