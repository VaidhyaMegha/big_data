package me.tingri.big_data.hbase.client.commands;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * This command puts a single column value to a table.
 * It takes the form of "put [table] [rowid] [columnfamily] [column] [value]
 */
public class PutValue extends Command {

    public PutValue(String name) {
        super(name);
    }

    public void run(Connection connection, String... args) throws InvalidArgsException, IOException {
        if (args.length != 5) throw new InvalidArgsException(args);

        // GetRow the arguments passed by the user.
        String tableName = args[0];
        String rowId = args[1];
        String columnFamily = args[2];
        String column = args[3];
        String value = args[4];

        Table table = connection.getTable(TableName.valueOf(tableName));

        // CreateTable a new Put request.
        Put put = new Put(Bytes.toBytes(rowId));

        // Here we add only one column value to the row but
        // multiple column values can be added to the row at
        // once by calling this method multiple times.
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));

        // Execute the put on the table.
        table.put(put);
    }

    public String getOptions() {
        return "TABLENAME ROWID COLUMNFAMILY COLUMN VALUE";
    }

    public String getDescription() {
        return "Put a column value";
    }
}
