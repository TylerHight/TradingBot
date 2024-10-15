/*
package preprocessing;

import monitoring.MultiTimeSeriesPlotter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

NOT WORKING CORRECTLY - source: https://gist.github.com/tartakynov/83f3cd8f44208a1856ce

public class FourierExtrapolation {

    public static List<Double> extrapolateUsingSignificantHarmonics(List<Double> prices, List<Long> timestamps, int numPredictions) {
        int nHarmonics = 10;  // Number of harmonics to consider
        List<Double> extrapolated = new ArrayList<>();

        // Create FourierTransform object to calculate FFT
        FourierTransformer transformer = new FourierTransformer();

        // Step 1: Calculate FFT using the FourierTransform class
        List<Double[]> frequencyMagnitudes = transformer.calculateFourierTransform(prices, timestamps);

        if (frequencyMagnitudes.isEmpty()) {
            return extrapolated; // Return empty if the FFT calculation failed
        }

        // Step 2: Sort frequencies by magnitude
        frequencyMagnitudes.sort(new Comparator<Double[]>() {
            @Override
            public int compare(Double[] a, Double[] b) {
                return Double.compare(Math.abs(b[1]), Math.abs(a[1])); // Sort by magnitude descending
            }
        });

        // Step 3: Extrapolate future values
        for (int i = 0; i < prices.size() + numPredictions; i++) {
            double value = 0;

            // Sum contributions from the most significant harmonics
            for (int j = 0; j < Math.min(frequencyMagnitudes.size(), nHarmonics); j++) {
                double frequency = frequencyMagnitudes.get(j)[0];
                double magnitude = frequencyMagnitudes.get(j)[1];

                // Restore the signal using cosine (Phase is set to 0 for simplicity)
                value += (magnitude / prices.size()) * Math.cos(2 * Math.PI * frequency * i);
            }

            extrapolated.add(value);
        }

        return extrapolated;
    }

    public static void main(String[] args) {
        // Input price data
        List<Double> prices = Arrays.asList(
                63823.09, 63823.12, 63891.73, 63812.9, 63856.84, 63862.3, 63862.29, 63862.29, 63862.29, 63848.53,
                63849.15, 63849.48, 63850.2, 63850.48, 63805.04, 63805.07, 63805.69, 63806.06, 63806.06, 63874.25,
                63875.63, 63875.63, 63875.63, 63784.67, 63856.76, 63822.92, 63825.17, 63825.17, 63825.17, 63825.17,
                63839.63, 63839.91, 63839.91, 63839.91, 63913.21, 63990.99, 64057.31, 63987.36, 64080.42, 64136.63,
                64192.59, 64281.01, 64208.99, 64189.27, 64200.0, 64291.0, 64322.53, 64322.61, 64148.27, 64247.91,
                64358.97, 64363.48, 64417.97, 64467.59, 64499.0, 64420.42, 64248.88, 64287.49, 64349.95, 64327.47,
                64297.74, 64362.5, 64248.84, 64248.84, 64208.12, 64285.12, 64285.12, 64298.29, 64248.25, 64202.1,
                64147.23, 64146.72, 64147.35, 64195.75, 64262.6, 64249.99, 64204.66
        );

        // Input timestamps data (in milliseconds)
        List<Long> timestamps = new ArrayList<>();
        // Start time (in milliseconds)
        long startTime = 1695567240000L; // 2024-09-24 14:14:00 in milliseconds
        // Increment for each minute (60000 ms = 1 minute)
        long increment = 60000L;

        // Add timestamps incrementing by 1 minute each time
        for (int i = 0; i < prices.size(); i++) {
            timestamps.add(startTime + (i * increment));
        }

        // Create FourierTransform object and calculate the Fourier Transform
        FourierTransformer transformer = new FourierTransformer();
        List<Double[]> frequencyMagnitudes = transformer.calculateFourierTransform(prices, timestamps);

        // Define the number of predictions to make
        int numPredictions = 10;

        // Extrapolate using the significant harmonics
        List<Double> extrapolatedValues = extrapolateUsingSignificantHarmonics(prices, timestamps, numPredictions);

        // Print extrapolated values for inspection
        for (Double value : extrapolatedValues) {
            System.out.println(value);
        }

        // Prepare data for plotting
        List<List<Double>> seriesData = new ArrayList<>();
        seriesData.add(prices);            // Original prices
        seriesData.add(extrapolatedValues); // Extrapolated values

        List<String> seriesNames = List.of("Original Prices", "Extrapolated Values");

        // Create and display the plot
        MultiTimeSeriesPlotter example = new MultiTimeSeriesPlotter("Multi Time Series Chart", seriesData, seriesNames);
        example.setSize(800, 600);
        example.setLocationRelativeTo(null);
        example.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        example.setVisible(true);
    }

}
*/
