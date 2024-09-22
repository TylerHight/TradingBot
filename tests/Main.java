import data_ingestion.QueryManager;

public class Main {

    public static void main(String[] args) {
        QueryManager queryManager = new QueryManager();

        // Test case 4: Test adding a query
        String newQuery = "query { NewQuery {} }";
        queryManager.addQuery("NewQuery", newQuery);
        String retrievedNewQuery = queryManager.getQuery("NewQuery");
        assert retrievedNewQuery.equals(newQuery);
    }
}




