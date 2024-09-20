package data_ingestion;

public class DataStorage {

    private BitqueryClient bitqueryClient;

    public DataStorage(BitqueryClient bitqueryClient) {
        this.bitqueryClient = bitqueryClient;
    }

    public void storeCryptoData() {
        try {
            String jsonData = bitqueryClient.fetchCryptoData();
            // Parse and store the data as needed
            System.out.println(jsonData); // For debugging
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
