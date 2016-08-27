package me.tingri.datagen;

import me.tingri.datagen.models.Record;
import me.tingri.datagen.util.Dictionary;
import me.tingri.datagen.util.RecordGenerator;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Hello world!
 */
public class Generator {
    @Option(name = "-dictionary", usage = "Optional Dictionary file containing types specified exactly in the below format " +
            "impressions={\"type\" : \"int\", \"length\" : \"12\", \"characters\": \"0123456789\"}")
    private String dictionary;

    @Option(name = "-headerFile", usage = "Input header file")
    private String headerFile;

    @Option(name = "-numOfRecords", usage = "Number of Records")
    private int numOfRecords;

    @Option(name = "-delimiter", usage = "Regular Expression to use as delimiter")
    private String delimiter;

    @Option(name = "-outputFile", usage = "Output file")
    private String outputFile;


    public static void main(String[] args) throws IOException {
        new Generator().run(args);
    }

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

    private void run(String[] args) throws IOException {
        if (!parseArgs(this, args)) return;

        if (dictionary != null) Dictionary.load(dictionary);

        Record[] records = RecordGenerator.records(readFile(headerFile, Charset.forName("UTF-8")), delimiter, numOfRecords);

        writeFile(records);
    }

    private void writeFile(Record[] records) throws IOException {
        try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));) {
            for (Record record : records) pw.println(record.value(delimiter));
        }
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
