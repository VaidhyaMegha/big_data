package test.me.tingri.qcf;


import me.tingri.qcf.NFA;
import org.junit.Test;

/**
 * Created by sandeepkunkunuru on 5/14/16.
 */
public class NFATest {

    @Test
    public void recognizesText() throws Exception {
        String regexp = "(ab)";
        String txt = "ab";
        NFA nfa = new NFA(regexp);

        System.out.println(nfa.toString());
        assert nfa.recognizes(txt);
    }

    @Test
    public void recognizesAny() throws Exception {
        String regexp = "(.*)";
        String txt = "ab";
        NFA nfa = new NFA(regexp);

        System.out.println(nfa.toString());
        assert nfa.recognizes(txt);
    }

}