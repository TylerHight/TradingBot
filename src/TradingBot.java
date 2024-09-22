import data_ingestion.BitqueryClient;
import data_ingestion.QueryManager;

import java.io.IOException;

public class TradingBot {

    public static void main(String[] args) throws IOException {
        // Replace with your actual API key and access token
        String apiKey = System.getenv("BITQUERY_API_KEY");
        String accessToken = System.getenv("BITQUERY_ACCESS_TOKEN");
        BitqueryClient client = new BitqueryClient(apiKey, accessToken);

        QueryManager queryManager = new QueryManager();
        String query = queryManager.getQuery("RecentBSCBlocksQuery");

        String responseData = client.fetchCryptoData(query);

        if (responseData != null) {
            System.out.println(responseData);
        } else {
            System.err.println("Failed to fetch data.");
        }
    }
}
