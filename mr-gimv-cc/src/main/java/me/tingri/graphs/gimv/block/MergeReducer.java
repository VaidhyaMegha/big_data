package me.tingri.graphs.gimv.block;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static me.tingri.graphs.gimv.block.GIMVUtility.*;
import static me.tingri.util.CONSTANTS.*;

/**
 * Created by sandeep on 2/3/16.
 ////////////////////////////////////////////////////////////////////////////////////////////////
// STAGE 2: merge partial comonent ids.
//  - Input: partial component ids
//  - Output: combined component ids
////////////////////////////////////////////////////////////////////////////////////////////////
 */
public class MergeReducer extends MapReduceBase implements Reducer<LongWritable, Text, LongWritable, Text> {
    protected int blockWidth;
    private String vectorIndicator;

    public void configure(JobConf conf) {
        vectorIndicator = conf.get(VECTOR_INDICATOR);
        blockWidth = Integer.parseInt(conf.get(BLOCK_WIDTH));
    }

    public void reduce(LongWritable key, Iterator<Text> values, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
        long[] outValues = new long[blockWidth];
        for (int i = 0; i < blockWidth; i++)
            outValues[i] = -1;

        while (values.hasNext()) {
            ArrayList<VectorElem<Long>> curVector = parseVectorVal(values.next().toString());

            for (VectorElem<Long> vElem : curVector)
                if (outValues[vElem.row] == -1 || outValues[vElem.row] > vElem.val)
                    outValues[vElem.row] = vElem.val;
        }

        long blockId = key.get();

        for (int i = 0; i < blockWidth; i++)
            output.collect(new LongWritable(blockWidth * blockId + i), new Text(vectorIndicator + outValues[i]));
    }
}
