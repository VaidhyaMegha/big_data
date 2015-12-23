package me.tingri.graphs.cc;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

/**
 * Created by sandeep on 12/23/15.
 */
public class SummarizeMapper extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text> {

    public void configure(JobConf jobConf) {

    }

    public void map(LongWritable longWritable, Text text, OutputCollector<LongWritable, Text> outputCollector, Reporter reporter) throws IOException {

    }
}
