package me.tingri.qcf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class GREP {

    // do not instantiate
    private GREP() {
    }

    public static List<String> search(String regex, Reader r) throws IOException {
        String regexp = "(.*" + regex + ".*)";
        BufferedReader bfr = new BufferedReader(r);
        NFA nfa = new NFA(regexp);
        List<String> matches = new ArrayList<>();
        String line;

        while ((line = bfr.readLine()) != null)
            if (nfa.recognizes(line)) matches.add(line);

        return matches;
    }
}
