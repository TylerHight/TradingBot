public class Solution {

    // Inner TradeDecisionManager class inside Solution
    public class TradeDecisionManager {
        private final double confidenceThreshold;

        public TradeDecisionManager(double confidenceThreshold) {
            this.confidenceThreshold = confidenceThreshold;
        }

        public TradeDecision evaluateTrade(TradingSignal signal) {
            try {
                double confidence = calculateConfidence(signal);
                boolean shouldTrade = confidence >= confidenceThreshold;

                return new TradeDecision(shouldTrade, confidence, signal.getAsset(), signal.getAction());
            } catch (Exception e) {
                System.err.println("Error evaluating trade: " + e.getMessage());
                return new TradeDecision(false, 0, signal.getAsset(), signal.getAction());
            }
        }

        private double calculateConfidence(TradingSignal signal) {
            double volumeWeight = 0.2;
            double trendWeight = 0.3;
            double indicatorWeight = 0.5;

            double volumeSignal = normalizeVolume(signal.getVolume());
            double trendSignal = "BULLISH".equalsIgnoreCase(signal.getMarketTrend()) ? 1.0 : 0.0;
            double indicatorSignal = normalizeIndicator(signal.getTechnicalIndicator());

            return (volumeSignal * volumeWeight) + (trendSignal * trendWeight) + (indicatorSignal * indicatorWeight);
        }

        private double normalizeVolume(double volume) {
            return Math.min(volume / 1000000.0, 1.0);
        }

        private double normalizeIndicator(double indicator) {
            return Math.max(0, Math.min(indicator, 1));
        }
    }

    // Inner TradingSignal class
    public class TradingSignal {
        private String asset;
        private String action;
        private double price;
        private double volume;
        private String marketTrend;
        private double technicalIndicator;

        public TradingSignal(String asset, String action, double price, double volume, String marketTrend, double technicalIndicator) {
            this.asset = asset;
            this.action = action;
            this.price = price;
            this.volume = volume;
            this.marketTrend = marketTrend;
            this.technicalIndicator = technicalIndicator;
        }

        // Getters
        public String getAsset() { return asset; }
        public String getAction() { return action; }
        public double getPrice() { return price; }
        public double getVolume() { return volume; }
        public String getMarketTrend() { return marketTrend; }
        public double getTechnicalIndicator() { return technicalIndicator; }
    }

    // Inner TradeDecision class
    public class TradeDecision {
        private boolean shouldTrade;
        private double confidence;
        private String asset;
        private String action;

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
}
