package test.me.tingri.datagen.util;

import me.tingri.datagen.models.Record;
import me.tingri.datagen.util.RecordGenerator;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by skunkunuru on 8/27/2016.
 */
public class RecordGeneratorTest {
    @Test
    public void records() throws Exception {
        Record[] records = RecordGenerator.records("conversions,impressions,channel", ",", 10);

        for (Record record: records){
            System.out.println(record.toString());
            assertNotNull("record" , record);
        }
    }

}