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
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;

/**
 * Heavy rewrite of original source code
 * Created by Sandeep Kunkunuru on 12/23/15.
 */
public class GIMVIdentityMapper extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text> {
    // Identity mapper
    public void map(LongWritable key, Text value, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
        String[] line = value.toString().split(CONSTANTS.DEFAULT_FIELD_SEPARATOR);

        output.collect(new LongWritable(Long.parseLong(line[0])), new Text(line[1]));
    }
}
