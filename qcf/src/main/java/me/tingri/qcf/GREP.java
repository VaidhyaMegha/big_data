package me.tingri.qcf;

import edu.princeton.cs.algs4.NFA;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class GREP {

    // do not instantiate
    private GREP() {
    }

    public static void main(String[] args) {
        String regexp = "(.*" + args[0] + ".*)";
        NFA nfa = new NFA(regexp);
        while (StdIn.hasNextLine()) {
            String line = StdIn.readLine();
            if (nfa.recognizes(line)) {
                StdOut.println(line);
            }
        }
    }
}
