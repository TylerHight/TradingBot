package data_ingestion;

import okhttp3.*;

import java.io.IOException;

public class BitqueryClient {
    private String apiKey;
    private String accessToken;

    public BitqueryClient(String apiKey, String accessToken) {
        this.apiKey = apiKey;
        this.accessToken = accessToken;
    }

    public String fetchCryptoData(String query) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, query);
        Request request = new Request.Builder()
                .url("https://streaming.bitquery.io/graphql")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-API-KEY", apiKey)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        // Parse the response body as JSON if needed
        return response.body().string();
    }

}
