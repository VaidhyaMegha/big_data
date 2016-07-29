package me.tingri.big_data.hbase.client;

import me.tingri.big_data.hbase.client.commands.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sandeepkunkunuru on 7/29/16.
 */
public class COMMANDS {
    public static final Command CREATE;
    public static final Command SCAN;
    public static final Command GET;
    public static final Command PUT;
    public static final Command LIST;

    static final HashMap<String, Command> COMMANDS = new HashMap<String, Command>();

    static {
        // CreateTable a list of commands that are supported. Each
        // command defines a run method and some methods for
        // printing help.
        // See the definition of each command below.
        COMMANDS.put("create", new CreateTable("create"));
        COMMANDS.put("scan", new ScanTable("scan"));
        COMMANDS.put("get", new GetRow("get"));
        COMMANDS.put("put", new PutValue("put"));
        COMMANDS.put("list", new ListTables("list"));

        CREATE = COMMANDS.get("create");
        SCAN = COMMANDS.get("scan");
        GET = COMMANDS.get("get");
        PUT = COMMANDS.get("put");
        LIST = COMMANDS.get("list");
    }

    public static Command getCommand(String commandName) {
        return COMMANDS.get(commandName);
    }

    public static Map<String, Command> getAllCommands(){
        return (Map<String, Command>) COMMANDS.clone();
    }

}
