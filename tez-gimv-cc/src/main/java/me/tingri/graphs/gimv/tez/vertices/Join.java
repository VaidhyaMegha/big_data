package me.tingri.graphs.gimv.tez.vertices;

import com.google.common.base.Preconditions;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.tez.runtime.api.ProcessorContext;
import org.apache.tez.runtime.library.api.KeyValueReader;
import org.apache.tez.runtime.library.api.KeyValueWriter;
import org.apache.tez.runtime.library.processor.SimpleProcessor;

import static me.tingri.graphs.util.CONSTANTS.*;

/**
 * Created by sandeep on 4/9/16.
 */
public class Join extends SimpleProcessor{
    LongWritable fromNode = new LongWritable();
    LongWritable toNode = new LongWritable();
    Text word = new Text();

    public Join(ProcessorContext context) {
        super(context);
    }

    @Override
    public void run() throws Exception {
        Preconditions.checkArgument(getInputs().size() == 2);
        Preconditions.checkArgument(getOutputs().size() == 1);

        KeyValueReader edgeReader = (KeyValueReader) getInputs().get(EDGES).getReader();
        KeyValueReader vecReader = (KeyValueReader) getInputs().get(VECTOR).getReader();
        KeyValueWriter joinWriter = (KeyValueWriter) getOutputs().get(JOIN).getWriter();

        while (edgeReader.next()) {
            String text = edgeReader.getCurrentValue().toString();
            String[] line = text.split(DEFAULT_FIELD_SEPARATOR);

            if (line.length != 2) continue; //ignore this line

            fromNode.set(Long.parseLong(line[0]));
            toNode.set(Long.parseLong(line[1]));

            joinWriter.write(fromNode, toNode);
            joinWriter.write(toNode, fromNode);
        }

        while (vecReader.next()) {
            String text = vecReader.getCurrentValue().toString();
            String[] line = text.split(DEFAULT_FIELD_SEPARATOR);

            if (line.length != 2) continue; //ignore this line

            fromNode.set(Long.parseLong(line[0]));
            toNode.set(Long.parseLong(line[1]));

            joinWriter.write(fromNode, toNode);
        }
    }
}
