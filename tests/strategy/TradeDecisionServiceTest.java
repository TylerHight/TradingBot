package strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "trade.confidence.threshold=0.7")
public class TradeDecisionServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/trade-decision/evaluate";
    }

    @Test
    public void testEvaluateTrade_ShouldTrade() {
        TradingSignal signal = new TradingSignal("AAPL", "BUY", 150.0, 1000000, "BULLISH", 0.8);

        ResponseEntity<TradeDecision> response = restTemplate.postForEntity(baseUrl, signal, TradeDecision.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isShouldTrade());
        assertTrue(response.getBody().getConfidence() >= 0.7);
    }

    @Test
    public void testEvaluateTrade_ShouldNotTrade() {
        TradingSignal signal = new TradingSignal("AAPL", "SELL", 150.0, 100000, "BEARISH", 0.2);

        ResponseEntity<TradeDecision> response = restTemplate.postForEntity(baseUrl, signal, TradeDecision.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isShouldTrade());
        assertTrue(response.getBody().getConfidence() < 0.7);
    }

    @Test
    public void testEvaluateTrade_InvalidInput() {
        TradingSignal signal = new TradingSignal(null, null, 0, 0, null, 0);

        ResponseEntity<TradeDecision> response = restTemplate.postForEntity(baseUrl, signal, TradeDecision.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isShouldTrade());
    }
}