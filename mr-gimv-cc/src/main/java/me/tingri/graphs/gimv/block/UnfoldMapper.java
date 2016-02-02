package me.tingri.graphs.gimv.block;

import me.tingri.util.CONSTANTS;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

/**
 * Created by sandeep on 2/2/16.
 * STAGE 4: Unfold the block component id format to plain format, after the bitstrings converged.
 * This is a map-only stage.
 * - Input: the converged component ids
 * - Output: (node_id, "msu"component_id)
 */
public class UnfoldMapper extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text> {
    int block_width;

    public void configure(JobConf job) {
        block_width = Integer.parseInt(job.get("block_width"));
    }

    /**
     * input sample : 1    msu0 1 1 1
     *
     * @param key
     * @param value
     * @param output
     * @param reporter
     * @throws IOException
     */
    public void map(LongWritable key, Text value, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
        String[] line = value.toString().split(CONSTANTS.DEFAULT_FIELD_SEPARATOR);
        String[] tokens = line[1].substring(3).split(CONSTANTS.SPACE);
        long block_id = Long.parseLong(line[0]);

        for (int i = 0; i < tokens.length; i += 2) {
            long elem_row = Long.parseLong(tokens[i]);
            long component_id = Long.parseLong(tokens[i + 1]);

            output.collect(new LongWritable(block_width * block_id + elem_row), new Text("msf" + component_id));
        }
    }
}

