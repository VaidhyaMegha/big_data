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
package me.tingri.graphs.gimv;

import me.tingri.util.CONSTANTS;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * //////////////////////////////////////////////////////////////////////
 * // STAGE 1: join matrix elements and vector elements using matrix.dst_id and vector.row_id
 * //////////////////////////////////////////////////////////////////////
 * <p/>
 * Heavy rewrite of original source code
 * Created by Sandeep Kunkunuru on 12/23/15.
 */
public class JoinReducer extends MapReduceBase implements Reducer<LongWritable, Text, LongWritable, Text> {
    private String vectorIndicator;

    public void configure(JobConf conf) {
        vectorIndicator = conf.get(CONSTANTS.VECTOR_INDICATOR);
    }

    public void reduce(LongWritable key, Iterator<Text> values, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
        String componentId = "";
        Set<Long> fromNodes = new HashSet<Long>();

        while (values.hasNext()) {
            String line = values.next().toString();

            if (line.startsWith(vectorIndicator))    // component info
                componentId = line;
            else                                                    // edge line
                fromNodes.add(Long.parseLong(line));
        }

        // add self loop. If it already exists it wont harm since it is a set.
        fromNodes.add(key.get());

        for (Long fromNode : fromNodes)
            output.collect(new LongWritable(fromNode), new Text(componentId));
    }
}
