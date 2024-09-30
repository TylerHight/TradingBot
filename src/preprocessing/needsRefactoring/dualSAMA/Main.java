package preprocessing.needsRefactoring.dualSAMA;

public class Main {

    public static void main(String[] args) {
        Solution solution = new Solution();
        // Example input data
        Solution.SentimentPriceData[] priceData = {
                new Solution.SentimentPriceData("2024-09-24 14:56:00", 64208.99, 0.1),
                new Solution.SentimentPriceData("2024-09-24 14:57:00", 64189.27, 0.15),
                new Solution.SentimentPriceData("2024-09-24 14:58:00", 64200.0, 0.2),
                new Solution.SentimentPriceData("2024-09-24 14:59:00", 64291.0, 0.1),
                new Solution.SentimentPriceData("2024-09-24 15:00:00", 64322.53, -0.1),
                new Solution.SentimentPriceData("2024-09-24 15:01:00", 64322.61, -0.15),
                new Solution.SentimentPriceData("2024-09-24 15:02:00", 64148.27, -0.2),
                new Solution.SentimentPriceData("2024-09-24 15:03:00", 64247.91, 0.05),
                new Solution.SentimentPriceData("2024-09-24 15:04:00", 64358.97, 0.2),
                new Solution.SentimentPriceData("2024-09-24 15:05:00", 64363.48, 0.25),
                new Solution.SentimentPriceData("2024-09-24 15:06:00", 64417.97, -0.05),
        };
        Solution.Output result = solution.runSAMA(priceData, 3, 5);
        // Test output for only first data point
        double expectedSma = 0; // 0 because there's not enough data for the window size yet
        double expectedSama = 0; // 0 for the same reason as above
        double expectedDifference = 0;
        assert result.smaValues[0] == expectedSma &&
                result.samaValues[0] == expectedSama &&
                result.differences[0] == expectedDifference;


        Solution.SentimentPriceData[] priceData2 = {
                new Solution.SentimentPriceData("2024-09-24 14:56:00", 64208.99, 0.1),
                new Solution.SentimentPriceData("2024-09-24 14:57:00", 64189.27, 0.15),
                new Solution.SentimentPriceData("2024-09-24 14:58:00", 64200.0, 0.2),
                new Solution.SentimentPriceData("2024-09-24 14:59:00", 64291.0, 0.1),
                new Solution.SentimentPriceData("2024-09-24 15:00:00", 64322.53, -0.1),
                new Solution.SentimentPriceData("2024-09-24 15:01:00", 64322.61, -0.15),
                new Solution.SentimentPriceData("2024-09-24 15:02:00", 64148.27, -0.2),
                new Solution.SentimentPriceData("2024-09-24 15:03:00", 64247.91, 0.05),
                new Solution.SentimentPriceData("2024-09-24 15:04:00", 64358.97, 0.2),
                new Solution.SentimentPriceData("2024-09-24 15:05:00", 64363.48, 0.25),
                new Solution.SentimentPriceData("2024-09-24 15:06:00", 64417.97, -0.05),
        };
        Solution.Output result2 = solution.runSAMA(priceData2, 3, 5);
        // Test output for the 11th data point
        double expectedSma2 = 64380.14;
        double expectedSama2 = 64307.32;
        double expectedDifference2 = -72.82;
        assert result2.smaValues[10] == expectedSma2 &&
                result2.samaValues[10] == expectedSama2 &&
                result2.differences[10] == expectedDifference2;

        
        // Example input data
        Solution.SentimentPriceData[] priceData3 = {
                new Solution.SentimentPriceData("2024-09-24 14:56:00", 64208.99, 0.1),
                new Solution.SentimentPriceData("2024-09-24 14:57:00", 64189.27, 0.15),
                new Solution.SentimentPriceData("2024-09-24 14:58:00", 64200.0, 0.2),
                new Solution.SentimentPriceData("2024-09-24 14:59:00", 64291.0, 0.1),
                new Solution.SentimentPriceData("2024-09-24 15:00:00", 64322.53, -0.1),
        };
        // Negative window length
        try {
            Solution.Output result3 = solution.runSAMA(priceData3, -3, 5);
            assert false;
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Input window lengths must be positive integers.";
            assert e.getMessage().equals(expectedMessage);
        }


        // Example input data
        Solution.SentimentPriceData[] priceData4 = {
                new Solution.SentimentPriceData("2024-09-24 14:56:00", 64208.99, 0.1),
                new Solution.SentimentPriceData("2024-09-24 14:57:00", 64189.27, 0.15),
                new Solution.SentimentPriceData("2024-09-24 14:58:00", 64200.0, 0.2),
                new Solution.SentimentPriceData("2024-09-24 14:59:00", 64291.0, 0.1),
                new Solution.SentimentPriceData("2024-09-24 15:00:00", 64322.53, -0.1),
        };

        // Negative window length
        try {
            Solution.Output result4 = solution.runSAMA(priceData4, 6, 5);
            assert false;
        } catch (IllegalArgumentException e) {
            String expectedMessage = "The input short window length must be less than the long window.";
            assert e.getMessage().equals(expectedMessage);
        }
    }
}
