package me.tingri.graphs.cc;

import me.tingri.graphs.gimv.JoinMapper;
import me.tingri.graphs.gimv.JoinReducer;
import me.tingri.util.CONSTANTS;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by sandeep on 12/23/15.
 */
public class ConnectedComponents extends Configured implements Tool {

    private long numOfNodes;
    private int numOfReducers;
    private int currentIteration;

    public static void main(String[] args) throws Exception {
        int result = ToolRunner.run(new Configuration(), new ConnectedComponents(), args);

        System.exit(result);
    }


    public int run(String[] strings) throws Exception {
        return 0;
    }


    protected JobConf joinConf() throws Exception {
        JobConf conf = new JobConf(getConf(), ConnectedComponents.class);
        conf.set(CONSTANTS.FIELD_SEPARATOR, CONSTANTS.DEFAULT_FIELD_SEPARATOR );
        conf.set(CONSTANTS.VECTOR_INDICATOR, CONSTANTS.DEFAULT_VECTOR_INDICATOR);
        conf.set(CONSTANTS.MAKE_SYMMETRIC, CONSTANTS.DEFAULT_MAKE_SYMMETRIC);

        conf.setJobName("ConnectedComponents_Join");

        conf.setMapperClass(JoinMapper.class);
        conf.setReducerClass(JoinReducer.class);

        FileInputFormat.setInputPaths(conf, CONSTANTS.DEFAULT_EDGE_PATH, CONSTANTS.DEFAULT_VECTOR_PATH);
        FileOutputFormat.setOutputPath(conf, CONSTANTS.DEFAULT_TEMP_JOIN_OUTPUT_PATH);

        conf.setNumReduceTasks(numOfReducers);

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);

        return conf;
    }
}