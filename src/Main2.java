import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.complex.Complex;

class Main2 {
    private static final double DELTA = 1e-10;

    public static void main(String[] args) {
        testEmptyInput();
        testSingleValue();
        testConstantInput();
        testSimpleSineWave();
        testSamplingFrequency();
        testCalculateFourierTransform();
        testCompositeSineWaveFFT();
        testSingleFrequencySineWaveFFT();
        System.out.println("All tests passed successfully!");
    }

    private static void testEmptyInput() {
        FourierTransformer transformer = new FourierTransformer();
        List<Double> emptyValues = Arrays.asList();

        Complex[] result = transformer.calculateFourierTransform(emptyValues);
        assert result.length == 0 : "Empty input should return empty array";
    }

    private static void testSingleValue() {
        FourierTransformer transformer = new FourierTransformer();
        List<Double> values = Arrays.asList(5.0);

        Complex[] result = transformer.calculateFourierTransform(values);
        assert Math.abs(result[0].getReal() - 5.0) < DELTA : "FFT of single value should be the value itself";
    }

    private static void testConstantInput() {
        FourierTransformer transformer = new FourierTransformer();
        List<Double> values = Arrays.asList(3.0, 3.0, 3.0, 3.0);

        Complex[] result = transformer.calculateFourierTransform(values);
        assert Math.abs(result[0].getReal() - 12.0) < DELTA : "DC component should be 4 times the constant value";
    }

    private static void testSimpleSineWave() {
        FourierTransformer transformer = new FourierTransformer();
        List<Double> values = Arrays.asList(0.0, 1.0, 0.0, -1.0);
        List<Long> timestamps = Arrays.asList(0L, 1L, 2L, 3L);
        Complex[] result = transformer.calculateFourierTransform(values);
        double samplingFrequency = transformer.calculateSamplingFrequency(timestamps);
        List<Double[]> freqMagPairs = transformer.getFrequencyMagnitudePairs(result, samplingFrequency);
        assert freqMagPairs.get(1)[1] > freqMagPairs.get(0)[1] && freqMagPairs.get(1)[1] > freqMagPairs.get(2)[1];
    }

    private static void testSamplingFrequency() {
        FourierTransformer transformer = new FourierTransformer();
        List<Long> timestamps = Arrays.asList(0L, 100L, 200L, 300L);
        double samplingFrequency = transformer.calculateSamplingFrequency(timestamps);
        assert Math.abs(samplingFrequency - 10.0) < DELTA : "Sampling frequency should be 10 Hz for 100ms intervals";
    }

    private static void testCalculateFourierTransform() {
        FourierTransformer transformer = new FourierTransformer();
        List<Double> values = new ArrayList<>();
        int numSamples = 1000;
        double frequency = 1.0; // 1 Hz
        double amplitude = 2.0;
        // Generate 1 Hz sine wave
        for (int i = 0; i < numSamples; i++) {
            double time = i / (double) numSamples;
            double value = amplitude * Math.sin(2 * Math.PI * frequency * time);
            values.add(value);
        }
        Complex[] fftResult = transformer.calculateFourierTransform(values);
        int expectedPeakIndex = 1;
        double expectedImaginary = -1008.0;
        Complex resultUnrounded = fftResult[expectedPeakIndex];
        long resultRoundedComplex = Math.round(resultUnrounded.getImaginary());
        assert resultRoundedComplex == expectedImaginary;
    }
    private static void testCompositeSineWaveFFT() {
        FourierTransformer transformer = new FourierTransformer();
        List<Double> values = new ArrayList<>();
        List<Long> timestamps = new ArrayList<>();
        int numSamples = 1000;
        double samplingInterval = 10; // 10 milliseconds
        for (int i = 0; i < numSamples; i++) {
            double time = i * samplingInterval;
            double value = Math.sin(2 * Math.PI * 1 * (time / 1000)) + Math.sin(2 * Math.PI * 3 * (time / 1000));
            values.add(value);
            timestamps.add((long) time);
        }
        Complex[] fftResult = transformer.calculateFourierTransform(values);
        double samplingFrequency = transformer.calculateSamplingFrequency(timestamps);
        List<Double[]> freqMagPairs = transformer.getFrequencyMagnitudePairs(fftResult, samplingFrequency);
        // Find peaks at 1 Hz and 3 Hz
        Double[] peak1Hz = null;
        Double[] peak3Hz = null;
        for (Double[] pair : freqMagPairs) {
            if (Math.abs(pair[0] - 1.0) < 0.1 && (peak1Hz == null || pair[1] > peak1Hz[1])) {
                peak1Hz = pair;
            }
            if (Math.abs(pair[0] - 3.0) < 0.1 && (peak3Hz == null || pair[1] > peak3Hz[1])) {
                peak3Hz = pair;
            }
        }
        double magnitude1Hz = 0;
        double magnitude3Hz = 0;
        if (peak1Hz != null && peak3Hz != null) {
            magnitude1Hz = Math.round(peak1Hz[1]);
            magnitude3Hz = Math.round(peak3Hz[1]);
        }
        assert magnitude1Hz == 469.0 && magnitude3Hz == 442.0;
    }

    private static void testSingleFrequencySineWaveFFT() {
        FourierTransformer transformer = new FourierTransformer();
        List<Double> values = new ArrayList<>();
        List<Long> timestamps = new ArrayList<>();
        int numSamples = 1000;
        double samplingInterval = 10; // 10 milliseconds
        // Generate 1 Hz sine wave
        for (int i = 0; i < numSamples; i++) {
            double time = i * samplingInterval;
            double value = Math.sin(2 * Math.PI * 1 * (time / 1000)); // 1 Hz sine wave
            values.add(value);
            timestamps.add((long) time);
        }
        Complex[] fftResult = transformer.calculateFourierTransform(values);
        double samplingFrequency = transformer.calculateSamplingFrequency(timestamps);
        List<Double[]> freqMagPairs = transformer.getFrequencyMagnitudePairs(fftResult, samplingFrequency);
        // Find peak at 1 Hz
        Double[] peak1Hz = null;
        for (Double[] pair : freqMagPairs) {
            if (Math.abs(pair[0] - 1.0) < 0.1 && (peak1Hz == null || pair[1] > peak1Hz[1])) {
                peak1Hz = pair;
            }
        }
        double magnitude1Hz = Math.round(peak1Hz[1]);
        // Assert that the magnitude is close to the expected value
        assert magnitude1Hz == 461.0;
    }

}