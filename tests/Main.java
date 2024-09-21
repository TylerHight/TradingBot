import preprocessing.TimeSeriesAnalysis;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Test Case 1: Input is a single sine wave at 1 Hz
        TimeSeriesAnalysis ts1 = new TimeSeriesAnalysis();
        for (int i = 0; i < 1000; i++) {
            double time = i * 10; // 10 milliseconds intervals, sampling at 100 Hz (ensure Nyquist rate is met)
            double value = Math.sin(2 * Math.PI * 1 * (time / 1000)); // 1 Hz sine wave
            ts1.addPrice(value, (long) time);
        }
        List<Double[]> result1 = ts1.calculateFourierTransform();
        // Check magnitude at 1 Hz
        double magnitudeAt1Hz = getMagnitudeAtFrequency(result1, 1.0);
        assert magnitudeAt1Hz == 461.0;
    }

    // Helper method that gets the magnitude at a specific frequency (not exact)
    private static double getMagnitudeAtFrequency(List<Double[]> result, double frequency) {
        for (Double[] pair : result) {
            double freq = pair[0];
            double magnitude = pair[1];
            if (Math.abs(freq - frequency) < 0.1) { // Tolerance for frequency
                return (int) Math.round(magnitude); // Return the magnitude if a frequency falls within the tolerance
            }
        }
        return 0.0; // If the frequency is not found
    }
}
