package test.me.tingri.big_data.hbase.client;

import me.tingri.big_data.util.TimeIt;
import me.tingri.big_data.hbase.client.Utilities;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by sandeepkunkunuru on 8/3/16.
 * Search for all nodes that were part of 123 or 456 operations and suppress nodes that were part of 789 operation
 */
public class TestMultiLevelFiltering {

    @Test
    public void testList() throws Exception {
        Configuration conf = HBaseConfiguration.create();

        try {
            String tableName = "cluster";
            List<List<Map<String, String>>> exportList = TimeIt.time(() -> getExportList(conf, tableName));

            System.out.println("Number of Included list Records" + exportList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<List<Map<String, String>>> getExportList(Configuration conf, String tableName) throws IOException {
        Scan scan = new Scan();
        FilterList superSet = new FilterList(FilterList.Operator.MUST_PASS_ALL);

        superSet.addFilter(getInclusionFilter(tableName));
        superSet.addFilter(getSuppressionList(tableName));
        scan.setFilter(superSet);

        System.out.println("filter created");

        Connection conn = ConnectionFactory.createConnection(conf);

        System.out.println("connection created");

        Table table = conn.getTable(TableName.valueOf(tableName));
        ResultScanner resultScanner = table.getScanner(scan);

        return Utilities.getRecord(resultScanner);
    }

    private Filter getSuppressionList(String tableName) {
        FilterList suppressionList = new FilterList(FilterList.Operator.MUST_PASS_ONE);

        SingleColumnValueFilter filterByCol4 = new SingleColumnValueFilter(
                Bytes.toBytes(tableName),
                Bytes.toBytes("operation_789"),
                CompareFilter.CompareOp.EQUAL,
                new NullComparator());

        filterByCol4.setFilterIfMissing(false);
        filterByCol4.setLatestVersionOnly(true);

        suppressionList.addFilter(filterByCol4);

        return suppressionList;
    }

    private Filter getInclusionFilter(String tableName) {
        FilterList inclusionList = new FilterList(FilterList.Operator.MUST_PASS_ONE);

        SingleColumnValueFilter filterByCol1 = new SingleColumnValueFilter(
                Bytes.toBytes(tableName),
                Bytes.toBytes("operation_123"),
                CompareFilter.CompareOp.NOT_EQUAL,
                new NullComparator());
        SingleColumnValueFilter filterByCol2 = new SingleColumnValueFilter(
                Bytes.toBytes(tableName),
                Bytes.toBytes("operation_456"),
                CompareFilter.CompareOp.NOT_EQUAL,
                new NullComparator());

        filterByCol1.setFilterIfMissing(true);
        filterByCol1.setLatestVersionOnly(true);

        filterByCol2.setFilterIfMissing(true);
        filterByCol2.setLatestVersionOnly(true);

        inclusionList.addFilter(filterByCol1);
        inclusionList.addFilter(filterByCol2);

        return inclusionList;
    }
}
