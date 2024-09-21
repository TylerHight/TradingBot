package preprocessing;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.List;

public class TimeSeriesAnalysis {
    private List<Double> prices;
    private List<Long> timestamps;

    // The prices and their associated timestamps
    public TimeSeriesAnalysis() {
        prices = new ArrayList<>();
        timestamps = new ArrayList<>();
    }

    // Adds a new price and timestamp
    public void addPrice(double price, long timestamp) {
        prices.add(price);
        timestamps.add(timestamp);
    }

    /**
     * Calculates the Simple Moving Average (SMA) over a specified period.
     * @param period The number of data points for each average.
     * @return A list of moving averages.
     */
    public List<Double> calculateMovingAverage(int period) {
        List<Double> movingAverages = new ArrayList<>();
        if (prices.size() < period) {
            return movingAverages; // Not enough data to calculate the moving average
        }
        // Go through the prices from start to end while calculating the moving average
        for (int i = 0; i <= prices.size() - period; i++) {
            double sum = 0.0;
            for (int j = 0; j < period; j++) {
                sum += prices.get(i + j);
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
        if (prices.size() < period) {
            return emaValues; // Not enough data to calculate EMA
        }

        double multiplier = 2.0 / (period + 1);
        double sum = 0.0;

        // Calculate the first EMA (SMA for the first period)
        for (int i = 0; i < period; i++) {
            sum += prices.get(i);
        }
        double ema = sum / period;
        emaValues.add(ema);

        // Continue calculating EMA for the values in prices
        for (int i = period; i < prices.size(); i++) {
            ema = ((prices.get(i) - ema) * multiplier) + ema;
            emaValues.add(ema);
        }
        return emaValues;
    }


    /**
     * Calculate the FFT of the prices. Note that there is no magnitude normalization,
     * but aliased frequencies are removed.
     * @return A list of Fourier Transform magnitudes.
     */
    public List<Double[]> calculateFourierTransform() {
        int n = prices.size();
        if (n == 0) {
            return new ArrayList<>(); // Return empty list if no prices are available
        }

        // Find the next power of two for padding
        int nextPowerOfTwo = 1;
        while (nextPowerOfTwo < n) {
            nextPowerOfTwo <<= 1;
        }

        // Create a padded array for FFT (zero-padding)
        double[] paddedPrices = new double[nextPowerOfTwo];
        for (int i = 0; i < n; i++) {
            paddedPrices[i] = prices.get(i);
        }

        // Check if we have enough timestamps to calculate the sampling interval
        if (timestamps.size() < 2) {
            System.out.println("Insufficient timestamps to calculate Fourier transform.");
            return new ArrayList<>(); // Return an empty list indicating a failure
        }

        // Calculate the FFT
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] fftResult = fft.transform(paddedPrices, TransformType.FORWARD);

        // Calculate sampling interval and frequency
        double samplingInterval = timestamps.get(1) - timestamps.get(0); // Interval in milliseconds
        double samplingFrequency = 1000.0 / samplingInterval; // Convert to Hz

        // Calculate magnitudes and corresponding frequencies (only for the first half)
        List<Double[]> frequencyMagnitudes = new ArrayList<>();
        int halfLength = fftResult.length / 2; // Only process the first half

        for (int k = 0; k < halfLength; k++) {
            double magnitude = fftResult[k].abs(); // Magnitude
            double frequency = k * samplingFrequency / nextPowerOfTwo; // Frequency for each bin
            frequencyMagnitudes.add(new Double[]{frequency, magnitude});
        }

        return frequencyMagnitudes; // Return list of frequency-magnitude pairs
    }

    public List<Double> getPrices() {
        return prices;
    }

    public List<Long> getTimestamps() {
        return timestamps;
    }
}
