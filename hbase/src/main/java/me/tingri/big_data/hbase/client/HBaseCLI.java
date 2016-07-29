package me.tingri.big_data.hbase.client;

/**
 * Created by sandeepkunkunuru on 7/29/16.
 *
 * Rewrite of https://github.com/GoogleCloudPlatform/cloud-bigtable-examples/tree/master/java/simple-cli
 */

import me.tingri.big_data.hbase.client.commands.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;

import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Connection;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * A simple command line interface to Cloud Bigtable using
 * the native HBase API. This application does not use anything
 * that is specific to Cloud Bigtable so it should work on a
 * normal HBase installation as well.
 *
 * This is not meant to be a full featured command line interface
 * or shell but to illustrate how to do some simple operations
 * on Cloud Bigtable using the HBase native API.
 */
public class HBaseCLI {

    /**
     * The main method for the CLI. This method takes the command line
     * arguments and runs the appropriate commands.
     */
    public static void main(String[] args) {
        // We use Apache commons-cli to check for a help option.
        Options options = new Options();
        Option help = new Option("help", "print this message");
        options.addOption(help);

        // create the parser
        CommandLineParser parser = new BasicParser();
        CommandLine line = null;
        try {
            // parse the command line arguments
            line = parser.parse(options, args);
        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println(exp.getMessage());
            usage();
            System.exit(1);
        }

        Command command = null;

        if (args.length > 0) command = COMMANDS.getCommand(args[0]);

        // Check for the help option and if it's there
        // display the appropriate help.
        if (line.hasOption("help")) {
            // If there is a command listed (e.g. create -help)
            // then show the help for that command
            if (command == null) {
                help(COMMANDS.getAllCommands().values());
            } else {
                help(command);
            }
            System.exit(0);
        } else if (command == null) {
            // No valid command was given so print the usage.
            usage();
            System.exit(0);
        }

        try {

            try (Connection connection = ConnectionFactory.createConnection()) {
                try {
                    // Run the command with the arguments after the command name.
                    command.run(connection, Arrays.copyOfRange(args, 1, args.length));
                } catch (InvalidArgsException e) {
                    System.out.println("ERROR: Invalid arguments");
                    usage(command);
                    System.exit(0);
                }
            }
            // Make sure the connection is closed even if
            // an exception occurs.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print the usage for the program.
     */
    public static void usage() {
        System.out.println("Usage: hbasecli.sh COMMAND [OPTIONS ...]");
        System.out.println("Try hbasecli.sh -help for more details.");
    }

    /**
     * Print the usage for a specific command.
     * @param command The command whose usage you want to print.
     */
    public static void usage(Command command) {
        System.out.println("Usage: ./hbasecli.sh " + command.getName() + " " + command.getOptions());
    }

    /**
     * Print the program help message. Includes a list of the supported commands.
     * @param commands A collection of the available commands.
     */
    public static void help(Collection<Command> commands) {
        usage();
        System.out.println("");
        System.out.println("Commands:");
        System.out.println("");
        for (Command command : commands) {
            System.out.println("    " + command.getName() + ": " + command.getDescription());
        }
        System.out.println("");
        System.out.println("Try hbasecli.sh COMMAND -help for command usage.");
    }

    /**
     * Prints the help for a specific command.
     * @param command The command whose help you want to print.
     */
    public static void help(Command command) {
        usage(command);
        System.out.println(command.getDescription());
    }

}
