package test.me.tingri.graphs.gimv.block;


import me.tingri.graphs.gimv.block.MergeMapper;
import me.tingri.graphs.gimv.block.MergeReducer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.MapDriver;
import org.apache.hadoop.mrunit.MapReduceDriver;
import org.apache.hadoop.mrunit.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static me.tingri.util.CONSTANTS.BLOCK_WIDTH;
import static me.tingri.util.CONSTANTS.DEFAULT_VECTOR_INDICATOR;
import static me.tingri.util.CONSTANTS.VECTOR_INDICATOR;
import static test.me.tingri.graphs.gimv.TESTDATA.*;

public class TestMergeMR {
    MapDriver<LongWritable, Text, LongWritable,Text> mapDriver;
    ReduceDriver<LongWritable, Text,LongWritable,Text> reduceDriver;
    MapReduceDriver<LongWritable, Text, LongWritable, Text, LongWritable, Text> mapReduceDriver;

    @Before
    public void setUp() {
        MergeMapper mapper = new MergeMapper();
        MergeReducer reducer = new MergeReducer();

        mapDriver = MapDriver.newMapDriver(mapper);
        reduceDriver = ReduceDriver.newReduceDriver(reducer);
        mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);

        mapDriver.getConfiguration().set(VECTOR_INDICATOR, DEFAULT_VECTOR_INDICATOR);
        mapDriver.getConfiguration().set(BLOCK_WIDTH, DEFAULT_BLOCK_WIDTH);

        reduceDriver.getConfiguration().set(VECTOR_INDICATOR, DEFAULT_VECTOR_INDICATOR);
        reduceDriver.getConfiguration().set(BLOCK_WIDTH, DEFAULT_BLOCK_WIDTH);

        mapReduceDriver.getConfiguration().set(VECTOR_INDICATOR, DEFAULT_VECTOR_INDICATOR);
        mapReduceDriver.getConfiguration().set(BLOCK_WIDTH, DEFAULT_BLOCK_WIDTH);
    }

    @Test
    public void testMapper() throws IOException {
        testGIMVIdentityMapper();
    }

    @Test
    public void testGIMVIdentityMapper() throws IOException {
        mapDriver.withAll(getBlockMergeMapperInput());

        mapDriver.withAllOutput(getBlockMergeMapperOutput());

        //though code is deterministic it is not necessary to function. Hence accepting results in any order.
        mapDriver.runTest(false);
    }


    @Test
    public void testReducer() throws IOException {

        reduceDriver.withAll(getBlockMergeReducerInput());

        reduceDriver.withAllOutput(getBlockMergeReducerOutput());

        //though code is deterministic it is not necessary to function. Hence accepting results in any order.
        reduceDriver.runTest(false);
    }

    @Test
    public void testMapReduce() throws IOException {
        mapReduceDriver.withAll(getBlockMergeMapperInput());

        mapReduceDriver.withAllOutput(getBlockMergeReducerOutput());

        //though code is deterministic it is not necessary to function. Hence accepting results in any order.
        mapReduceDriver.runTest(false);
    }

}
