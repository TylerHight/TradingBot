package data_ingestion;

public class TestMarketDataFetcher {

    public static void main(String[] args) {
        MarketDataFetcher fetcher = new MarketDataFetcher();
        String marketData = fetcher.fetchMarketData();

        // Print the fetched market data
        System.out.println("Fetched Market Data:");
        System.out.println(marketData);
    }
}
