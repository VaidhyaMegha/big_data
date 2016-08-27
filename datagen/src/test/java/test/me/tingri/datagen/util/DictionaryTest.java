package test.me.tingri.datagen.util;

import org.junit.Test;
import me.tingri.datagen.util.Dictionary;

import static org.junit.Assert.*;

/**
 * Created by skunkunuru on 8/27/2016.
 */
public class DictionaryTest {
    @Test
    public void testGet() throws Exception {
        System.out.println(Dictionary.get("conversions"));
        assertNotNull(Dictionary.get("conversions"));
    }

}