public class Solution {

    public static class SentimentPriceData {

        private String time;
        private double price;
        private double sentiment;

        public SentimentPriceData(String time, double price, double sentiment) {
            this.time = time;
            this.price = price;
            this.sentiment = sentiment;
        }

        public String getTime() {
            return time;
        }

        public double getPrice() {
            return price;
        }

        public double getSentiment() {
            return sentiment;
        }
    }

    public static class Output {

        public double[] smaValues;
        public double[] samaValues;
        public double[] differences;

        public Output(double[] smaValues, double[] samaValues, double[] differences) {
            this.smaValues = smaValues;
            this.samaValues = samaValues;
            this.differences = differences;
        }

        public double[] getSmaValues() {
            return smaValues;
        }

        public double[] getSamaValues() {
            return samaValues;
        }

        public double[] getDifferences() {
            return differences;
        }
    }

    public Output runSAMA(SentimentPriceData[] data, int shortWindow, int defaultLongWindow) {
        if (shortWindow <= 0 || defaultLongWindow <= 0) {
            throw new IllegalArgumentException("Input window lengths must be positive integers.");
        }

        if (shortWindow >= defaultLongWindow) {
            throw new IllegalArgumentException("The input short window length must be less than the long window.");
        }

        int n = data.length;
        double[] smaValues = new double[n];
        double[] samaValues = new double[n];
        double[] differences = new double[n];

        for (int i = 0; i < n; i++) {
            // Simple Moving Average (SMA)
            smaValues[i] = calculateSMA(data, shortWindow, i);

            // Calculate the adjusted long window using sentiment
            double sentiment = Math.abs(data[i].getSentiment());
            int longWindow = (int) Math.round(defaultLongWindow - ((defaultLongWindow - shortWindow) * sentiment));

            // Sentiment-Adjusted Moving Average (SAMA)
            samaValues[i] = calculateSMA(data, longWindow, i);

            // Calculate the difference
            if (smaValues[i] == 0 || samaValues[i] == 0) {
                differences[i] = 0;
            } else {
                differences[i] = smaValues[i] - samaValues[i];
            }

            // Round to four decimal places
            smaValues[i] = Math.round(smaValues[i] * 10000.0) / 10000.0;
            samaValues[i] = Math.round(samaValues[i] * 10000.0) / 10000.0;
            differences[i] = Math.round(differences[i] * 10000.0) / 10000.0;
        }

        return new Output(smaValues, samaValues, differences);
    }

    // Helper method to calculate simple moving average (SMA)
    private double calculateSMA(SentimentPriceData[] data, int window, int index) {
        if (index < window - 1) return 0;
        double sum = 0;
        for (int i = index - window + 1; i <= index; i++) {
            sum += data[i].getPrice();
        }
        return sum / window;
    }
}