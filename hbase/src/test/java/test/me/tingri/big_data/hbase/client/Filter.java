package test.me.tingri.big_data.hbase.client;

import me.tingri.big_data.hbase.client.COMMANDS;
import me.tingri.big_data.hbase.client.commands.InvalidArgsException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by sandeepkunkunuru on 7/29/16.
 *
 put 'filter_example', 'row1', 'cf:a', 'value1'
 put 'filter_example', 'row2', 'cf:b', 'value2'
 put 'filter_example', 'row3', 'cf:c', 'value3'
 put 'filter_example', 'row3', 'cf:col1', 'value_filter_example'
 put 'filter_example', 'row4', 'cf:d', 'value4'

 # no rows
 scan 'filter_example', {COLUMNS => ['cf'],  FILTER => "SingleColumnValueFilter('cf', 'b', =, 'regexstring:value3', true, true)"}

 #row2
 scan 'filter_example', {COLUMNS => ['cf'],  FILTER => "SingleColumnValueFilter('cf', 'b', =, 'regexstring:value*', true, true)"}

 #row3
 scan 'filter_example', {COLUMNS => ['cf'],  FILTER => "SingleColumnValueFilter('cf', 'c', =, 'regexstring:value3', true, true)"}

 #row3
 scan 'filter_example', {COLUMNS => ['cf'],  FILTER => "SingleColumnValueFilter('cf', 'c', =, 'regexstring:va*', true, true)"}

 #row3
 scan 'filter_example', {COLUMNS => ['cf'],  FILTER => "SingleColumnValueFilter('cf', 'c', =, 'regexstring:.', true, true)"}
 */
public class Filter {
    @Test
    public void complete() throws Exception {
        Configuration conf = HBaseConfiguration.create();



        try {
            String tableName = "filter_example";

            Connection conn = ConnectionFactory.createConnection(conf);

            COMMANDS.CREATE.run(conn, tableName, "cf");

            COMMANDS.PUT.run(conn, tableName, "row1", "cf", "a", "value1");
            COMMANDS.PUT.run(conn, tableName, "row2", "cf", "b", "value2");
            COMMANDS.PUT.run(conn, tableName, "row3", "cf", "c", "value3");
            COMMANDS.PUT.run(conn, tableName, "row4", "cf", "d", "value4");
            COMMANDS.PUT.run(conn, tableName, "row3", "cf", "col1", "value_filter_example");

            // This command supports using a columnvalue filter.
            // The filter takes the form of <columnfamily>:<column><operator><value>
            // An example would be cf:col>=10
            COMMANDS.SCAN.run(conn, tableName, "cf:b=value3", "true", "true");  // no rows
            COMMANDS.SCAN.run(conn, tableName, "cf:b=^value", "true", "true"); //row2
            COMMANDS.SCAN.run(conn, tableName, "cf:c=value3", "true", "true"); //row3
            COMMANDS.SCAN.run(conn, tableName, "cf:c=^va", "true", "true"); //row3
            COMMANDS.SCAN.run(conn, tableName, "cf:c=.*", "true", "true"); //row3


        } catch (IOException | InvalidArgsException e) {
            e.printStackTrace();
        }

    }
}
