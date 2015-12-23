package me.tingri.graphs.gimv;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by sandeep on 12/23/15.
 */
public class JoinReducer extends MapReduceBase implements Reducer<LongWritable, Text,LongWritable,Text> {

    public void configure(JobConf jobConf) {

    }

    public void reduce(LongWritable key, Iterator<Text> iter, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {

    }
}
