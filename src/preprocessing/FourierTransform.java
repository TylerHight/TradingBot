package preprocessing;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FourierTransform {

    // Initialize the logger
    private static final Logger logger = LoggerFactory.getLogger(FourierTransform.class);

    /**
     * Calculate the FFT of the input values. Note that there is no magnitude normalization,
     * but mirrored frequencies are removed.
     *
     * @param values     The list of values representing the time series data.
     * @param timestamps The list of timestamps associated with the time series data.
     * @return A list of Fourier Transform magnitudes and their corresponding frequencies.
     */
    public List<Double[]> calculateFourierTransform(List<Double> values, List<Long> timestamps) {
        int n = values.size();
        if (n == 0) {
            logger.warn("No values provided for Fourier Transform calculation.");
            return new ArrayList<>(); // Return empty list if no prices are available
        }

        // Find the next power of two for padding
        int nextPowerOfTwo = 1;
        while (nextPowerOfTwo < n) {
            nextPowerOfTwo <<= 1;
        }

        logger.info("Zero-padding input values to the next power of two: {}", nextPowerOfTwo);

        // Create a padded array for FFT (zero-padding)
        double[] paddedPrices = new double[nextPowerOfTwo];
        for (int i = 0; i < n; i++) {
            paddedPrices[i] = values.get(i);
        }

        // Check if there are enough timestamps to calculate the sampling interval
        if (timestamps.size() < 2) {
            logger.error("Insufficient timestamps to calculate Fourier transform.");
            return new ArrayList<>(); // Return an empty list indicating a failure
        }

        // Calculate the FFT
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] fftResult = fft.transform(paddedPrices, TransformType.FORWARD);

        // Calculate sampling interval and frequency
        double samplingInterval = timestamps.get(1) - timestamps.get(0); // Interval in milliseconds
        double samplingFrequency = 1000.0 / samplingInterval; // Convert to Hz

        logger.debug("Sampling interval: {} ms, Sampling frequency: {} Hz", samplingInterval, samplingFrequency);

        // Calculate magnitudes and corresponding frequencies (only for the first half)
        List<Double[]> frequencyMagnitudes = new ArrayList<>();
        int halfLength = fftResult.length / 2; // Only process the first half

        for (int k = 0; k < halfLength; k++) {
            double magnitude = fftResult[k].abs(); // Magnitude
            double frequency = k * samplingFrequency / nextPowerOfTwo; // Frequency for each bin
            frequencyMagnitudes.add(new Double[]{frequency, magnitude});
        }

        logger.info("Fourier Transform calculation completed successfully with {} frequency-magnitude pairs.", frequencyMagnitudes.size());
        return frequencyMagnitudes; // Return list of frequency-magnitude pairs
    }

    // Used for quick testing/scratch
    public static void main(String[] args) {
        // Initialize the FourierTransform instance
        FourierTransform fourierTransform = new FourierTransform();

        // Prices as a list of Double values
        List<Double> prices = Arrays.asList(
                63823.09, 63823.12, 63891.73, 63812.9, 63856.84, 63862.3, 63862.29,
                63862.29, 63862.29, 63848.53, 63849.15, 63849.48, 63850.2, 63850.48,
                63805.04, 63805.07, 63805.69, 63806.06, 63806.06, 63874.25, 63875.63,
                63875.63, 63875.63, 63784.67, 63856.76, 63822.92, 63825.17, 63825.17,
                63825.17, 63825.17, 63839.63, 63839.91, 63839.91, 63839.91, 63913.21,
                63990.99, 64057.31, 63987.36, 64080.42, 64136.63, 64192.59, 64281.01,
                64208.99, 64189.27, 64200.0, 64291.0, 64322.53, 64322.61, 64148.27,
                64247.91, 64358.97, 64363.48, 64417.97, 64467.59, 64499.0, 64420.42,
                64248.88, 64287.49, 64349.95, 64327.47, 64297.74, 64362.5, 64248.84,
                64248.84, 64208.12, 64285.12, 64285.12, 64298.29, 64248.25, 64202.1,
                64147.23, 64146.72, 64147.35, 64195.75, 64262.6, 64249.99, 64204.66
        );

        // Timestamps in milliseconds relative to a base timestamp
        List<Long> timestamps = Arrays.asList(
                1414000L, 1415000L, 1416000L, 1417000L, 1418000L, 1419000L, 1420000L,
                1421000L, 1422000L, 1423000L, 1424000L, 1425000L, 1426000L, 1427000L,
                1428000L, 1429000L, 1430000L, 1431000L, 1432000L, 1433000L, 1434000L,
                1435000L, 1436000L, 1437000L, 1438000L, 1439000L, 1440000L, 1441000L,
                1442000L, 1443000L, 1444000L, 1445000L, 1446000L, 1447000L, 1448000L,
                1449000L, 1450000L, 1451000L, 1452000L, 1453000L, 1454000L, 1455000L,
                1456000L, 1457000L, 1458000L, 1459000L, 1460000L, 1461000L, 1462000L,
                1463000L, 1464000L, 1465000L, 1466000L, 1467000L, 1468000L, 1469000L,
                1470000L, 1471000L, 1472000L, 1473000L, 1474000L, 1475000L, 1476000L,
                1477000L, 1478000L, 1479000L, 1480000L, 1481000L, 1482000L, 1483000L,
                1484000L, 1485000L, 1486000L, 1487000L, 1488000L, 1489000L
        );

        // Calculate the Fourier Transform
        List<Double[]> result = fourierTransform.calculateFourierTransform(prices, timestamps);

        // Print the results
        for (Double[] entry : result) {
            System.out.println("Frequency: " + entry[0] + ", Magnitude: " + entry[1]);
        }
    }

}
