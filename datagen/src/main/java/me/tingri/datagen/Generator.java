package me.tingri.datagen;

import me.tingri.big_data.util.Utility;
import me.tingri.datagen.models.Record;
import me.tingri.datagen.util.Constants;
import me.tingri.datagen.util.Dictionary;
import me.tingri.datagen.util.RecordGenerator;
import org.kohsuke.args4j.Option;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static me.tingri.datagen.util.Constants.*;

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
    private long numOfRecords;

    @Option(name = "-delimiter", usage = "Regular Expression to use as delimiter")
    private String delimiter;

    @Option(name = "-outputFile", usage = "Output file")
    private String outputFile;


    public static void main(String[] args) throws IOException {
        new Generator().run(args);
    }

    private void run(String[] args) throws IOException {
        if (!Utility.parseArgs(this, args)) return;

        if (dictionary != null) Dictionary.load(dictionary);

        Stream<Record> records;

        if(numOfRecords < STREAM_BARRIER) {
            Record[] recordArray = RecordGenerator.records(readFile(headerFile, Charset.forName(Constants.UTF8)), delimiter, (int) numOfRecords);
            records = Arrays.stream(recordArray);
        } else {
            records = RecordGenerator.records(readFile(headerFile, Charset.forName(Constants.UTF8)), delimiter, numOfRecords);
        }

        writeFile(records.parallel());
    }

    private void writeFile(Stream<Record> records) throws IOException {
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile), BUFFER_SIZE);) {
            records.forEach((record) -> writeRecord(bos, record));
        }
    }

    private void writeRecord(OutputStream os, Record record) {
        try {
            os.write((record.value(delimiter) + LINE_SEPARATOR).getBytes());
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
