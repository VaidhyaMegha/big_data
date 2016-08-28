package me.tingri.big_data.util;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by sandeep on 4/23/16.
 */
public class Utility {

    public static boolean parseArgs(Object o, String[] args) {
        CmdLineParser cmdParser = new CmdLineParser(o);

        try {
            // parse the options.
            cmdParser.parseArgument(args);
        } catch (CmdLineException e) {
            cmdParser.printUsage(System.err);
            System.err.println();
            return false;
        }

        return true;
    }
}
