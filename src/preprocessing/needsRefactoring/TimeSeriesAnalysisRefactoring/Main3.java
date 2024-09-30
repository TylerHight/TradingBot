package preprocessing.needsRefactoring.TimeSeriesAnalysisRefactoring;

public class Main3 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // Test first EMA calculation
        solution.addPrice(100, 1695686520000L);
        solution.addPrice(200, 1695686580000L);
        solution.addPrice(300, 1695686640000L);
        assert solution.calculateEMA(3) == 200.0;
    }
}
