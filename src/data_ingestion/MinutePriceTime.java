package data_ingestion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MinutePriceTime {

    // Inner class to store each minute's price and timestamp
    public static class PriceTimeEntry {
        private long timestamp;
        private double price;

        public PriceTimeEntry(long timestamp, double price) {
            this.timestamp = timestamp;
            this.price = price;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            // Convert the timestamp to a readable date
            Date date = new Date(timestamp);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "Time: " + formatter.format(date) + ", Price: " + price;
        }
    }

    // Holds all the price-times
    private List<PriceTimeEntry> priceTimeEntries;

    public MinutePriceTime() {
        this.priceTimeEntries = new ArrayList<>();
    }

    /**
     * Adds a new price and timestamp to the list.
     *
     * @param timestamp The timestamp in milliseconds since epoch.
     * @param price The price at the given timestamp.
     */
    public void addPrice(long timestamp, double price) {
        this.priceTimeEntries.add(new PriceTimeEntry(timestamp, price));
    }

    /**
     * Returns the list of price-time entries.
     *
     * @return The list of PriceTimeEntry objects.
     */
    public List<PriceTimeEntry> getPriceTimeEntries() {
        return priceTimeEntries;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Minute-Level Bitcoin Prices:\n");
        for (PriceTimeEntry entry : priceTimeEntries) {
            sb.append(entry.toString()).append("\n");
        }
        return sb.toString();
    }
}
