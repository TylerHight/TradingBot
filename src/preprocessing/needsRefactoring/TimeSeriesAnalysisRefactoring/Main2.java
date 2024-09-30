package preprocessing.needsRefactoring.TimeSeriesAnalysisRefactoring;

public class Main2 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        solution.addPrice(100, 1695686400000L);
        solution.addPrice(200, 1695686460000L);
        solution.addPrice(300, 1695686520000L);
        solution.addPrice(200, 1695686580000L);
        solution.addPrice(300, 1695686640000L);
        solution.calculateMovingAverage(3);
        // Should be the average of the three most recent data points
        assert Math.round(solution.calculateMovingAverage(3) * 100.0) / 100.0 == 266.67;
    }
}
