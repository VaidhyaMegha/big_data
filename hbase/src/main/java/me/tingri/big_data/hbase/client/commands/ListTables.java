package me.tingri.big_data.hbase.client.commands;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

import java.io.IOException;

/**
 * This command lists the tables in your Cloud Bigtable Cluster
 * It also accepts a glob pattern argument which can be used to limit
 * the number of tables listed.
 */
public class ListTables extends Command {

    public ListTables(String name) {
        super(name);
    }

    public void run(Connection connection, String... args) throws InvalidArgsException, IOException {
        String pattern = null;

        if (args.length == 1) pattern = args[0];
        else if (args.length != 0) throw new InvalidArgsException(args);

        Admin admin = connection.getAdmin();
        HTableDescriptor[] tables;

        // We use the listTables() method on the Admin instance
        // to get a list of HTableDescriptor objects.
        if (pattern != null) {
            tables = admin.listTables(pattern);
        } else {
            tables = admin.listTables();
        }

        // For each of the tables we get the table name and column families
        // registered with the table, and print them out.
        for (HTableDescriptor table : tables) {
            HColumnDescriptor[] columnFamilies = table.getColumnFamilies();
            String columnFamilyNames = "";
            for (HColumnDescriptor columnFamily : columnFamilies) {
                columnFamilyNames += columnFamily.getNameAsString() + ",";
            }
            if (columnFamilyNames.length() > 0) {
                columnFamilyNames = " <" + columnFamilyNames.substring(0, columnFamilyNames.length()) + ">";
            }

            System.out.println(table.getTableName() + columnFamilyNames);
        }
    }

    public String getOptions() {
        return "[TABLENAME PATTERN]";
    }

    public String getDescription() {
        return "List tables";
    }
}
