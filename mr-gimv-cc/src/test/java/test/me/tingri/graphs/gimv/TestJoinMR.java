package test.me.tingri.graphs.gimv;


import me.tingri.graphs.gimv.JoinMapper;
import me.tingri.graphs.gimv.JoinReducer;
import me.tingri.util.CONSTANTS;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.MapDriver;
import org.apache.hadoop.mrunit.MapReduceDriver;
import org.apache.hadoop.mrunit.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TestJoinMR {
    MapDriver<LongWritable, Text, LongWritable,Text> mapDriver;
    ReduceDriver<LongWritable, Text,LongWritable,Text> reduceDriver;
    MapReduceDriver<LongWritable, Text, LongWritable, Text, LongWritable, Text> mapReduceDriver;

    @Before
    public void setUp() {
        JoinMapper mapper = new JoinMapper();
        JoinReducer reducer = new JoinReducer();

        mapDriver = MapDriver.newMapDriver(mapper);
        reduceDriver = ReduceDriver.newReduceDriver(reducer);
        mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);

        mapDriver.getConfiguration().set(CONSTANTS.MAKE_SYMMETRIC,"1");
        mapDriver.getConfiguration().set(CONSTANTS.FIELD_SEPARATOR,CONSTANTS.DEFAULT_FIELD_SEPARATOR);
        mapDriver.getConfiguration().set(CONSTANTS.VECTOR_INDICATOR,CONSTANTS.DEFAULT_VECTOR_INDICATOR);


        reduceDriver.getConfiguration().set(CONSTANTS.MAKE_SYMMETRIC,"1");
        reduceDriver.getConfiguration().set(CONSTANTS.FIELD_SEPARATOR,CONSTANTS.DEFAULT_FIELD_SEPARATOR);
        reduceDriver.getConfiguration().set(CONSTANTS.VECTOR_INDICATOR,CONSTANTS.DEFAULT_VECTOR_INDICATOR);

        mapReduceDriver.getConfiguration().set(CONSTANTS.MAKE_SYMMETRIC,"1");
        mapReduceDriver.getConfiguration().set(CONSTANTS.FIELD_SEPARATOR,CONSTANTS.DEFAULT_FIELD_SEPARATOR);
        mapReduceDriver.getConfiguration().set(CONSTANTS.VECTOR_INDICATOR,CONSTANTS.DEFAULT_VECTOR_INDICATOR);
    }

    @Test
    public void testMapper() throws IOException {
        mapDriver.withAll(TESTDATA.getMapperInput());

        mapDriver.withAllOutput(TESTDATA.getMapperOutput());

        //though code is deterministic it is not necessary to function. Hence accepting results in any order.
        mapDriver.runTest(false);
    }

    @Test
    public void testReducer() throws IOException {

        reduceDriver.withAll(TESTDATA.getReducerInput());

        reduceDriver.withAllOutput(TESTDATA.getReducerOutput());

        //since HashSet is usedm results are non-deterministic. Hence order does not matter
        reduceDriver.runTest(false);
    }


    @Test
    public void testMapReduce() throws IOException {
        mapReduceDriver.withAll(TESTDATA.getMapperInput());

        mapReduceDriver.withAllOutput(TESTDATA.getReducerOutput());

        //since HashSet is used in reducer, results are non-deterministic. Hence order does not matter
        mapReduceDriver.runTest(false);
    }

}
