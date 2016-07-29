package me.tingri.big_data.hbase.client.commands;

/**
 * An exception that is thrown when invalid arguments are
 * passed to a command.
 */
public class InvalidArgsException extends Exception {
    private String[] args;

    public InvalidArgsException(String[] args) {
        this.args = args;
    }

    public String[] getArgs() {
        return this.args;
    }
}
