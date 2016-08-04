package me.tingri.big_data.hbase.client;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List<List<Map<String, String>>> getRecord(ResultScanner resultScanner) {
        List<List<Map<String, String>>> records = new ArrayList<>();

        for (Result result : resultScanner) records.add(getRecord(result));

        return records;
    }

    public static List<Map<String, String>> getRecord(Result result) {
        List<Map<String, String>> record = new ArrayList<>();

        for (Cell cell : result.listCells()) {
            Map<String, String> kv = new HashMap<>();

            kv.put("row", new String(CellUtil.cloneRow(cell)));
            kv.put("family", new String(CellUtil.cloneFamily(cell)));
            kv.put("column", new String(CellUtil.cloneQualifier(cell)));
            kv.put("value", new String(CellUtil.cloneValue(cell)));
            kv.put("timestamp", String.valueOf(cell.getTimestamp()));

            record.add(kv);
        }

        return record;
    }
}
