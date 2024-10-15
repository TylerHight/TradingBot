package monitoring;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import preprocessing.TimeSeriesAnalysis;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FourierPlotterTest {

    TimeSeriesAnalysis timeSeries;

    @BeforeEach
    public void setup() {
        timeSeries = new TimeSeriesAnalysis(3, 5);
    }

    @Test
    public void testSingleSineWave() throws InterruptedException {
        // Test Case 1: Test graph generation from a single sine wave input
        for (int i = 0; i < 1000; i++) {
            double time = i * 10; // 10 milliseconds intervals, sampling at 100 Hz
            double value = Math.sin(2 * Math.PI * 1 * (time / 1000)); // 1 Hz sine wave
            timeSeries.addPrice(value, (long) time);
        }

        // Get Fourier Transform result
        List<Double[]> result = timeSeries.calculateFourierTransform();

        // Check that the correct number of data points exist in the result (based on zero-padding)
        assertEquals(512, result.size(), "Expected 512 frequency bins after FFT");

        // Plot the Fourier transform to ensure it doesn't crash
        FourierPlotter.plotFourierTransform(result, "Single Sine Wave at 1 Hz");

        // Optionally keep the plot window open to inspect the graph
        Thread.sleep(10000);
    }

    @Test
    public void testEmptyInput() throws InterruptedException {
        // No input data added to TimeSeries
        List<Double[]> result = timeSeries.calculateFourierTransform();

        // Expect no data points in the output of the Fourier Transform
        assertEquals(0, result.size(), "Expected no frequency bins for empty time series");

        // Ensure the plot doesn't crash with empty data
        FourierPlotter.plotFourierTransform(result, "Empty List");

        // Optionally keep the plot window open to inspect the graph
        Thread.sleep(10000);
    }

    @Test
    public void testMultipleSineWaves() throws InterruptedException {
        // Test Case 2: Sum of Multiple Sine Waves (1 Hz + 3 Hz)
        for (int i = 0; i < 1000; i++) {
            double time = i * 10; // 10 milliseconds intervals
            double value = Math.sin(2 * Math.PI * 1 * (time / 1000)) + Math.sin(2 * Math.PI * 3 * (time / 1000)); // 1 Hz + 3 Hz sine wave
            timeSeries.addPrice(value, (long) time);
        }

        // Get Fourier Transform result
        List<Double[]> result = timeSeries.calculateFourierTransform();

        // Check that the correct number of data points exist in the result
        assertEquals(512, result.size(), "Expected 512 frequency bins after FFT");

        // Plot the Fourier transform
        FourierPlotter.plotFourierTransform(result, "Multiple Sine Waves at 1 Hz and 3 Hz");

        // Optionally keep the plot window open to inspect the graph
        Thread.sleep(10000);
    }
}
