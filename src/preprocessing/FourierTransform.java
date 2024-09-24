package preprocessing;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.List;

public class FourierTransform {

    /**
     * Calculate the FFT of the input values. Note that there is no magnitude normalization,
     * but mirrored frequencies are removed.
     *
     * @param values The list of values representing the time series data.
     * @param timestamps The list of timestamps associated with the time series data.
     * @return A list of Fourier Transform magnitudes and their corresponding frequencies.
     */
    public List<Double[]> calculateFourierTransform(List<Double> values, List<Long> timestamps) {
        int n = values.size();
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
            paddedPrices[i] = values.get(i);
        }

        // Check if there are enough timestamps to calculate the sampling interval
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
}
