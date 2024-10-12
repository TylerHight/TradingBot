/**
package execution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@SpringBootApplication
@RestController
@RequestMapping("/api/trade-decision")
public class TradeDecisionService {

    private static final Logger logger = LoggerFactory.getLogger(TradeDecisionService.class);

    @Value("${trade.confidence.threshold}")
    private double confidenceThreshold;

    public static void main(String[] args) {
        SpringApplication.run(TradeDecisionService.class, args);
    }

    @PostMapping("/evaluate")
    public ResponseEntity<TradeDecision> evaluateTrade(@RequestBody TradingSignal signal) {
        try {
            double confidence = calculateConfidence(signal);
            boolean shouldTrade = confidence >= confidenceThreshold;

            TradeDecision decision = new TradeDecision(shouldTrade, confidence, signal.getAsset(), signal.getAction());

            logger.info("Trade decision made for {}: shouldTrade={}, confidence={}",
                    signal.getAsset(), shouldTrade, confidence);

            return ResponseEntity.ok(decision);
        } catch (Exception e) {
            logger.error("Error evaluating trade", e);
            return ResponseEntity.internalServerError().build();
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

class TradingSignal {
    private String asset;
    private String action;
    private double price;
    private double volume;
    private String marketTrend;
    private double technicalIndicator;

    // Constructors
    public TradingSignal() {}

    public TradingSignal(String asset, String action, double price, double volume, String marketTrend, double technicalIndicator) {
        this.asset = asset;
        this.action = action;
        this.price = price;
        this.volume = volume;
        this.marketTrend = marketTrend;
        this.technicalIndicator = technicalIndicator;
    }

    // Getters and Setters
    public String getAsset() { return asset; }
    public void setAsset(String asset) { this.asset = asset; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = volume; }

    public String getMarketTrend() { return marketTrend; }
    public void setMarketTrend(String marketTrend) { this.marketTrend = marketTrend; }

    public double getTechnicalIndicator() { return technicalIndicator; }
    public void setTechnicalIndicator(double technicalIndicator) { this.technicalIndicator = technicalIndicator; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradingSignal that = (TradingSignal) o;
        return Double.compare(that.price, price) == 0 &&
                Double.compare(that.volume, volume) == 0 &&
                Double.compare(that.technicalIndicator, technicalIndicator) == 0 &&
                Objects.equals(asset, that.asset) &&
                Objects.equals(action, that.action) &&
                Objects.equals(marketTrend, that.marketTrend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asset, action, price, volume, marketTrend, technicalIndicator);
    }

    @Override
    public String toString() {
        return "TradingSignal{" +
                "asset='" + asset + '\'' +
                ", action='" + action + '\'' +
                ", price=" + price +
                ", volume=" + volume +
                ", marketTrend='" + marketTrend + '\'' +
                ", technicalIndicator=" + technicalIndicator +
                '}';
    }
}

class TradeDecision {
    private boolean shouldTrade;
    private double confidence;
    private String asset;
    private String action;

    // Constructors
    public TradeDecision() {}

    public TradeDecision(boolean shouldTrade, double confidence, String asset, String action) {
        this.shouldTrade = shouldTrade;
        this.confidence = confidence;
        this.asset = asset;
        this.action = action;
    }

    // Getters and Setters
    public boolean isShouldTrade() { return shouldTrade; }
    public void setShouldTrade(boolean shouldTrade) { this.shouldTrade = shouldTrade; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public String getAsset() { return asset; }
    public void setAsset(String asset) { this.asset = asset; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeDecision that = (TradeDecision) o;
        return shouldTrade == that.shouldTrade &&
                Double.compare(that.confidence, confidence) == 0 &&
                Objects.equals(asset, that.asset) &&
                Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shouldTrade, confidence, asset, action);
    }

    @Override
    public String toString() {
        return "TradeDecision{" +
                "shouldTrade=" + shouldTrade +
                ", confidence=" + confidence +
                ", asset='" + asset + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
 **/