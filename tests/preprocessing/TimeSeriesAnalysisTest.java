package preprocessing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class TimeSeriesAnalysisTest {

    private TimeSeriesAnalysis tsa;

    @BeforeEach
    void setUp() {
        tsa = new TimeSeriesAnalysis(3, 3);
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
        // This test depends on the implementation of FourierTransform class
        // You might need to mock this class or provide a simple implementation for testing
        tsa.addPrice(10, 1000);
        tsa.addPrice(20, 2000);
        tsa.addPrice(30, 3000);

        List<Double[]> result = tsa.calculateFourierTransform();
        assertNotNull(result);
        // Add more specific assertions based on expected Fourier Transform results
    }
}