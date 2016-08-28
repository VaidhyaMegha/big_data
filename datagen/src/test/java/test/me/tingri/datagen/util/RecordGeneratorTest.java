package test.me.tingri.datagen.util;

import me.tingri.big_data.util.TimeIt;
import me.tingri.datagen.models.Record;
import me.tingri.datagen.util.RecordGenerator;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Created by skunkunuru on 8/27/2016.
 */
public class RecordGeneratorTest {
    @Test
    public void records1() throws Exception {
        Stream<Record> parallelStream = TimeIt.time(() -> testGenerateParallelRecordStream());
    }

    @Test
    public void records2() throws Exception {
        Stream<Record> recordStream = TimeIt.time(() -> testGenerateRecordStream());
    }

    @Test
    public void records3() throws Exception {
         Record[] records = TimeIt.time(() -> testGenerateRecords());
    }

    public Record[] testGenerateRecords() {
        Record[] records = RecordGenerator.records("conversions,impressions,channel", ",", 100000);

        for (Record record: records){
            assertNotNull("record" , record);
        }

        return records;
    }

    public Stream<Record> testGenerateRecordStream() {
        Stream<Record> records = RecordGenerator.records("conversions,impressions,channel", ",", 100000L);

        records.forEach((record) -> {
            assertNotNull("record" , record);
        });

        return records;
    }

    public Stream<Record> testGenerateParallelRecordStream() {
        Stream<Record> records = RecordGenerator.records("conversions,impressions,channel", ",", 100000L);

        records.parallel().forEach((record) -> {
            assertNotNull("record" , record);
        });

        return records;
    }
}