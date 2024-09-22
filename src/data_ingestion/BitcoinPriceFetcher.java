package data_ingestion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitcoinPriceFetcher {

    // Create logger instance
    private static final Logger logger = LoggerFactory.getLogger(BitcoinPriceFetcher.class);

    /**
     * Makes an HTTP GET request to the CoinDesk API to retrieve the current Bitcoin price and associated time.
     *
     * @return A PriceTime object containing the price and the time.
     * @throws IOException If there is an error making the request or reading the response.
     */
    public static PriceTime getCurrentPriceAndTime() throws IOException {
        logger.info("Starting request to fetch Bitcoin price and time.");
        URL url = new URL("https://api.coindesk.com/v1/bpi/currentprice/BTC.json");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Get the response code
        int responseCode = connection.getResponseCode();
        logger.info("Received response code: {}", responseCode);

        // If the request succeeded
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            // Read through the response
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            logger.info("Response received: {}", response.toString());

            // Parse JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.toString());

            // Extract price and time
            String price = rootNode.path("bpi").path("USD").path("rate").asText();
            String time = rootNode.path("time").path("updated").asText();

            logger.info("Fetched price: {} and time: {}", price, time);

            // Return the price and time
            return new PriceTime(price, time);
        } else {
            String errorMessage = "Failed to get Bitcoin price. Response code: " + responseCode;
            logger.error(errorMessage);
            throw new IOException(errorMessage);
        }
    }

    /**
     * Stores price and time data
     */
    public static class PriceTime {
        private final String price;
        private final String time;

        public PriceTime(String price, String time) {
            this.price = price;
            this.time = time;
        }

        public String getPrice() {
            return price;
        }

        public String getTime() {
            return time;
        }

        @Override
        public String toString() {
            return "Price: " + price + ", Time: " + time;
        }
    }

    public static void main(String[] args) {
        try {
            PriceTime priceTime = getCurrentPriceAndTime();
            System.out.println(priceTime);  // Uses the overridden toString method in PriceTime class
        } catch (IOException e) {
            logger.error("Error occurred while fetching Bitcoin price: {}", e.getMessage());
        }
    }
}
