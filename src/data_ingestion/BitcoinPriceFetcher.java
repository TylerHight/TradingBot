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

    private static final Logger logger = LoggerFactory.getLogger(BitcoinPriceFetcher.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Helper method to send GET requests
    private static String sendGetRequest(String urlString, String apiKey) throws IOException {
        logger.info("Sending request to: {}", urlString);
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("GET");
        if (apiKey != null) {
            connection.setRequestProperty("X-MBX-APIKEY", apiKey);
        }

        int responseCode = connection.getResponseCode();
        logger.info("Received response code: {}", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                logger.info("Response received: {}", response.toString());
                return response.toString();
            }
        } else {
            String errorMessage = "Request failed. Response code: " + responseCode;
            logger.error(errorMessage);
            throw new IOException(errorMessage);
        }
    }

    // Method to fetch the current Bitcoin price and time
    public static CurrentPriceTime getCurrentPriceAndTime() throws IOException {
        String response = sendGetRequest("https://api.coindesk.com/v1/bpi/currentprice/BTC.json", null);
        JsonNode rootNode = objectMapper.readTree(response);
        String price = rootNode.path("bpi").path("USD").path("rate").asText();
        String time = rootNode.path("time").path("updated").asText();
        logger.info("Fetched current price: {} and time: {}", price, time);
        return new CurrentPriceTime(price, time);
    }

    // Method to fetch daily historical prices for a given date range
    public static DailyPriceTime getDailyHistoricalPrices(String startDate, String endDate) throws IOException {
        String urlString = String.format("https://api.coindesk.com/v1/bpi/historical/close.json?start=%s&end=%s", startDate, endDate);
        String response = sendGetRequest(urlString, null);
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode pricesNode = rootNode.path("bpi");

        DailyPriceTime historicalPrices = new DailyPriceTime();
        pricesNode.fieldNames().forEachRemaining(date -> {
            double price = pricesNode.path(date).asDouble();
            historicalPrices.addPrice(date, price);
        });
        logger.info("Fetched historical prices for range: {} to {}.", startDate, endDate);
        return historicalPrices;
    }

    // Method to fetch minute-level prices from Binance API
    public static MinutePriceTime getMinuteLevelPrices(String symbol, String interval, String apiKey) throws IOException {
        String urlString = String.format("https://api.binance.us/api/v3/klines?symbol=%s&interval=%s", symbol, interval);
        String response = sendGetRequest(urlString, apiKey);
        JsonNode rootNode = objectMapper.readTree(response);

        MinutePriceTime minutePriceTime = new MinutePriceTime();
        for (JsonNode kline : rootNode) {
            long openTime = kline.get(0).asLong();
            double openPrice = kline.get(1).asDouble();
            minutePriceTime.addPrice(openTime, openPrice);
        }
        logger.info("Fetched minute-level prices for symbol: {} at interval: {}.", symbol, interval);
        return minutePriceTime;
    }

    public static void main(String[] args) {
        try {
            CurrentPriceTime priceTime = getCurrentPriceAndTime();
            System.out.println(priceTime);

            DailyPriceTime historicalPrices = getDailyHistoricalPrices("2024-09-01", "2024-09-10");
            System.out.println(historicalPrices);

            MinutePriceTime minutePrices = getMinuteLevelPrices("BTCUSDT", "1m", System.getenv("BINANCE_API_KEY"));
            System.out.println(minutePrices);
        } catch (IOException e) {
            logger.error("Error occurred: {}", e.getMessage());
        }
    }
}
