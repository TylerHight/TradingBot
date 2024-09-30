package preprocessing;

import java.util.ArrayList;
import java.util.List;

public class TimeSeriesAnalysis {
    private List<Double> values;
    private List<Long> timestamps;
    private FourierTransform fourierTransform;

    // The prices and their associated timestamps
    public TimeSeriesAnalysis() {
        values = new ArrayList<>();
        timestamps = new ArrayList<>();
        fourierTransform = new FourierTransform();
    }

    // Adds a new price and timestamp
    public void addPrice(double price, long timestamp) {
        values.add(price);
        timestamps.add(timestamp);
    }

    /**
     * Calculates the Simple Moving Average (SMA) over a specified period.
     * @param period The number of data points for each average.
     * @return A list of moving averages.
     */
    public List<Double> calculateMovingAverage(int period) {
        List<Double> movingAverages = new ArrayList<>();
        if (values.size() < period) {
            return movingAverages; // Not enough data to calculate the moving average
        }
        // Go through the prices from start to end while calculating the moving average
        for (int i = 0; i <= values.size() - period; i++) {
            double sum = 0.0;
            for (int j = 0; j < period; j++) {
                sum += values.get(i + j);
            }
            movingAverages.add(sum / period);
        }
        return movingAverages;
    }

    /**
     * Calculates the Exponential Moving Average (EMA) over a specified period.
     * @param period The number of data points for the EMA.
     * @return A list of EMAs.
     */
    public List<Double> calculateEMA(int period) {
        List<Double> emaValues = new ArrayList<>();
        if (values.size() < period) {
            return emaValues; // Not enough data to calculate EMA
        }

        double multiplier = 2.0 / (period + 1);
        double sum = 0.0;

        // Calculate the first EMA (SMA for the first period)
        for (int i = 0; i < period; i++) {
            sum += values.get(i);
        }
        double ema = sum / period;
        emaValues.add(ema);

        // Continue calculating EMA for the values in prices
        for (int i = period; i < values.size(); i++) {
            ema = ((values.get(i) - ema) * multiplier) + ema;
            emaValues.add(ema);
        }
        return emaValues;
    }

    /**
     * Delegates the FFT calculation to the {@link FourierTransform} class.
     * @return A list of Fourier Transform magnitudes and their corresponding frequencies.
     */
    public List<Double[]> calculateFourierTransform() {
        return fourierTransform.calculateFourierTransform(values, timestamps);
    }

    public List<Double> getValues() {
        return values;
    }

    public List<Long> getTimestamps() {
        return timestamps;
    }
}
