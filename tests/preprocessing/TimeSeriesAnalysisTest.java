package preprocessing;

import monitoring.FourierPlotter;
import monitoring.FourierPlotterTest;

import java.util.List;

public class TimeSeriesAnalysisTest {
    public static void main(String[] args) {
        TimeSeriesAnalysis timeSeriesAnalysis = new TimeSeriesAnalysis();

        testCalculateMovingAverage(timeSeriesAnalysis);
        testCalculateFourierTransform();

        System.out.println("All tests passed.");
    }

    public static void testCalculateMovingAverage(TimeSeriesAnalysis timeSeriesAnalysis) {
        timeSeriesAnalysis.addPrice(100.0, System.currentTimeMillis());
        timeSeriesAnalysis.addPrice(102.0, System.currentTimeMillis() + 1000);
        timeSeriesAnalysis.addPrice(104.0, System.currentTimeMillis() + 2000);
        timeSeriesAnalysis.addPrice(106.0, System.currentTimeMillis() + 3000);

        // Calculate the moving average for a period of 2
        List<Double> movingAverages = timeSeriesAnalysis.calculateMovingAverage(2);

        assert movingAverages.size() == 3 : "Expected 3 moving averages, but got " + movingAverages.size();
        assert movingAverages.get(0) == 101.0 : "Expected moving average 101.0, but got " + movingAverages.get(0);
        assert movingAverages.get(1) == 103.0 : "Expected moving average 103.0, but got " + movingAverages.get(1);
        assert movingAverages.get(2) == 105.0 : "Expected moving average 105.0, but got " + movingAverages.get(2);
    }

    private static void testCalculateFourierTransform() {
        FourierPlotter plotter = new FourierPlotter(); // Initialize the plotter for plotting the results

        // Test Case 1: Input is a single sine wave at 1 Hz
        TimeSeriesAnalysis ts1 = new TimeSeriesAnalysis();
        for (int i = 0; i < 1000; i++) {
            double time = i * 10; // 10 milliseconds intervals, sampling at 100 Hz (ensure Nyquist rate is met)
            double value = Math.sin(2 * Math.PI * 1 * (time / 1000)); // 1 Hz sine wave
            ts1.addPrice(value, (long) time);
        }
        List<Double[]> result1 = ts1.calculateFourierTransform();
        FourierPlotter.plotFourierTransform(result1, "1 Hz Sine Wave");
        // Check magnitude at 1 Hz
        double magnitudeAt1Hz = getMagnitudeAtFrequency(result1, 1.0);
        assert magnitudeAt1Hz == 461.0;

        // Test Case 2: No Prices should return an empty list
        TimeSeriesAnalysis timeSeries2 = new TimeSeriesAnalysis();
        List<Double[]> result2 = timeSeries2.calculateFourierTransform();
        assert result2.isEmpty();

        // Test Case 3: Not enough timestamps to calculate FFT
        TimeSeriesAnalysis timeSeries3 = new TimeSeriesAnalysis();
        timeSeries3.addPrice(100.0, System.currentTimeMillis()); // Add a single price and timestamp
        List<Double[]> result3 = timeSeries3.calculateFourierTransform();
        assert result3.isEmpty();

        // Test Case 4: Input is a sum of sine waves at 1 Hz and 3 Hz
        TimeSeriesAnalysis ts4 = new TimeSeriesAnalysis();
        for (int i = 0; i < 1000; i++) {
            double time = i * 10; // 10 milliseconds intervals, sampling at 100 Hz (ensure Nyquist rate is met)
            double value = Math.sin(2 * Math.PI * 1 * (time / 1000)) + Math.sin(2 * Math.PI * 3 * (time / 1000)); // 1 Hz + 3 Hz sine wave
            ts4.addPrice(value, (long) time);
        }
        List<Double[]> result4 = ts4.calculateFourierTransform();
        double magnitudeAt1HzMultiple = getMagnitudeAtFrequency(result4, 1.0);
        assert magnitudeAt1HzMultiple == 469.0;

        // Test Case 5: Another check for sum of sine waves
        TimeSeriesAnalysis ts5 = new TimeSeriesAnalysis();
        for (int i = 0; i < 1000; i++) {
            double time = i * 10; // 10 milliseconds intervals, sampling at 100 Hz (ensure Nyquist rate is met)
            double value = Math.sin(2 * Math.PI * 1 * (time / 1000)) + Math.sin(2 * Math.PI * 3 * (time / 1000)); // 1 Hz + 3 Hz sine wave
            ts5.addPrice(value, (long) time);
        }
        List<Double[]> result5 = ts5.calculateFourierTransform();
        // Get magnitude at 3 Hz
        double magnitudeAt3Hz = getMagnitudeAtFrequency(result5, 3.0);
        assert magnitudeAt3Hz == 181;

        // Print success message for all test cases
        System.out.println("All test cases passed!");
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
