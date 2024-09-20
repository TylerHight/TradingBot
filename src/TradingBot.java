import data_ingestion.BitqueryClient;
import data_ingestion.DataStorage;

public class TradingBot {

    public static void main(String[] args) {
        // Replace with your actual access token
        String accessToken = "YOUR_ACCESS_TOKEN";
        BitqueryClient bitqueryClient = new BitqueryClient(accessToken);
        DataStorage dataStorage = new DataStorage(bitqueryClient);

        dataStorage.storeCryptoData(); // Fetch and store data
    }
}
