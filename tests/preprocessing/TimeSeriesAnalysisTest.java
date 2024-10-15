package preprocessing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class TimeSeriesAnalysisTest {

    private TimeSeriesAnalysis tsa;
    private FourierTransformer fourierTransformer;

    @BeforeEach
    void setUp() {
        tsa = new TimeSeriesAnalysis(3, 3);
        fourierTransformer = new FourierTransformer();
    }

    @Test
    void testSMACalculation() {
        tsa.addPrice(10, 1000);
        assertNull(tsa.getLastSMA());

        tsa.addPrice(20, 2000);
        assertNull(tsa.getLastSMA());

        tsa.addPrice(30, 3000);
        assertEquals(20, tsa.getLastSMA());

        tsa.addPrice(40, 4000);
        assertEquals(30, tsa.getLastSMA());

        tsa.addPrice(50, 5000);
        assertEquals(40, tsa.getLastSMA());
    }

    @Test
    void testEMACalculation() {
        tsa.addPrice(10, 1000);
        assertNull(tsa.getLastEMA());

        tsa.addPrice(20, 2000);
        assertNull(tsa.getLastEMA());

        tsa.addPrice(30, 3000);
        assertEquals(20, tsa.getLastEMA());

        tsa.addPrice(40, 4000);
        assertEquals(30, tsa.getLastEMA());

        tsa.addPrice(50, 5000);
        assertEquals(40, tsa.getLastEMA());
    }

    @Test
    void testInitialDataLargerThanWindow() {
        List<Double> initialValues = Arrays.asList(10.0, 20.0, 30.0, 40.0, 50.0);
        List<Long> initialTimestamps = Arrays.asList(1000L, 2000L, 3000L, 4000L, 5000L);

        TimeSeriesAnalysis tsaWithInitialData = new TimeSeriesAnalysis(3, 3, initialValues, initialTimestamps);

        assertEquals(40, tsaWithInitialData.getLastSMA());
        assertEquals(40, tsaWithInitialData.getLastEMA());

        tsaWithInitialData.addPrice(60, 6000);
        assertEquals(50, tsaWithInitialData.getLastSMA());
        assertEquals(50, tsaWithInitialData.getLastEMA());
    }

    @Test
    void testAddPrice() {
        tsa.addPrice(10, 1000);
        assertEquals(1, tsa.getValues().size());
        assertEquals(1, tsa.getTimestamps().size());
        assertEquals(10, tsa.getValues().get(0));
        assertEquals(1000, tsa.getTimestamps().get(0));
    }

    @Test
    void testFourierTransform() {
        List<Double> values = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        List<Long> timestamps = IntStream.range(0, values.size())
                .mapToObj(i -> (long) i * 1000)
                .collect(Collectors.toList());

        for (int i = 0; i < values.size(); i++) {
            tsa.addPrice(values.get(i), timestamps.get(i));
        }

        List<Double[]> result = tsa.calculateFourierTransform();

        assertNotNull(result);
        assertEquals(4, result.size()); // Half of the input size due to symmetry

        // Check if frequencies are correctly calculated
        assertEquals(0.0, result.get(0)[0], 1e-6);
        assertEquals(0.125, result.get(1)[0], 1e-6);
        assertEquals(0.25, result.get(2)[0], 1e-6);
        assertEquals(0.375, result.get(3)[0], 1e-6);

        // Check if magnitudes are non-negative
        for (Double[] freqMag : result) {
            assertTrue(freqMag[1] >= 0);
        }
    }
}