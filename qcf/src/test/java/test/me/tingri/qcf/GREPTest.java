package test.me.tingri.qcf;

import me.tingri.qcf.GREP;
import org.junit.Test;

import java.io.StringReader;


/**
 * Created by sandeepkunkunuru on 5/15/16.
 */
public class GREPTest {
    @Test
    public void search() throws Exception {
        String sample = "abcdef";
        String regExp = "(ab)";

        assert GREP.search(regExp, new StringReader(sample)) != null;
    }

}