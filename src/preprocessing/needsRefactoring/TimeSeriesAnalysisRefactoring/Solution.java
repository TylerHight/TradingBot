package preprocessing.needsRefactoring.TimeSeriesAnalysisRefactoring;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private List<Double> values;
    private List<Long> timestamps;
    private double lastEMA = Double.NaN; // Tracks the most recently calculated EMA
    private double rollingSum = 0.0; // Tracks the rolling sum for the SMA

    // The prices and their associated timestamps
    public Solution() {
        values = new ArrayList<>();
        timestamps = new ArrayList<>();
    }

    // Adds a new price and timestamp
    public void addPrice(double price, long timestamp) {
        values.add(price);
        timestamps.add(timestamp);
    }

    /**
     * Calculates the Simple Moving Average (SMA) for the most recent data point.
     * This method should be re-called as needed when new data points are added.
     * @param period The number of data points for each average.
     * @return The most recent moving average, or Double.NaN if not enough data.
     */
    public double calculateMovingAverage(int period) {
        boolean firstCalculation = false; // Flag for if the first average has been calculated or not

        if (values.size() < period) {
            return Double.NaN; // Not enough data to calculate the moving average
        }

        if (values.size() >= period && !firstCalculation) {
            firstCalculation = true;
            // Calculate the rolling sum for when the window size is met for the first time
            rollingSum = 0.0;
            for (int i = 0; i < period; i++) {
                rollingSum += values.get(values.size() - period + i);
            }
        } else {
            // Add the new value to the rolling sum and remove the oldest value
            rollingSum = rollingSum - values.get(values.size() - period - 1) + values.get(values.size() - 1);
        }

        // Calculate and return the average
        return rollingSum / period;
    }

    /**
     * Calculates the Exponential Moving Average (EMA) for the most recent data point.
     * This method should be re-called as needed when new data points are added.
     * @param period The number of data points for the EMA.
     * @return The most recent EMA, or Double.NaN if not enough data.
     */
    public double calculateEMA(int period) {
        if (values.size() < period) {
            return Double.NaN; // Not enough data to calculate EMA
        }

        // Value used in calculating EMA -- can be adjusted but is not typical to
        double multiplier = 2.0 / (period + 1);

        if (Double.isNaN(lastEMA)) {
            // The first EMA calculation is the SMA of the most recent 'period' values
            double sum = 0.0;
            for (int i = 0; i < period; i++) {
                sum += values.get(values.size() - period + i);
            }
            lastEMA = sum / period;
        } else {
            // Calculate EMA using the previous EMA and the latest price
            lastEMA = ((values.get(values.size() - 1) - lastEMA) * multiplier) + lastEMA;
        }

        return lastEMA;
    }


    public List<Double> getValues() {
        return values;
    }

    public List<Long> getTimestamps() {
        return timestamps;
    }
}
