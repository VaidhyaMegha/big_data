package test.me.tingri.big_data.hbase.client;

import me.tingri.big_data.hbase.client.Client;
import me.tingri.big_data.hbase.client.COMMANDS;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.junit.Test;

/**
 * Created by sandeepkunkunuru on 7/29/16.
 */
public class CommandsTest {
    @Test
    public void complete() throws Exception {
        try {
            String tablename = "test3";

            Configuration conf = HBaseConfiguration.create();
            Connection conn = ConnectionFactory.createConnection();

            COMMANDS.CREATE.run(conn, tablename, "grade", "course");

            // add record zkb
            COMMANDS.PUT.run(conn, tablename, "zkb", "grade", "", "5");
            COMMANDS.PUT.run(conn, tablename, "zkb", "course", "", "90");
            COMMANDS.PUT.run(conn, tablename, "zkb", "course", "math", "97");
            COMMANDS.PUT.run(conn, tablename, "zkb", "course", "art", "87");
            // add record baoniu
            COMMANDS.PUT.run(conn, tablename, "baoniu", "grade", "", "4");
            COMMANDS.PUT.run(conn, tablename, "baoniu", "course", "math", "89");

            System.out.println("===========get one record========");
            COMMANDS.GET.run(conn, tablename, "zkb");

            System.out.println("===========show all record========");
            Client.getAllRecord(conf, tablename);

            System.out.println("===========del one record========");
            Client.delRecord(conf, tablename, "baoniu");
            Client.getAllRecord(conf, tablename);

            System.out.println("===========show all record========");
            Client.getAllRecord(conf, tablename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}