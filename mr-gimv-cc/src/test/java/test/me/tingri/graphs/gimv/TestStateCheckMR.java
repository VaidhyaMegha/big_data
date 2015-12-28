package test.me.tingri.graphs.gimv;


import me.tingri.graphs.cc.StateCheckMapper;
import me.tingri.graphs.cc.StateCheckReducer;
import me.tingri.util.CONSTANTS;
import me.tingri.util.FLAGS;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.MapDriver;
import org.apache.hadoop.mrunit.MapReduceDriver;
import org.apache.hadoop.mrunit.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestStateCheckMR {
    MapDriver<LongWritable, Text, LongWritable,Text> mapDriver;
    ReduceDriver<LongWritable, Text,LongWritable,Text> reduceDriver;
    MapReduceDriver<LongWritable, Text, LongWritable, Text, LongWritable, Text> mapReduceDriver;

    @Before
    public void setUp() {
        StateCheckMapper mapper = new StateCheckMapper();
        StateCheckReducer reducer = new StateCheckReducer();

        mapDriver = MapDriver.newMapDriver(mapper);
        reduceDriver = ReduceDriver.newReduceDriver(reducer);
        mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);

        mapDriver.getConfiguration().set(CONSTANTS.VECTOR_INDICATOR,CONSTANTS.DEFAULT_VECTOR_INDICATOR);
        reduceDriver.getConfiguration().set(CONSTANTS.VECTOR_INDICATOR,CONSTANTS.DEFAULT_VECTOR_INDICATOR);
        mapReduceDriver.getConfiguration().set(CONSTANTS.VECTOR_INDICATOR,CONSTANTS.DEFAULT_VECTOR_INDICATOR);
    }

    @Test
    public void testMapper() throws IOException {
        mapDriver.withAll(TESTDATA.getStateCheckMapperInput());

        mapDriver.withAllOutput(TESTDATA.getStateCheckerMapperOutput());

        //though code is deterministic it is not necessary to function. Hence accepting results in any order.
        mapDriver.runTest(false);
    }

    @Test
    public void testReducer() throws IOException {

        reduceDriver.withAll(TESTDATA.getStateCheckReducerInput());

        reduceDriver.withAllOutput(TESTDATA.getStateCheckReducerOutput());

        //though code is deterministic it is not necessary to function. Hence accepting results in any order.
        reduceDriver.runTest(false);
        assertEquals("Expected 3 counter increments", "" + 3, "" + (reduceDriver.getCounters().findCounter(FLAGS.CHANGED).getValue()));
    }


    @Test
    public void testMapReduce() throws IOException {
        mapReduceDriver.withAll(TESTDATA.getStateCheckMapperInput());

        mapReduceDriver.withAllOutput(TESTDATA.getStateCheckReducerOutput());

        //though code is deterministic it is not necessary to function. Hence accepting results in any order.
        mapReduceDriver.runTest(false);
        assertEquals("Expected 3 counter increments", "" + 3, "" + (mapReduceDriver.getCounters().findCounter(FLAGS.CHANGED).getValue()));
    }

}
