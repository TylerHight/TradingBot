package data_ingestion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitcoinPriceFetcher {

    /**
     * Prints the current Bitcoin price.
     *
     * This method makes an HTTP GET request to the CoinDesk API to retrieve the current Bitcoin price
     * and prints it to the console.
     *
     * @throws IOException if there is an error making the HTTP request or reading the response.
     */
    public static void printBitcoinPrice() throws IOException {
        // Create a URL object with the CoinDesk API endpoint
        URL url = new URL("https://api.coindesk.com/v1/bpi/currentprice/BTC.json");

        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method to GET
        connection.setRequestMethod("GET");

        // Get the response code
        int responseCode = connection.getResponseCode();

        // Check if the request was successful (response code 200)
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Create a BufferedReader to read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // Read the response line by line
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Close the reader
            reader.close();

            // Parse the JSON response to get the Bitcoin price
            String json = response.toString();
            String price = json.substring(json.indexOf("rate") + 7, json.indexOf("rate") + 14);

            // Print the Bitcoin price
            System.out.println("The current Bitcoin price is: " + price);
        } else {
            // Print an error message if the request was not successful
            System.out.println("Failed to get Bitcoin price. Response code: " + responseCode);
        }

        // Disconnect the connection
        connection.disconnect();
    }

    public static void main(String[] args) {
        try {
            printBitcoinPrice();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}