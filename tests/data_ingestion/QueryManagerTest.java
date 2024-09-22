package data_ingestion;

public class QueryManagerTest {
    public static void main(String[] args) {
        QueryManager queryManager = new QueryManager();

        // Test case 1: Test getting a query
        String recentBSCBlocksQuery = queryManager.getQuery("AddressIntersectQuery");
        assert recentBSCBlocksQuery.equals("query($addresses: [String!]) { EVM(dataset: archive){ Transfers(where: { any: [ { Transfer: {Sender: {in: $addresses} Receiver: {notIn: $addresses}} }, { Transfer: {Receiver: {in: $addresses} Sender: {notIn: $addresses}} }, ] }) { array_intersect(side1: Transfer_Sender side2: Transfer_Receiver intersectWith: $addresses) } } } } <!-- Parameters --> { \"addresses\": [\"0x21743a2efb926033f8c6e0c3554b13a0c669f63f\",\"0x107f308d85d5481f5b729cfb1710532500e40217\"] }");

        // Test case 2: Test trying to get a query that doesn't exist
        String nonExistingQuery = queryManager.getQuery("NonExistingQuery");
        assert nonExistingQuery == null;

        // Test case 3: Test removing a query
        queryManager.removeQuery("MaxEthTransferByDate");
        String maxEthTransferQuery = queryManager.getQuery("MaxEthTransferByDate");
        assert maxEthTransferQuery == null;

        // Test case 4: Test adding a query
        String newQuery = "query { NewQuery {} }";
        queryManager.addQuery("NewQuery", newQuery);
        String retrievedNewQuery = queryManager.getQuery("NewQuery");
        assert retrievedNewQuery.equals(newQuery);

        System.out.println("All tests passed successfully.");
    }
}
