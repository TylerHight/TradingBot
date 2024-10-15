import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

class Main {
    public static void main(String[] args) {
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
}