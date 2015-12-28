package me.tingri.graphs.gimv;

import me.tingri.util.CONSTANTS;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 * // STAGE 2: merge partial component ids.
 * //  - Input: partial component ids
 * //  - Output: combined component ids
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 *
 *  Heavy rewrite of original source code
 * Created by Sandeep Kunkunuru on 12/23/15.
 */
public class MergeReducer extends MapReduceBase implements Reducer<LongWritable, Text, LongWritable, Text> {
    private String vectorIndicator;

    public void configure(JobConf conf) {
        vectorIndicator = conf.get(CONSTANTS.VECTOR_INDICATOR);
    }

    public void reduce(LongWritable key, Iterator<Text> values, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
        long curMinNodeId = -1;

        while (values.hasNext()) {
            long nodeId = Long.parseLong(values.next().toString().substring(1));

            curMinNodeId = (curMinNodeId == -1 || nodeId < curMinNodeId) ? nodeId : curMinNodeId;
        }

        output.collect(key, new Text(vectorIndicator + Long.toString(curMinNodeId)));
    }
}
