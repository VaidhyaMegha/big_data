/***********************************************************************
 * PEGASUS: Peta-Scale Graph Mining System
 * Authors: U Kang, Duen Horng Chau, and Christos Faloutsos
 * <p/>
 * This software is licensed under Apache License, Version 2.0 (the  "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -------------------------------------------------------------------------
 * File: ConCmpt.java
 * - HCC: Find Connected Components of graph
 * Version: 2.0
 ***********************************************************************/

package me.tingri.graphs.cc;

import me.tingri.util.CONSTANTS;
import me.tingri.util.FLAGS;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

/**
 * <p/>
 * Heavy rewrite of original source code
 * Created by Sandeep Kunkunuru on 12/23/15.
 */
public class VectorGeneratorMapper extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text> {

    private String fieldSeparator;
    private FLAGS makeSymmetric;

    private LongWritable fromNode = new LongWritable();
    private LongWritable toNode = new LongWritable();

    public void configure(JobConf conf) {
        fieldSeparator = conf.get(CONSTANTS.FIELD_SEPARATOR);
        makeSymmetric = (FLAGS.YES.getValue() == Integer.parseInt(conf.get(CONSTANTS.MAKE_SYMMETRIC))) ? FLAGS.YES : FLAGS.NO;
    }

    public void map(LongWritable key, Text value, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
        String line_text = value.toString();
        String[] line = line_text.split(fieldSeparator);

        if (line.length != 2)
            return;

        fromNode.set(Long.parseLong(line[0]));
        toNode.set(Long.parseLong(line[1]));

        // invert dst and src so that matrix's (col, row) and vector's (row, col) fall on same reducer
        output.collect(toNode, new Text(line[0]));

        // if make-symmetric-edge requirement is requested make inverse edge
        // self-loop is not required since reducer takes care of it
        if (makeSymmetric == FLAGS.YES && toNode.get() != fromNode.get())
            output.collect(fromNode, new Text(line[1]));
    }
}
