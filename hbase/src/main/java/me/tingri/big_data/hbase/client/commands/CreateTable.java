package me.tingri.big_data.hbase.client.commands;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This command creates a new Bigtable table. It uses the
 * HBase Admin class to create the table based on a
 * HTableDescriptior.
 */
public class CreateTable extends Command {

    public CreateTable(String name) {
        super(name);
    }

    public void run(Connection connection, String... args) throws InvalidArgsException, IOException {
        if (args.length < 1) throw new InvalidArgsException(args);

        ArrayList<String> columnFamilies = new ArrayList<>();
        if (args.length > 1)
            columnFamilies.addAll(Arrays.asList(args).subList(1, args.length));

        TableName tableName = TableName.valueOf(args[0]);

        // CreateTable the table based on the passed in arguments.
        // We used the standard HBase Admin and HTableDescriptor classes.
        Admin admin = connection.getAdmin();

        if (admin.tableExists(tableName)) System.out.println("table already exists!");
        else {
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

            for (String colFamily : columnFamilies)
                tableDescriptor.addFamily(new HColumnDescriptor(colFamily));

            admin.createTable(tableDescriptor);

            System.out.println("create table " + tableName + " ok.");
        }

    }

    public String getOptions() {
        return "TABLENAME [COLUMNFAMILY ...]";
    }

    public String getDescription() {
        return "CreateTable a new table";
    }
}
