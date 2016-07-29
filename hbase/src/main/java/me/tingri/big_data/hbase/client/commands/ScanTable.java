package me.tingri.big_data.hbase.client.commands;

import me.tingri.big_data.hbase.client.Utilities;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;

import java.io.IOException;

/**
 * This command will scan a Bigtable table. Scanning,
 * by default, returns all columns from all rows in the table.
 * Many more options are available when scanning a table so
 * please see the documentation for the standard HBase ScanTable
 * and ResultScanner classes.
 */
public class ScanTable extends Command {

    public ScanTable(String name) {
        super(name);
    }

    public void run(Connection connection, String... args) throws InvalidArgsException, IOException {
        if (args.length < 1 || args.length > 4) throw new InvalidArgsException(args);

        String tableName = args[0];
        String filterVal = null;

        if (args.length > 1) filterVal = args[1];

        Table table = connection.getTable(TableName.valueOf(tableName));

        // CreateTable a new ScanTable instance.
        Scan scan = new Scan();

        // This command supports using a columnvalue filter.
        // The filter takes the form of <columnfamily>:<column><operator><value>
        // An example would be cf:col>=10
        if (filterVal != null) {
            String splitVal = "=";
            CompareFilter.CompareOp op = CompareFilter.CompareOp.EQUAL;

            if (filterVal.contains(">=")) {
                op = CompareFilter.CompareOp.GREATER_OR_EQUAL;
                splitVal = ">=";
            } else if (filterVal.contains("<=")) {
                op = CompareFilter.CompareOp.LESS_OR_EQUAL;
                splitVal = "<=";
            } else if (filterVal.contains(">")) {
                op = CompareFilter.CompareOp.GREATER;
                splitVal = ">";
            } else if (filterVal.contains("<")) {
                op = CompareFilter.CompareOp.LESS;
                splitVal = "<";
            }
            String[] filter = filterVal.split(splitVal);
            String[] filterCol = filter[0].split(":");
            SingleColumnValueFilter filterObj = null;

            //	SingleColumnValueFilter(byte[] family, byte[] qualifier, CompareFilter.CompareOp compareOp, ByteArrayComparable comparator, boolean filterIfMissing, boolean latestVersionOnly)
            if(args.length  == 2){
                filterObj = new SingleColumnValueFilter(filterCol[0].getBytes(), filterCol[1].getBytes(), op, filter[1].getBytes());
            } else {
                filterObj = new SingleColumnValueFilter(filterCol[0].getBytes(), filterCol[1].getBytes(), op, new RegexStringComparator(filter[1]));
                filterObj.setFilterIfMissing(Boolean.valueOf(args[2]));
                filterObj.setLatestVersionOnly(Boolean.valueOf(args[3]));
            }

            scan.setFilter(filterObj);
        }
        ResultScanner resultScanner = table.getScanner(scan);
        Utilities.result(resultScanner);
    }

    public String getOptions() {
        return "TABLENAME [FILTER]";
    }

    public String getDescription() {
        return "ScanTable a table";
    }
}
