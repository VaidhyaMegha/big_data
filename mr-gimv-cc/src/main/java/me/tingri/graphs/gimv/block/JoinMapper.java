package me.tingri.graphs.gimv.block;

import me.tingri.util.FLAGS;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

import static me.tingri.util.CONSTANTS.*;

/**
 * Created by sandeep on 2/3/16.
 * STAGE 1: generate partial block-component ids.
 * //          Hash-join edge and vector by Vector.BLOCKROWID == Edge.BLOCKCOLID where
 * //          vector: key=BLOCKID, value= msu (IN-BLOCK-INDEX VALUE)s
 * //                                      moc
 * //          edge: key=BLOCK-ROW		BLOCK-COL, value=(IN-BLOCK-ROW IN-BLOCK-COL VALUE)s
 * //  - Input: edge_file, component_ids_from_the_last_iteration
 * //  - Output: partial component ids
 */
public class JoinMapper extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text> {
    protected short blockWidth;
    private String fieldSeparator;
    private String sepInValue;
    private String vectorIndicator;
    private FLAGS makeSymmetric;

    public void configure(JobConf conf) {
        fieldSeparator = conf.get(FIELD_SEPARATOR);
        sepInValue = conf.get(SEPARATOR_WITHIN_VALUE);
        vectorIndicator = conf.get(VECTOR_INDICATOR);
        makeSymmetric = (FLAGS.YES.getValue() == Integer.parseInt(conf.get(MAKE_SYMMETRIC))) ? FLAGS.YES : FLAGS.NO;
        blockWidth = Short.parseShort(conf.get(BLOCK_WIDTH));
    }

    /**
     * Input to the mapper task is of the below form
     * vector: key=index-in-vector, value=component-identifier(ex: v1)
     * edge:  key=row-index-in-matrix, value=column-index-in-matrix
     * <p/>
     * Output of the mapper task should be of the below form
     * vector: key=BLOCKID, value= (IN-BLOCK-INDEX VALUE)s
     * edge: key=BLOCK-COLID, value=(BLOCK-ROWID    IN-BLOCK-COL IN-BLOCK-ROW VALUE)s
     *
     * @throws IOException
     */
    public void map(LongWritable key, Text value, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
        String line_text = value.toString();
        String[] line = line_text.split(fieldSeparator);

        if (line.length != 2)
            return;

        long blockRowId = Long.parseLong(line[0]) / blockWidth;
        short inBlockRowIndex = (short) (Long.parseLong(line[0]) % blockWidth);

        if (line[1].startsWith(vectorIndicator)) {
            output.collect(new LongWritable(blockRowId), new Text(inBlockRowIndex + sepInValue + line[1].substring(1)));
        } else {
            long blockColId = Long.parseLong(line[1]) / blockWidth;
            long inBlockColIndex = Long.parseLong(line[1]) % blockWidth;

            output.collect(new LongWritable(blockColId), new Text(blockRowId + fieldSeparator + inBlockColIndex + sepInValue + inBlockRowIndex + sepInValue + 1));

            // if make-symmetric-edge requirement is requested make inverse edge
            // self-loop is not required since reducer takes care of it
            if (makeSymmetric == FLAGS.YES && !line[0].equals(line[1]))
                output.collect(new LongWritable(blockRowId), new Text(blockColId + fieldSeparator + inBlockRowIndex + sepInValue + inBlockColIndex + sepInValue + 1));
        }
    }
}
