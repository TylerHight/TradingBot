package data_ingestion;

public class CurrentPriceTime {
    private final String price;
    private final String time;

    public CurrentPriceTime(String price, String time) {
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
