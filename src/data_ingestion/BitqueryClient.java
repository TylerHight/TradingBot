package data_ingestion;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class BitqueryClient {

    private String accessToken;

    public BitqueryClient(String accessToken) {
        this.accessToken = accessToken;
    }

    public String fetchCryptoData() throws Exception {
        String url = "https://graphql.bitquery.io/";
        String query = "{ bitcoin { trades(options: {limit: 10}) { id price timestamp amount exchange { name } } } }";

        // Create HTTP client
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + accessToken);

            // Set the request body
            StringEntity entity = new StringEntity("{\"query\": \"" + query + "\"}");
            post.setEntity(entity);

            // Execute request and process response
            try (CloseableHttpResponse response = client.execute(post)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
}
