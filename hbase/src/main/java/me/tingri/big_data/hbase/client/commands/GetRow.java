package me.tingri.big_data.hbase.client.commands;

import me.tingri.big_data.hbase.client.Utilities;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

/**
 * A simple get command. This command takes the form of
 * "get [rowid]" and prints all columns for the row.
 */
public class GetRow extends Command {

    public GetRow(String name) {
        super(name);
    }

    public void run(Connection connection, String... args) throws InvalidArgsException, IOException {
        if (args.length != 2) throw new InvalidArgsException(args);

        String tableName = args[0];
        String rowId = args[1];

        Table table = connection.getTable(TableName.valueOf(tableName));

        // CreateTable a new GetRow request and specify the rowId passed by the user.
        Result result = table.get(new Get(rowId.getBytes()));

        // Iterate of the results. Each Cell is a value for column
        // so multiple Cells will be processed for each row.
        Utilities.cell(result);
    }

    public String getOptions() {
        return "TABLENAME ROWID";
    }

    public String getDescription() {
        return "GetRow all columns from a single row";
    }
}
