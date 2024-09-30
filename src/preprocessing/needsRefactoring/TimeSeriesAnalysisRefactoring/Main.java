package preprocessing.needsRefactoring.TimeSeriesAnalysisRefactoring;

public class Main{
    public static void main(String[] args) {
        Solution solution = new Solution();

        // Not enough data for simple moving average
        solution.addPrice(100, 1695686400000L);
        assert Double.isNaN(solution.calculateMovingAverage(3));
    }
}
