package me.tingri.big_data.hbase.client;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;

/**
 * Created by sandeepkunkunuru on 7/29/16.
 */
public class Utilities {
    public static void result(ResultScanner resultScanner) {
        for (Result result : resultScanner) {
            cell(result);
        }
    }

    public static void cell(Result result) {
        for (Cell cell : result.listCells()) {
            String row = new String(CellUtil.cloneRow(cell));
            String family = new String(CellUtil.cloneFamily(cell));
            String column = new String(CellUtil.cloneQualifier(cell));
            String value = new String(CellUtil.cloneValue(cell));
            long timestamp = cell.getTimestamp();
            System.out.printf("%-20s column=%s:%s, timestamp=%s, value=%s\n", row, family, column, timestamp, value);
        }
    }
}
