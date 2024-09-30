package preprocessing.needsRefactoring.TimeSeriesAnalysisRefactoring;

public class Main4 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // Test EMA subsequent updates
        solution.addPrice(100, 1695686460000L);
        solution.addPrice(200, 1695686520000L);
        solution.addPrice(300, 1695686580000L);
        solution.addPrice(400, 1695686640000L);
        double expectedEMA = Math.round((((400 - 200.0) * (2.0 / (3 + 1))) + 200.0) * 100.0) / 100.0;
        assert Math.round(solution.calculateEMA(3) * 100.0) / 100.0 == expectedEMA;
    }
}
