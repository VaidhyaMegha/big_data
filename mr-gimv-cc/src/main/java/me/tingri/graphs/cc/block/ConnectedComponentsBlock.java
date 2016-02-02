package me.tingri.graphs.cc.block;
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
 * File: CCBlock.java
 * - HCC: Find Connected Components of graph using block multiplication. This is a block-based version of HCC.
 * Version: 2.0
 ***********************************************************************/

import me.tingri.graphs.cc.Utility;
import me.tingri.graphs.gimv.MergeMapper;
import me.tingri.graphs.gimv.block.BlockElem;
import me.tingri.graphs.gimv.block.GIMVUtility;
import me.tingri.graphs.gimv.block.UnfoldMapper;
import me.tingri.graphs.gimv.block.VectorElem;
import me.tingri.util.CONSTANTS;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Heavy rewrite of original source code
 * Created by Sandeep Kunkunuru on 02/02/16.
 */
public class ConnectedComponentsBlock extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int result = ToolRunner.run(new Configuration(), new ConnectedComponentsBlock(), args);

        System.exit(result);
    }

    protected static int printUsage() {
        System.out.println("CCBlock <edgePath> <curVectorPath> <# of nodes> <# of reducers> <outputPath> <fast or normal> <blockWidth> <restart>");

        ToolRunner.printGenericCommandUsage(System.out);

        return -1;
    }

    public int run(String[] args) throws Exception {
        if (args.length != 7 && args.length != 8) return printUsage();

        Path edgePath = new Path(args[0]);
        Path vecPath = new Path(args[1]);
        Path curVectorPath = new Path(vecPath.toString() + "_WIP");
        Path tempVectorPath = new Path(curVectorPath.toString() + "_temp");
        Path nextVectorPath = new Path(curVectorPath.toString() + "_next");
        Path stateCheckTempPath = new Path(curVectorPath.toString() + "_STATE_CHECK_TEMP");
        long numOfNodes = Long.parseLong(args[2]);
        int numOfReducers = Integer.parseInt(args[3]);
        Path outputPath = new Path(args[4]);
        int recurDiagonalMult = (CONSTANTS.FAST.equalsIgnoreCase(args[5])) ? 1 : 0;
        int blockWidth = Integer.parseInt(args[6]);

        System.out.println("\n-----===[PEGASUS: A Peta-Scale Graph Mining System]===-----\n");
        System.out.println("[PEGASUS] Computing connected component using block method. Reducers = " + numOfReducers + ", blockWidth = " + blockWidth);

        long changed = -1;
        FileSystem fs = FileSystem.get(getConf());

        //start from where we stopped i.e. if a vector_path exists and restart is requested jump straight to loop
        if (fs.exists(vecPath) && args.length == 8 && CONSTANTS.RESTART.equalsIgnoreCase(args[7]))
            Utility.rename(fs, vecPath, curVectorPath);
        else
            Utility.generateVector(new JobConf(getConf(), ConnectedComponentsBlock.class), fs, edgePath, curVectorPath, "1", numOfReducers);

        // Iteratively calculate neighborhood function.
        for (int i = 0; i < CONSTANTS.MAX_ITERATIONS; i++) {
            JobClient.runJob(join(blockWidth, recurDiagonalMult, edgePath, curVectorPath, tempVectorPath, numOfReducers));
            JobClient.runJob(merge(blockWidth, tempVectorPath, nextVectorPath, numOfReducers));

            changed = Utility.stateCheck(new JobConf(getConf(), ConnectedComponentsBlock.class), fs, curVectorPath, nextVectorPath, stateCheckTempPath, numOfReducers);

            System.out.println("Hop " + i + " : changed = " + changed + ", unchanged = " + (numOfNodes - changed));

            // rotate directory
            Utility.deleteIfExists(fs, tempVectorPath);
            Utility.deleteIfExists(fs, outputPath);
            Utility.rename(fs, nextVectorPath, curVectorPath);

            // Stop when the minimum neighborhood doesn't change
            if (changed == 0) {
                System.out.println("All the component ids converged.Unfolding the block structure for easy lookup...");

                JobClient.runJob(unfold(blockWidth, curVectorPath, outputPath));

                System.out.println("[PEGASUS] Connected component information is saved in the HDFS concmpt_curbm as\n\"node_id	'msf'component_id\" format");

                break;
            }
        }

        if (changed != 0)
            System.out.println("Convergence has not been achieved in " + CONSTANTS.MAX_ITERATIONS + " iterations");

        return 0;
    }

    protected JobConf join(int blockWidth, int recurDiagonalMult, Path edgePath,
                           Path curVectorPath, Path tempVectorPath, int numOfReducers) throws Exception {
        JobConf conf = new JobConf(getConf(), ConnectedComponentsBlock.class);
        conf.set("blockWidth", "" + blockWidth);
        conf.set("recurDiagonalMult", "" + recurDiagonalMult);
        conf.setJobName("CCBlock_join");

        conf.setMapperClass(JoinMapper.class);
        conf.setReducerClass(JoinReducer.class);

        FileInputFormat.setInputPaths(conf, edgePath, curVectorPath);
        FileOutputFormat.setOutputPath(conf, tempVectorPath);

        conf.setNumReduceTasks(numOfReducers);

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);

        return conf;
    }

    protected JobConf merge(int blockWidth, Path tempVectorPath, Path nextVectorPath, int numOfReducers) throws Exception {
        JobConf conf = new JobConf(getConf(), ConnectedComponentsBlock.class);
        conf.set("blockWidth", "" + blockWidth);
        conf.setJobName("CCBlock_reduce");

        conf.setMapperClass(MergeMapper.class);
        conf.setReducerClass(MergeReducer.class);

        FileInputFormat.setInputPaths(conf, tempVectorPath);
        FileOutputFormat.setOutputPath(conf, nextVectorPath);

        conf.setNumReduceTasks(numOfReducers);

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);

        return conf;
    }


    // Configure unfold
    protected JobConf unfold(int blockWidth, Path curVectorPath, Path curVectorUnfoldPath) throws Exception {
        JobConf conf = new JobConf(getConf(), ConnectedComponentsBlock.class);
        conf.set("blockWidth", "" + blockWidth);
        conf.setJobName("CCBlock_unfold");

        conf.setMapperClass(UnfoldMapper.class);

        FileInputFormat.setInputPaths(conf, curVectorPath);
        FileOutputFormat.setOutputPath(conf, curVectorUnfoldPath);

        conf.setNumReduceTasks(0);        //This is essential for map-only tasks.

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);

        return conf;
    }


    //////////////////////////////////////////////////////////////////////
    // STAGE 1: generate partial block-component ids.
    //          Hash-join edge and vector by Vector.BLOCKROWID == Edge.BLOCKCOLID where
    //          vector: key=BLOCKID, value= msu (IN-BLOCK-INDEX VALUE)s
    //                                      moc
    //          edge: key=BLOCK-ROW		BLOCK-COL, value=(IN-BLOCK-ROW IN-BLOCK-COL VALUE)s
    //  - Input: edge_file, component_ids_from_the_last_iteration
    //  - Output: partial component ids
    //////////////////////////////////////////////////////////////////////
    public static class JoinMapper extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text> {
        protected int blockWidth;

        public void configure(JobConf job) {
            blockWidth = Integer.parseInt(job.get("blockWidth"));
        }

        public void map(LongWritable key, Text value, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
            String line_text = value.toString();
            String[] line = line_text.split(CONSTANTS.DEFAULT_FIELD_SEPARATOR);

            if (line.length == 2) {    // vector. component information.
                output.collect(new LongWritable(Long.parseLong(line[0])), new Text(line[1]));
            } else {                    // edge
                output.collect(new LongWritable(Long.parseLong(line[1])), new Text(line[0] + CONSTANTS.DEFAULT_FIELD_SEPARATOR + line[2]));
            }
        }
    }

    public static class JoinReducer extends MapReduceBase implements Reducer<LongWritable, Text, LongWritable, Text> {
        protected int blockWidth;
        protected int recurDiagonalMult;

        public void configure(JobConf job) {
            blockWidth = Integer.parseInt(job.get("blockWidth"));
            recurDiagonalMult = Integer.parseInt(job.get("recurDiagonalMult"));
        }

        public void reduce(LongWritable key, Iterator<Text> values, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
            ArrayList<VectorElem<Long>> vectorArr = null;        // save vector
            ArrayList<ArrayList<BlockElem<Integer>>> blockArr = new ArrayList<ArrayList<BlockElem<Integer>>>();    // save blocks
            ArrayList<Long> blockRowArr = new ArrayList<Long>();    // save block rows(integer)

            while (values.hasNext()) {
                // vector: key=BLOCKID, value= (IN-BLOCK-INDEX VALUE)s
                // edge: key=BLOCK-COLID	BLOCK-ROWID, value=(IN-BLOCK-COL IN-BLOCK-ROW VALUE)s
                String line_text = values.next().toString();
                String[] line = line_text.split(CONSTANTS.DEFAULT_FIELD_SEPARATOR);

                // vector : VALUE
                if (line.length == 1)
                    vectorArr = GIMVUtility.parseVectorVal(line_text.substring(3));
                else {                    // edge : BLOCK-ROWID		VALUE
                    blockArr.add(GIMVUtility.parseBlockVal(line[1]));
                    blockRowArr.add(Long.parseLong(line[0]));
                }
            }

            if (vectorArr == null) // missing vector or block.
                return;

            // output 'self' block to check convergence
            output.collect(key, GIMVUtility.formatVectorElemOutput("msi", vectorArr));

            // For every matrix block, join it with vector and output partial results
            Iterator<Long> blockRowIter = blockRowArr.iterator();
            for (ArrayList<BlockElem<Integer>> cur_block : blockArr) {
                long cur_block_row = blockRowIter.next();

                ArrayList<VectorElem<Long>> cur_mult_result = null;

                if (key.get() == cur_block_row && recurDiagonalMult == 1) {    // do recursive multiplication
                    ArrayList<VectorElem<Long>> tempVectorArr = vectorArr;
                    for (int i = 0; i < blockWidth; i++) {
                        cur_mult_result = GIMVUtility.minBlockVector(cur_block, tempVectorArr, blockWidth, true);

                        if (cur_mult_result == null || GIMVUtility.compareVectors(tempVectorArr, cur_mult_result) == 0)
                            break;

                        tempVectorArr = cur_mult_result;
                    }
                } else
                    cur_mult_result = GIMVUtility.minBlockVector(cur_block, vectorArr, blockWidth, false);

                Text output_vector = GIMVUtility.formatVectorElemOutput("moi", cur_mult_result);

                if (output_vector.toString().length() > 0)
                    output.collect(new LongWritable(cur_block_row), output_vector);
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // STAGE 2: merge partial comonent ids.
    //  - Input: partial component ids
    //  - Output: combined component ids
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static class MergeReducer extends MapReduceBase implements Reducer<LongWritable, Text, LongWritable, Text> {
        protected int blockWidth;

        public void configure(JobConf job) {
            blockWidth = Integer.parseInt(job.get("blockWidth"));
        }

        public void reduce(LongWritable key, Iterator<Text> values, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
            ArrayList<VectorElem<Long>> self_vector = null;
            long[] out_vals = new long[blockWidth];
            for (int i = 0; i < blockWidth; i++)
                out_vals[i] = -1;

            while (values.hasNext()) {
                String cur_str = values.next().toString();

                if (cur_str.charAt(1) == 's') {
                    self_vector = GIMVUtility.parseVectorVal(cur_str.substring(3));
                }

                ArrayList<VectorElem<Long>> cur_vector = GIMVUtility.parseVectorVal(cur_str.substring(3));

                for (VectorElem<Long> v_elem : cur_vector) {
                    if (out_vals[v_elem.row] == -1)
                        out_vals[v_elem.row] = v_elem.val;
                    else if (out_vals[v_elem.row] > v_elem.val)
                        out_vals[v_elem.row] = v_elem.val;
                }
            }

            ArrayList<VectorElem<Long>> new_vector = GIMVUtility.makeIntVectors(out_vals, blockWidth);
            int isDifferent = GIMVUtility.compareVectors(self_vector, new_vector);

            String out_prefix = "ms";
            if (isDifferent == 1)
                out_prefix += "i";    // incomplete
            else
                out_prefix += "f";    // finished

            output.collect(key, GIMVUtility.formatVectorElemOutput(out_prefix, new_vector));
        }
    }

    //////////////////////////////////////////////////////////////////////
    // STAGE 3: Calculate number of nodes whose component id changed/unchanged.
    //  - Input: current component ids
    //  - Output: number_of_changed_nodes
    //////////////////////////////////////////////////////////////////////
    /*

    // Configure pass3
    protected JobConf stateCheck(Path stateCheckTempPath, Path nextVectorPath) throws Exception {
        JobConf conf = new JobConf(getConf(), ConnectedComponentsBlock.class);
        conf.setJobName("CCBlock_pass3");

        conf.setMapperClass(StateCheckMapper.class);
        conf.setReducerClass(StateCheckReducer.class);
        conf.setCombinerClass(StateCheckReducer.class);

        FileInputFormat.setInputPaths(conf, nextVectorPath);
        FileOutputFormat.setOutputPath(conf, stateCheckTempPath);

        conf.setNumReduceTasks(1);// This is necessary to summarize and save data.

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);

        return conf;
    }

    public static class StateCheckMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
        // output : f n		( n : # of node whose component didn't change)
        //          i m		( m : # of node whose component changed)
        public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
            String[] line = value.toString().split(CONSTANTS.DEFAULT_FIELD_SEPARATOR);
            char change_prefix = line[1].charAt(2);

            output.collect(new Text(Character.toString(change_prefix)), new Text(Integer.toString(1)));
        }
    }

    public static class StateCheckReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
            int sum = 0;

            while (values.hasNext()) {
                String line = values.next().toString();
                long cur_value = Long.parseLong(line);

                sum += cur_value;
            }

            output.collect(key, new Text(Integer.toString(sum)));

            if (key.toString().startsWith("i") && sum != 0) {
                reporter.incrCounter(FLAGS.CHANGED, 1);
            }
        }
    }

    */

}
