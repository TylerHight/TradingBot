package strategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class TradeDecisionManager {
    private final double confidenceThreshold;
    private final Map<String, Indicator<?>> indicators;
    private final HistoricalDataManager historicalDataManager;
    private final MLModelClient mlModelClient;
    private final WeightAdjustmentClient weightAdjustmentClient;

    public TradeDecisionManager(double confidenceThreshold,
                                HistoricalDataManager historicalDataManager,
                                MLModelClient mlModelClient,
                                WeightAdjustmentClient weightAdjustmentClient) {
        this.confidenceThreshold = confidenceThreshold;
        this.indicators = new ConcurrentHashMap<>();
        this.historicalDataManager = historicalDataManager;
        this.mlModelClient = mlModelClient;
        this.weightAdjustmentClient = weightAdjustmentClient;
    }

    public TradeDecision evaluateTrade(Map<String, Object> signalData) {
        try {
            historicalDataManager.updateHistoricalData(signalData);
            double confidence = calculateConfidence(signalData);
            boolean shouldTrade = confidence >= confidenceThreshold;

            TradeDecision decision = new TradeDecision(shouldTrade, confidence,
                    (String)signalData.get("asset"),
                    (String)signalData.get("action"));

            // Asynchronously update weights based on this decision
            weightAdjustmentClient.submitTradeResult(decision);

            return decision;
        } catch (Exception e) {
            System.err.println("Error evaluating trade: " + e.getMessage());
            return new TradeDecision(false, 0,
                    (String)signalData.get("asset"),
                    (String)signalData.get("action"));
        }
    }

    private double calculateConfidence(Map<String, Object> signalData) {
        double totalWeight = 0;
        double weightedSum = 0;

        Map<String, Double> currentWeights = weightAdjustmentClient.getCurrentWeights();

        for (Map.Entry<String, Indicator<?>> entry : indicators.entrySet()) {
            String key = entry.getKey();
            Indicator<?> indicator = entry.getValue();

            Object currentValue = signalData.get(key);
            List<Object> history = historicalDataManager.getHistory(key);

            double normalizedValue = indicator.normalize(currentValue);

            // Use ML model for confidence calculation
            double confidence = mlModelClient.predictConfidence(key, normalizedValue, history);

            double weight = currentWeights.getOrDefault(key, indicator.getWeight());
            weightedSum += confidence * weight;
            totalWeight += weight;
        }

        return totalWeight > 0 ? weightedSum / totalWeight : 0;
    }

    public <T> void addIndicator(String name, double initialWeight, Class<T> type,
                                 Function<T, Double> normalizer) {
        indicators.put(name, new Indicator<T>(initialWeight, type, normalizer));
        weightAdjustmentClient.registerNewIndicator(name, initialWeight);
    }

    public void removeIndicator(String name) {
        indicators.remove(name);
        weightAdjustmentClient.removeIndicator(name);
    }

    private static class Indicator<T> {
        private final Class<T> type;
        private final Function<T, Double> normalizer;
        private double weight;

        public Indicator(double weight, Class<T> type, Function<T, Double> normalizer) {
            this.weight = weight;
            this.type = type;
            this.normalizer = normalizer;
        }

        public double normalize(Object value) {
            if (!type.isInstance(value)) {
                throw new IllegalArgumentException("Value is not of expected type: " + type.getName());
            }
            return normalizer.apply(type.cast(value));
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }
}

class HistoricalDataManager {
    private final Map<String, Deque<Object>> historicalData;
    private final int maxHistorySize;

    public HistoricalDataManager(int maxHistorySize) {
        this.historicalData = new ConcurrentHashMap<>();
        this.maxHistorySize = maxHistorySize;
    }

    public void updateHistoricalData(Map<String, Object> signalData) {
        for (Map.Entry<String, Object> entry : signalData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            historicalData.computeIfAbsent(key, k -> new LinkedList<>()).addFirst(value);

            Deque<Object> history = historicalData.get(key);
            while (history.size() > maxHistorySize) {
                history.removeLast();
            }
        }
    }

    public List<Object> getHistory(String key) {
        return new ArrayList<>(historicalData.getOrDefault(key, new LinkedList<>()));
    }
}

interface MLModelClient {
    double predictConfidence(String indicatorName, double normalizedValue, List<Object> history);
}

interface WeightAdjustmentClient {
    Map<String, Double> getCurrentWeights();
    void submitTradeResult(TradeDecision decision);
    void registerNewIndicator(String name, double initialWeight);
    void removeIndicator(String name);
}

class TradeDecision {
    private final boolean shouldTrade;
    private final double confidence;
    private final String asset;
    private final String action;

    public TradeDecision(boolean shouldTrade, double confidence, String asset, String action) {
        this.shouldTrade = shouldTrade;
        this.confidence = confidence;
        this.asset = asset;
        this.action = action;
    }

    // Getters
    public boolean isShouldTrade() { return shouldTrade; }
    public double getConfidence() { return confidence; }
    public String getAsset() { return asset; }
    public String getAction() { return action; }
}