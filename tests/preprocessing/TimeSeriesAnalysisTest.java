package preprocessing;

import monitoring.FourierPlotter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeSeriesAnalysisTest {

    @Test
    public void testCalculateMovingAverage() {
        TimeSeriesAnalysis timeSeriesAnalysis = new TimeSeriesAnalysis();
        timeSeriesAnalysis.addPrice(100.0, System.currentTimeMillis());
        timeSeriesAnalysis.addPrice(102.0, System.currentTimeMillis() + 1000);
        timeSeriesAnalysis.addPrice(104.0, System.currentTimeMillis() + 2000);
        timeSeriesAnalysis.addPrice(106.0, System.currentTimeMillis() + 3000);

        // Calculate the moving average for a period of 2
        List<Double> movingAverages = timeSeriesAnalysis.calculateMovingAverage(2);

        assertEquals(3, movingAverages.size(), "Expected 3 moving averages.");
        assertEquals(101.0, movingAverages.get(0), 0.001, "Expected moving average 101.0.");
        assertEquals(103.0, movingAverages.get(1), 0.001, "Expected moving average 103.0.");
        assertEquals(105.0, movingAverages.get(2), 0.001, "Expected moving average 105.0.");
    }

    @Test
    public void testCalculateFourierTransform() {
        FourierPlotter plotter = new FourierPlotter(); // Initialize the plotter for plotting the results

        // Test Case 1: Input is a single sine wave at 1 Hz
        TimeSeriesAnalysis ts1 = new TimeSeriesAnalysis();
        for (int i = 0; i < 1000; i++) {
            double time = i * 10; // 10 milliseconds intervals, sampling at 100 Hz
            double value = Math.sin(2 * Math.PI * 1 * (time / 1000)); // 1 Hz sine wave
            ts1.addPrice(value, (long) time);
        }
        FourierTransform fourierTransform = new FourierTransform();
        List<Double[]> result1 = fourierTransform.calculateFourierTransform(ts1.getValues(), ts1.getTimestamps());
        FourierPlotter.plotFourierTransform(result1, "1 Hz Sine Wave");

        // Check magnitude at 1 Hz
        double magnitudeAt1Hz = getMagnitudeAtFrequency(result1, 1.0);
        assertEquals(461.0, magnitudeAt1Hz, 5.0, "Magnitude at 1 Hz is not as expected.");

        // Test Case 2: No prices should return an empty list
        TimeSeriesAnalysis timeSeries2 = new TimeSeriesAnalysis();
        List<Double[]> result2 = fourierTransform.calculateFourierTransform(timeSeries2.getValues(), timeSeries2.getTimestamps());
        assertTrue(result2.isEmpty(), "Expected an empty Fourier transform result for no prices.");

        // Test Case 3: Not enough timestamps to calculate FFT
        TimeSeriesAnalysis timeSeries3 = new TimeSeriesAnalysis();
        timeSeries3.addPrice(100.0, System.currentTimeMillis()); // Add a single price and timestamp
        List<Double[]> result3 = fourierTransform.calculateFourierTransform(timeSeries3.getValues(), timeSeries3.getTimestamps());
        assertTrue(result3.isEmpty(), "Expected an empty Fourier transform result for insufficient timestamps.");

        // Test Case 4: Input is a sum of sine waves at 1 Hz and 3 Hz
        TimeSeriesAnalysis ts4 = new TimeSeriesAnalysis();
        for (int i = 0; i < 1000; i++) {
            double time = i * 10; // 10 milliseconds intervals, sampling at 100 Hz
            double value = Math.sin(2 * Math.PI * 1 * (time / 1000)) + Math.sin(2 * Math.PI * 3 * (time / 1000)); // 1 Hz + 3 Hz sine wave
            ts4.addPrice(value, (long) time);
        }
        List<Double[]> result4 = fourierTransform.calculateFourierTransform(ts4.getValues(), ts4.getTimestamps());
        double magnitudeAt1HzMultiple = getMagnitudeAtFrequency(result4, 1.0);
        assertEquals(469.0, magnitudeAt1HzMultiple, 5.0, "Magnitude at 1 Hz for multiple sine waves is not as expected.");

        // Test Case 5: Check for sum of sine waves at 3 Hz
        List<Double[]> result5 = fourierTransform.calculateFourierTransform(ts4.getValues(), ts4.getTimestamps());
        double magnitudeAt3Hz = getMagnitudeAtFrequency(result5, 3.0);
        assertEquals(181, magnitudeAt3Hz, 5.0, "Magnitude at 3 Hz is not as expected.");
    }

    // Helper method that gets the magnitude at a specific frequency (with tolerance)
    private static double getMagnitudeAtFrequency(List<Double[]> result, double frequency) {
        for (Double[] pair : result) {
            double freq = pair[0];
            double magnitude = pair[1];
            if (Math.abs(freq - frequency) < 0.1) { // Tolerance for frequency
                return Math.round(magnitude); // Return the magnitude if a frequency falls within the tolerance
            }
        }
        return 0.0; // If the frequency is not found
    }
}
