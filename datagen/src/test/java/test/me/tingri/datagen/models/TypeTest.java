package test.me.tingri.datagen.models;

import me.tingri.datagen.util.Dictionary;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by skunkunuru on 8/27/2016.
 */
public class TypeTest {
    @Test
    public void testToString() throws Exception {
        String value = Dictionary.get("conversions").toString();
        System.out.println(value);
        assertNotNull(value, value);
    }

    @Test
    public void testGet() throws Exception {
        String value = Dictionary.get("conversions").get("type");
        System.out.println(value);
        assertNotNull(value, value);

        value = Dictionary.get("conversions").get("length");
        System.out.println(value);
        assertNotNull(value, value);

        value = Dictionary.get("conversions").get("characters");
        System.out.println(value);
        assertNotNull(value, value);
    }

}