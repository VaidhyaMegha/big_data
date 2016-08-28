package me.tingri.qcf;

/**
 * Created by sandeep on 5/2/16.
 */

import me.tingri.big_data.util.TimeIt;
import me.tingri.big_data.util.Utility;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;


public class QCF {
    @Option(name = "-inputFile", usage = "Input .gz file")
    private String inputFile;

    @Option(name = "-outputFile", usage = "Output file")
    private String outputFile;

    @Option(name = "-numOfTrials", usage = "Number of trials")
    private int numOfTrials;

    @Option(name = "-regExp", usage = "Regular Expression to search")
    private String regExp;

    public static void main(String[] args) throws IOException {
        new QCF().run(args);
    }

    public double run(String[] args) throws IOException {
        if (!Utility.parseArgs(this, args)) return -1.0;

        System.out.println("Input File = " + inputFile);
        System.out.println("Output File = " + outputFile);

        return benchmark(regExp);
    }

    private double benchmark(String regexp) throws IOException {
        List<Double> timings1 = new ArrayList<>();
        List<Double> timings2 = new ArrayList<>();

        unzip();

        for (int i = 0; i < numOfTrials; i++) {
            List<String> result1 = TimeIt.time(() -> searchUncompressedFile(regexp), timings1);
            List<String> result2 = TimeIt.time(() -> searchCompressedFile(regexp), timings2);

            System.out.println("First Result in uncompressed File : " + result1.get(0) );
            System.out.println("First Result in compressed File : " + result2.get(0) );
            System.out.println("Time in uncompressed File : " + timings1.get(i) );
            System.out.println("Time in compressed File : " + timings2.get(i) );
        }

        return compare(timings1, timings2);
    }

    private double compare(List<Double> timings1, List<Double> timings2) {
        double diff = 0;

        for (int i = 0; i < numOfTrials; i++) diff += (timings1.get(i) - timings2.get(i));

        return diff / numOfTrials;
    }

    private List<String> searchCompressedFile(String regexp) throws IOException {
        try(Reader r = readCompressedFile()) {
            return search(regexp, r);
        }
    }

    private List<String> searchUncompressedFile(String regexp) throws IOException {
        try(FileReader fr = new FileReader(outputFile)) {
            return search(regexp, fr);
        }
    }

    private List<String> search(String regexp, Reader r) throws IOException {
        return GREP.search(regexp, r);
    }

    private Reader readCompressedFile() throws IOException {
        return new InputStreamReader(new GZIPInputStream(new FileInputStream(inputFile)));
    }

    private void unzip() throws IOException {
        byte[] buffer = new byte[1024];

        // https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (
                GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(inputFile));
                FileOutputStream out = new FileOutputStream(outputFile)
        ) {
            int len;
            while ((len = gzis.read(buffer)) > 0)
                out.write(buffer, 0, len);
        }
    }
}
