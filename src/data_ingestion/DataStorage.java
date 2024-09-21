package data_ingestion;

import org.json.JSONObject;

public class DataStorage {

    private BitqueryClient bitqueryClient;

    public DataStorage(BitqueryClient bitqueryClient) {
        this.bitqueryClient = bitqueryClient;
    }

    public void storeCryptoData(String query) {
        try {
            String jsonData = bitqueryClient.fetchCryptoData(query);
            // Parse the JSON data
            parseAndStoreData(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseAndStoreData(String jsonData) {
        // For demonstration, we're simply printing the data
        // You can add your logic to parse and store the data as needed
        JSONObject jsonObject = new JSONObject(jsonData);

        // Extract and process the necessary information from jsonObject
        // For example, accessing trades:
        if (jsonObject.has("data")) {
            JSONObject data = jsonObject.getJSONObject("data");
            if (data.has("bitcoin")) {
                // Process bitcoin data
                // Implement your logic to store or use the extracted data
                System.out.println(data.getJSONObject("bitcoin").toString());
            }
        }
    }
}
