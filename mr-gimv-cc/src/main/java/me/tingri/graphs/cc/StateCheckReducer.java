package me.tingri.graphs.cc;

import me.tingri.util.FLAGS;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;

import static me.tingri.util.CONSTANTS.VECTOR_INDICATOR;

/**
 * Created by sandeep on 12/23/15.
 * No explicit output is generated. As placeholders for future need LongWritable and Text are specified.
 */
public class StateCheckReducer extends MapReduceBase implements Reducer<LongWritable, Text, LongWritable, Text> {
    private String vectorIndicator;

    public void configure(JobConf conf) {
        vectorIndicator = conf.get(VECTOR_INDICATOR);
    }

    public void reduce(LongWritable key, Iterator<Text> values, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
        long id1 = Long.parseLong(values.next().toString().substring(1));
        long id2 = Long.parseLong(values.next().toString().substring(1));

        if(id1 != id2){
            output.collect(key, new Text(vectorIndicator +  (id1 < id2 ? id1 : id2)));
            reporter.incrCounter(FLAGS.CHANGED, 1);
        }
    }
}
