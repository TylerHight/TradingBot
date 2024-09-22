package data_ingestion;

import java.util.HashMap;
import java.util.Map;

public class QueryManager {
    private final Map<String, String> queries;

    public QueryManager() {
        queries = new HashMap<>();
        initializeQueries();
    }

    private void initializeQueries() {
        queries.put("RecentBSCBlocksQuery", "{ \"query\": \"{ EVM(network: bsc, dataset: archive) { Blocks(limit: { count: 10 }) { Block { Number Time } } } }\" }");
        queries.put("MaxEthTransferByDate", "query { EVM(dataset: archive network: eth) { Transfers(where: { Block: {Date: {after: \"2022-02-20\"}} Transfer: {Currency: {Native: true}}}) { Block { Date } Transfer { Amount(maximum: Transfer_Amount) } } } } }");
        queries.put("AddressIntersectQuery", "query($addresses: [String!]) { EVM(dataset: archive){ Transfers(where: { any: [ { Transfer: {Sender: {in: $addresses} Receiver: {notIn: $addresses}} }, { Transfer: {Receiver: {in: $addresses} Sender: {notIn: $addresses}} }, ] }) { array_intersect(side1: Transfer_Sender side2: Transfer_Receiver intersectWith: $addresses) } } } } <!-- Parameters --> { \"addresses\": [\"0x21743a2efb926033f8c6e0c3554b13a0c669f63f\",\"0x107f308d85d5481f5b729cfb1710532500e40217\"] }");
        queries.put("GetRecentBitcoinTransactions", "query { bitcoin { transactions(date: {after: \"2023-07-22\"} options: {desc: \"block.timestamp.iso8601\", limit: 10}) { block { height timestamp { iso8601 } } feeValue(in: USD) feeValueDecimal hash index inputCount inputCountBigInt inputValue inputValueDecimal minedValue minedValueDecimal outputCount outputCountBigInt outputValue outputValueDecimal txCoinbase txLocktime txSize txVersion txVsize txWeight } } }");
    }

    public String getQuery(String queryName) {
        return queries.getOrDefault(queryName, null);
    }

    public void removeQuery(String queryName) {
        if (queries.containsKey(queryName)) {
            queries.remove(queryName);
            System.out.println("Query removed: " + queryName);
        } else {
            System.out.println("Query not found: " + queryName);
        }
    }

    public void addQuery(String queryName, String query) {
        queries.put(queryName, query);
        System.out.println("Query added: " + queryName);
    }
}
