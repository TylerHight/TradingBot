package preprocessing;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

class FourierTransformer {
    private static final Logger logger = LoggerFactory.getLogger(FourierTransformer.class);

    /**
     * Calculate the FFT of the input values. There is no magnitude normalization.
     *
     * @param values The list of values associated with the time series data.
     * @return The complex FFT result.
     */
    public Complex[] calculateFourierTransform(List<Double> values) {
        int n = values.size();
        if (n == 0) {
            logger.warn("No values provided for Fourier Transform calculation.");
            return new Complex[0];
        }

        // Find the next power of two for padding
        int nextPowerOfTwo = 1;
        while (nextPowerOfTwo < n) {
            nextPowerOfTwo <<= 1;
        }
        logger.info("Zero-padding input values to the next power of two: {}", nextPowerOfTwo);

        // Create a padded array for FFT (zero-padding)
        double[] paddedValues = new double[nextPowerOfTwo];
        for (int i = 0; i < n; i++) {
            paddedValues[i] = values.get(i);
        }

        // Calculate the FFT
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] fftResult = fft.transform(paddedValues, TransformType.FORWARD);

        logger.info("Fourier Transform calculation complete.");
        return fftResult;
    }

    /**
     * Calculate the sampling frequency from the timestamps.
     *
     * @param timestamps The list of timestamps for the time series data.
     * @return The sampling frequency in Hz.
     */
    public double calculateSamplingFrequency(List<Long> timestamps) {
        if (timestamps.size() < 2) {
            logger.error("Not enough timestamps to calculate sampling frequency.");
            return 0.0;
        }

        // Calculate sampling interval and frequency
        double samplingInterval = (timestamps.get(timestamps.size() - 1) - timestamps.get(0)) / (double) (timestamps.size() - 1);
        double samplingFrequency = 1000.0 / samplingInterval; // Convert to Hz
        logger.debug("Sampling interval: {} ms, Sampling frequency: {} Hz", samplingInterval, samplingFrequency);

        return samplingFrequency;
    }

    /**
     * Get frequency-magnitude pairs from the complex FFT result.
     *
     * @param fftResult         The complex FFT result.
     * @param samplingFrequency The sampling frequency of the original data.
     * @return A list of Fourier Transform magnitudes and frequencies.
     */
    public List<Double[]> getFrequencyMagnitudePairs(Complex[] fftResult, double samplingFrequency) {
        List<Double[]> frequencyMagnitudes = new ArrayList<>();
        int halfLength = fftResult.length / 2; // Only get the first half

        for (int k = 0; k < halfLength; k++) {
            double magnitude = fftResult[k].abs(); // Magnitude
            double frequency = k * samplingFrequency / fftResult.length; // Frequency for each bin
            frequencyMagnitudes.add(new Double[]{frequency, magnitude});
        }

        logger.info("Got {} frequency-magnitude pairs.", frequencyMagnitudes.size());
        return frequencyMagnitudes;
    }
}