package me.tingri.graphs.cc;

import me.tingri.util.FLAGS;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by sandeep on 12/23/15.
 * No explicit output is generated. As placeholders for future need LongWritable and Text are specified.
 */
public class StateCheckReducer extends MapReduceBase implements Reducer<LongWritable, Text, LongWritable, Text> {
    public void reduce(LongWritable key, Iterator<Text> values, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
        long curNodeId = -1;

        while (values.hasNext()) {
            long nodeId = Long.parseLong(values.next().toString().substring(1));

            curNodeId = (curNodeId == -1 || nodeId == curNodeId) ? nodeId : curNodeId;

            if (curNodeId != nodeId) {
                reporter.incrCounter(FLAGS.CHANGED, 1);
                break;
            }
        }
    }
}
