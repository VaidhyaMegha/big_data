package me.tingri.graphs.cc;

import me.tingri.util.CONSTANTS;
import me.tingri.util.FLAGS;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by sandeep on 12/23/15.
 */
public class StateCheckReducer extends MapReduceBase implements Reducer<LongWritable, Text,LongWritable,Text> {
    private String vectorIndicator;

    public void configure(JobConf conf) {
        vectorIndicator = conf.get(CONSTANTS.VECTOR_INDICATOR);
    }

    public void reduce(LongWritable key, Iterator<Text> values, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
        long curNodeId = -1;

        while (values.hasNext()) {
            long nodeId = Long.parseLong(values.next().toString().substring(1));

            curNodeId = (curNodeId == -1 || nodeId == curNodeId) ? nodeId : curNodeId;

            if(curNodeId != nodeId) {
                reporter.getCounter(FLAGS.CHANGED).increment(1);
                break;
            }
        }
    }
}
