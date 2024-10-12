public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        Solution.TradeDecisionManager service = solution.new TradeDecisionManager(0.7);

        Solution.TradingSignal signal = solution.new TradingSignal("AAPL", "BUY", 150.0, 1000000, "BULLISH", 0.8);
        Solution.TradeDecision decision = service.evaluateTrade(signal);
        assert decision != null : "Decision should not be null";

        System.out.println("TestEvaluateTradeDecisionNotNull_ShouldTrade passed!");
    }
}
