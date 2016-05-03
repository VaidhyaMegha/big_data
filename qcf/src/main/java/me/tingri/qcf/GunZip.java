package me.tingri.qcf;

/**
 * Created by sandeep on 5/2/16.
 */

import org.kohsuke.args4j.Option;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;


public class GunZip {
    @Option(name = "-inputFile", usage = "Input .gz file")
    private String inputFile;

    @Option(name = "-outputFile", usage = "Output file")
    private String outputFile;

    public static void main(String[] args) {
        new GunZip().run(args);
    }

    void run(String[] args) {
        if (!Utility.parseArgs(this, args)) return;

        System.out.println("Input File = " + inputFile);
        System.out.println("Output File = " + outputFile);

        unzip();
    }

    void unzip() {
        byte[] buffer = new byte[1024];

        // https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (
            BufferedInputStream bfis = new BufferedInputStream(new FileInputStream(inputFile));
            GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(inputFile));
            FileOutputStream out = new FileOutputStream(outputFile);
        ) {
            int len;
            while ((len = gzis.read(buffer)) > 0)
                out.write(buffer, 0, len); //System.out.write(buffer, 0, len);

            while ((len = bfis.read(buffer)) > 0)
                System.out.write(buffer, 0, len);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
