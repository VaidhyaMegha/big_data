package me.tingri.graphs.gimv;

import me.tingri.util.CONSTANTS;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;

/**
 * Created by sandeep on 12/23/15.
 */
public class GIMVIdentityMapper extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text> {
    // Identity mapper
    public void map(LongWritable key, Text value, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
        String[] line = value.toString().split(CONSTANTS.DEFAULT_FIELD_SEPARATOR);

        output.collect(new LongWritable(Long.parseLong(line[0])), new Text(line[1]));
    }
}
