package me.tingri.big_data.hbase.client.commands;

import org.apache.hadoop.hbase.client.Connection;

import java.io.IOException;

/**
 * Defines the interface for commands that can be run by the CLI
 */
public abstract class Command {
    private String name;

    public Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Run the command.
     * @param connection The HBase/Cloud Bigtable connection object.
     * @param args A list of args to the command.
     */
    public abstract void run(Connection connection, String... args) throws InvalidArgsException, IOException;

    /**
     * Gets a string describing command line arguments of the command.
     * Used when printing usage and help.
     */
    public abstract String getOptions();

    /**
     * Gets a string describing what the command does.
     * Used when printing usage and help.
     */
    public abstract String getDescription();
}
