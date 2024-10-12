/**
 package strategy;

import java.util.HashMap;
import java.util.Map;

public class TradeDecisionManagerTest {
    public static void main(String[] args) {
        testBasicTradeDecision();
        testMultipleIndicators();
        System.out.println("All tests passed!");
    }

    private static void testBasicTradeDecision() {
        TradeDecisionManager manager = new TradeDecisionManager(0.6);
        manager.addIndicator("price", 1.0, Double.class, price -> price / 100.0);

        Map<String, Object> signalData = new HashMap<>();
        signalData.put("asset", "AAPL");
        signalData.put("action", "BUY");
        signalData.put("price", 150.0);

        TradeDecision decision = manager.evaluateTrade(signalData);

        assert decision.isShouldTrade() : "Should decide to trade";
        assert decision.getConfidence() > 0.6 : "Confidence should be greater than threshold";
        assert "AAPL".equals(decision.getAsset()) : "Asset should be AAPL";
        assert "BUY".equals(decision.getAction()) : "Action should be BUY";
    }

    private static void testMultipleIndicators() {
        TradeDecisionManager manager = new TradeDecisionManager(0.7);
        manager.addIndicator("price", 0.5, Double.class, price -> price / 100.0);
        manager.addIndicator("volume", 0.5, Long.class, volume -> Math.min(volume / 1000000.0, 1.0));

        Map<String, Object> signalData = new HashMap<>();
        signalData.put("asset", "GOOGL");
        signalData.put("action", "SELL");
        signalData.put("price", 2500.0);
        signalData.put("volume", 1000000L);

        TradeDecision decision = manager.evaluateTrade(signalData);

        assert decision.isShouldTrade() : "Should decide to trade";
        assert decision.getConfidence() > 0.7 : "Confidence should be greater than threshold";
        assert "GOOGL".equals(decision.getAsset()) : "Asset should be GOOGL";
        assert "SELL".equals(decision.getAction()) : "Action should be SELL";
    }
}
**/