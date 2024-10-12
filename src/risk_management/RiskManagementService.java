package risk_management;

import java.util.HashMap;
import java.util.Map;

public class RiskManagementService {

    public static RiskAssessment calculateRisk(
            double accountBalance,
            double maxRiskPercentage,
            double currentPrice,
            double volatility,
            double confidence,
            Map<String, Double> otherFactors,
            double tradingFeePercentage
    ) {
        // Adjust position size based on chosen max risk percentage
        double maxPositionSize = accountBalance * maxRiskPercentage;

        // Adjust position size based on volatility
        double volatilityFactor = 1 / (1 + volatility);
        double adjustedPositionSize = maxPositionSize * volatilityFactor;

        // Adjust position size based on confidence
        adjustedPositionSize *= confidence;

        // Calculate risk score
        double riskScore = calculateRiskScore(volatility, confidence, otherFactors);

        // Determine trade should be executed based on risk score
        boolean shouldTrade = riskScore < 0.7 && confidence > 0.6;

        // Calculate estimated fees
        double estimatedFees = adjustedPositionSize * tradingFeePercentage;

        // Adjust position size for fees
        double feeAdjustedPositionSize = adjustedPositionSize - estimatedFees;

        // Ensure the position size after fees doesn't exceed the max risk
        if (feeAdjustedPositionSize / accountBalance > maxRiskPercentage) {
            feeAdjustedPositionSize = maxPositionSize - estimatedFees;
            estimatedFees = feeAdjustedPositionSize * tradingFeePercentage;
        }

        // Final position size (0 if we shouldn't trade)
        double finalPositionSize = shouldTrade ? feeAdjustedPositionSize : 0;

        return new RiskAssessment(shouldTrade, finalPositionSize, riskScore, estimatedFees);
    }

    private static double calculateRiskScore(
            double volatility,
            double confidence,
            Map<String, Double> additionalFactors
    ) {
        double baseRiskScore = (volatility * 0.4) + ((1 - confidence) * 0.6);

        // Consider additional factors
        for (double factor : additionalFactors.values()) {
            baseRiskScore += factor * 0.05; // Each additional factor has a small impact
        }

        // Normalize risk score between 0 and 1
        return Math.min(1, Math.max(0, baseRiskScore));
    }

    public static class RiskAssessment {
        public final boolean shouldTrade;
        public final double positionSize;
        public final double riskScore;
        public final double estimatedFees;

        public RiskAssessment(boolean shouldTrade, double positionSize, double riskScore, double estimatedFees) {
            this.shouldTrade = shouldTrade;
            this.positionSize = positionSize;
            this.riskScore = riskScore;
            this.estimatedFees = estimatedFees;
        }
    }

    public static void main(String[] args) {
        // Example usage
        double accountBalance = 10000; // $10,000
        double maxRiskPercentage = 0.02; // 2% max risk per trade
        double currentPrice = 50000; // Current BTC price
        double volatility = 0.05; // 5% volatility
        double confidence = 0.75; // 75% confidence in the trade
        double tradingFeePercentage = 0.001; // 0.1% trading fee

        Map<String, Double> additionalFactors = new HashMap<>();
        additionalFactors.put("marketSentiment", 0.2); // Positive market sentiment
        additionalFactors.put("newsImpact", 0.1); // Minor positive news

        RiskAssessment assessment = calculateRisk(
                accountBalance,
                maxRiskPercentage,
                currentPrice,
                volatility,
                confidence,
                additionalFactors,
                tradingFeePercentage
        );

        System.out.println("Should trade: " + assessment.shouldTrade);
        System.out.printf("Recommended position size: $%.2f%n", assessment.positionSize);
        System.out.printf("Risk score: %.2f%n", assessment.riskScore);
        System.out.printf("Estimated fees: $%.2f%n", assessment.estimatedFees);
    }
}