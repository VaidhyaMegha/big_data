package me.tingri.qcf;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by sandeep on 4/23/16.
 */
public class Utility {

    static boolean parseArgs(Object o, String[] args) {
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

    static <T> T time(Callable<T> task, List<Double> timings) {
        T call = null;
        try {
            long startTime = System.currentTimeMillis();
            call = task.call();
            timings.add((System.currentTimeMillis() - startTime) / 1000d);
        } catch (Exception e) {
            //...
        }
        return call;
    }
}
