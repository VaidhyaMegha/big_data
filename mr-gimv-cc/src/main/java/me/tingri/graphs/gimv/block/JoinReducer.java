package me.tingri.graphs.gimv.block;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static me.tingri.graphs.gimv.block.GIMVUtility.*;
import static me.tingri.util.CONSTANTS.*;

/**
 * Created by sandeep on 2/3/16.
 */
public class JoinReducer extends MapReduceBase implements Reducer<LongWritable, Text, LongWritable, Text> {
    protected int blockWidth;
    protected int recurDiagonalMult;
    private String fieldSeparator;
    private String sepInValue;

    public void configure(JobConf conf) {
        fieldSeparator = conf.get(FIELD_SEPARATOR);
        sepInValue = conf.get(SEPARATOR_WITHIN_VALUE);
        blockWidth = Integer.parseInt(conf.get(BLOCK_WIDTH));
        recurDiagonalMult = Integer.parseInt(conf.get(RECURSIVE_DIAG_MULT));
    }

    /**
     * Input is of the below form:
     * vector: key=BLOCKID, value= (IN-BLOCK-INDEX VALUE)s
     * edge: key=BLOCK-COLID, value=(BLOCK-ROWID    IN-BLOCK-COL IN-BLOCK-ROW VALUE)s
     * @throws IOException
     */
    public void reduce(LongWritable key, Iterator<Text> values, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
        StringBuilder vectorArrBuffer = new StringBuilder();
        ArrayList<ArrayList<BlockElem<Integer>>> blockArr = new ArrayList<ArrayList<BlockElem<Integer>>>();
        HashMap<Long, StringBuilder> blockRowMap = new HashMap<Long, StringBuilder>();

        while (values.hasNext()) {
            String line_text = values.next().toString();
            String[] line = line_text.split(fieldSeparator);

            // vector : IN-BLOCK-INDEX VALUE
            if (line.length == 1)
                vectorArrBuffer.append(line_text).append(sepInValue);
            else {                    // edge : BLOCK-ROWID		IN-BLOCK-COL IN-BLOCK-ROW VALUE
                long blockRowId = Long.parseLong(line[0]);
                if (blockRowMap.containsKey(blockRowId))
                    blockRowMap.get(blockRowId).append(sepInValue).append(line[1]);
                else
                    blockRowMap.put(blockRowId, new StringBuilder(line[1]));
            }
        }

        ArrayList<VectorElem<Long>> vectorArr = parseVectorVal(vectorArrBuffer.toString().trim());
        ArrayList<Long> blockRowIdArr = new ArrayList<Long>();

        for (long blockRowId: blockRowMap.keySet()){
            blockRowIdArr.add(blockRowId);
            blockArr.add(parseBlockVal(blockRowMap.get(blockRowId).toString()));
        }

        if (vectorArr == null) // missing vector or block.
            return;

        // For every matrix block, join it with vector and output partial results
        Iterator<Long> blockRowIter = blockRowIdArr.iterator();
        for (ArrayList<BlockElem<Integer>> block : blockArr) {
            long curBlockRow = blockRowIter.next();

            ArrayList<VectorElem<Long>> result;

            if (key.get() == curBlockRow && recurDiagonalMult == 1) {    // do recursive multiplication
                ArrayList<VectorElem<Long>> tempVectorArr = vectorArr;
                result = minBlockVector(block, vectorArr, blockWidth, true);
                int i = 1;
                while (compareVectors(tempVectorArr, result) != 0 && ++i < MAX_ITERATIONS) { //setting threshold
                    tempVectorArr = result;
                    result = minBlockVector(block, result, blockWidth, true);
                }
            } else
                result = minBlockVector(block, vectorArr, blockWidth, false);

            Text resultVector = formatVectorElemOutput(result);

            if (resultVector.toString().length() > 0)
                output.collect(new LongWritable(curBlockRow), resultVector);
        }
    }

}
