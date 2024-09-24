package data_ingestion;

import java.util.HashMap;
import java.util.Map;

public class DailyPriceTime {
    private final Map<String, Double> prices = new HashMap<>();

    public void addPrice(String date, double price) {
        prices.put(date, price);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        prices.forEach((date, price) -> sb.append("Date: ").append(date).append(", Price: ").append(price).append("\n"));
        return sb.toString();
    }
}