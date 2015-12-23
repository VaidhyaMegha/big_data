package me.tingri.graphs.gimv;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by sandeep on 12/23/15.
 */
public class MergeReducer extends MapReduceBase implements Reducer<LongWritable, Text,LongWritable,Text> {

    public void configure(JobConf jobConf) {

    }

    public void reduce(LongWritable longWritable, Iterator<Text> iterator, OutputCollector<LongWritable, Text> outputCollector, Reporter reporter) throws IOException {

    }
}
