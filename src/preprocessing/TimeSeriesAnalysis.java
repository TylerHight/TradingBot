package preprocessing;

import java.util.ArrayList;
import java.util.List;

public class TimeSeriesAnalysis {
    private List<Double> values;
    private List<Long> timestamps;
    private FourierTransform fourierTransform;
    private Double lastSMA;
    private Double lastEMA;
    private int smaPeriod;
    private int emaPeriod;
    private double smaSum;

    public TimeSeriesAnalysis(int smaPeriod, int emaPeriod) {
        this(smaPeriod, emaPeriod, new ArrayList<>(), new ArrayList<>());
    }

    public TimeSeriesAnalysis(int smaPeriod, int emaPeriod, List<Double> initialValues, List<Long> initialTimestamps) {
        if (initialValues.size() != initialTimestamps.size()) {
            throw new IllegalArgumentException("Values and timestamps must have the same size");
        }
        this.values = new ArrayList<>(initialValues);
        this.timestamps = new ArrayList<>(initialTimestamps);
        this.fourierTransform = new FourierTransform();
        this.smaPeriod = smaPeriod;
        this.emaPeriod = emaPeriod;
        this.lastSMA = null;
        this.lastEMA = null;
        this.smaSum = 0.0;

        initializeIndicators();
    }

    private void initializeIndicators() {
        if (values.isEmpty()) return;

        // Initialize SMA
        int smaStartIndex = Math.max(0, values.size() - smaPeriod);
        for (int i = smaStartIndex; i < values.size(); i++) {
            smaSum += values.get(i);
        }
        if (values.size() >= smaPeriod) {
            lastSMA = smaSum / smaPeriod;
        }

        // Initialize EMA
        if (values.size() >= emaPeriod) {
            double sum = 0.0;
            for (int i = values.size() - emaPeriod; i < values.size(); i++) {
                sum += values.get(i);
            }
            lastEMA = sum / emaPeriod;
        }
    }

    public void addPrice(double price, long timestamp) {
        values.add(price);
        timestamps.add(timestamp);
        updateSMA(price);
        updateEMA(price);
    }

    private void updateSMA(double newPrice) {
        if (values.size() <= smaPeriod) {
            smaSum += newPrice;
            if (values.size() == smaPeriod) {
                lastSMA = smaSum / smaPeriod;
            } else {
                lastSMA = null;
            }
        } else {
            smaSum = smaSum - values.get(values.size() - smaPeriod - 1) + newPrice;
            lastSMA = smaSum / smaPeriod;
        }
    }

    private void updateEMA(double newPrice) {
        if (values.size() < emaPeriod) {
            lastEMA = null;
            return;
        }

        if (lastEMA == null) {
            double sum = 0.0;
            for (int i = values.size() - emaPeriod; i < values.size(); i++) {
                sum += values.get(i);
            }
            lastEMA = sum / emaPeriod;
        } else {
            double multiplier = 2.0 / (emaPeriod + 1);
            lastEMA = ((newPrice - lastEMA) * multiplier) + lastEMA;
        }
    }

    public Double getLastSMA() {
        return lastSMA;
    }

    public Double getLastEMA() {
        return lastEMA;
    }

    public List<Double[]> calculateFourierTransform() {
        return fourierTransform.calculateFourierTransform(values, timestamps);
    }

    public List<Double> getValues() {
        return new ArrayList<>(values);
    }

    public List<Long> getTimestamps() {
        return new ArrayList<>(timestamps);
    }
}