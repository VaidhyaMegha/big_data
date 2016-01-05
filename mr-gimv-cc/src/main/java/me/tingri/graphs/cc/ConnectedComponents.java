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

import me.tingri.graphs.gimv.JoinMapper;
import me.tingri.graphs.gimv.JoinReducer;
import me.tingri.graphs.gimv.MergeMapper;
import me.tingri.graphs.gimv.MergeReducer;
import me.tingri.util.CONSTANTS;
import me.tingri.util.FLAGS;
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

/**
 * Heavy rewrite of original source code
 * Created by Sandeep Kunkunuru on 12/23/15.
 */
public class ConnectedComponents extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int result = ToolRunner.run(new Configuration(), new ConnectedComponents(), args);

        System.exit(result);
    }

    protected static int printUsage() {
        System.out.println("ConnectedComponents <edge_path> <vector_path> <# of nodes> <# of reducers> <makesym or nosym> <restart>");

        ToolRunner.printGenericCommandUsage(System.out);

        return -1;
    }

    public int run(String[] args) throws Exception {
        if (args.length != 5 && args.length != 6) return printUsage();

        Path edgePath = new Path(args[0]);
        Path vecPath = new Path(args[1]);
        Path curVectorPath = new Path(vecPath.toString() + "_WIP");
        Path tempVectorPath = new Path(curVectorPath.toString() + "_temp");
        Path nextVectorPath = new Path(curVectorPath.toString() + "_next");
        Path stateCheckTempPath = new Path(curVectorPath.toString() + "_STATE_CHECK_TEMP");
        long numOfNodes = Long.parseLong(args[2]);
        int numOfReducers = Integer.parseInt(args[3]);
        String makeSymmetric = (CONSTANTS.MAKE_SYMMETRIC.equalsIgnoreCase(args[4])) ? "1" : "0";

        FileSystem fs = FileSystem.get(getConf());

        //start from where we stopped i.e. if a vector_path exists and restart is requested jump straight to loop
        if(fs.exists(vecPath) && args.length == 6 && CONSTANTS.RESTART.equalsIgnoreCase(args[5]))
            rename(fs, vecPath, curVectorPath);
        else
            generateVector(fs, edgePath, curVectorPath, makeSymmetric, numOfReducers);

        //TODO for debugging retain all intermediate vectors

        long changed = -1;

        // Iteratively calculate neighbor with minimum id.
        for (int i = 0; i < CONSTANTS.MAX_ITERATIONS; i++) {
            join(fs, edgePath, curVectorPath, tempVectorPath, makeSymmetric, numOfReducers);

            merge(fs, tempVectorPath, nextVectorPath, numOfReducers);

            changed = countChanged(fs, curVectorPath, nextVectorPath, stateCheckTempPath, numOfReducers);

            System.out.println("Iteration " + i + " : changed = " + changed + ", unchanged = " + (numOfNodes - changed));

            rename(fs, nextVectorPath, curVectorPath);

            // Stop when there are no more changes to vector
            if (changed == 0) {
                System.out.println("All the component ids converged.");
                System.out.println("Convergence has been achieved in " + i + " iterations. Final Results are in " + vecPath);
                break;
            }
        }

        rename(fs, curVectorPath, vecPath);

        if (changed != 0)
            System.out.println("Convergence has not been achieved in " + CONSTANTS.MAX_ITERATIONS + " iterations. Final Results are in" + vecPath);

        return 0;
    }

    private void rename(FileSystem fs, Path path, Path newPath) throws IOException {
        deleteIfExists(fs, newPath);
        fs.rename(path, newPath);
    }

    private void deleteIfExists(FileSystem fs, Path path) throws IOException {
        if(fs.exists(path))
            fs.delete(path, true);
    }

    private RunningJob generateVector(FileSystem fs, Path edgePath, Path vecPath, String makeSymmetric, int numOfReducers) throws IOException {
        deleteIfExists(fs, vecPath);

        JobConf conf = new JobConf(getConf(), ConnectedComponents.class);
        conf.set(CONSTANTS.FIELD_SEPARATOR, CONSTANTS.DEFAULT_FIELD_SEPARATOR);
        conf.set(CONSTANTS.VECTOR_INDICATOR, CONSTANTS.DEFAULT_VECTOR_INDICATOR);
        conf.set(CONSTANTS.MAKE_SYMMETRIC, makeSymmetric);

        conf.setJobName("ConnectedComponents_Preparation_Vector_Generation");

        conf.setMapperClass(VectorGeneratorMapper.class);
        conf.setReducerClass(VectorGeneratorReducer.class);

        FileInputFormat.setInputPaths(conf, edgePath);
        FileOutputFormat.setOutputPath(conf, vecPath);

        conf.setNumReduceTasks(numOfReducers);

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);

        return JobClient.runJob(conf);
    }


    protected RunningJob join(FileSystem fs, Path edgePath, Path vecPath, Path tempVectorPath, String makeSymmetric, int numOfReducers) throws Exception {
        deleteIfExists(fs, tempVectorPath);

        JobConf conf = new JobConf(getConf(), ConnectedComponents.class);
        conf.set(CONSTANTS.FIELD_SEPARATOR, CONSTANTS.DEFAULT_FIELD_SEPARATOR);
        conf.set(CONSTANTS.VECTOR_INDICATOR, CONSTANTS.DEFAULT_VECTOR_INDICATOR);
        conf.set(CONSTANTS.MAKE_SYMMETRIC, makeSymmetric);

        conf.setJobName("ConnectedComponents_Join");

        conf.setMapperClass(JoinMapper.class);
        conf.setReducerClass(JoinReducer.class);

        FileInputFormat.setInputPaths(conf, edgePath, vecPath);
        FileOutputFormat.setOutputPath(conf, tempVectorPath);

        conf.setNumReduceTasks(numOfReducers);

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);

        return JobClient.runJob(conf);
    }

    protected RunningJob merge(FileSystem fs, Path tempVectorPath, Path nextVectorPath, int numOfReducers) throws Exception {
        deleteIfExists(fs, nextVectorPath);

        JobConf conf = new JobConf(getConf(), ConnectedComponents.class);
        conf.set(CONSTANTS.VECTOR_INDICATOR, CONSTANTS.DEFAULT_VECTOR_INDICATOR);

        conf.setJobName("ConnectedComponents_Merge");

        conf.setMapperClass(MergeMapper.class);
        conf.setReducerClass(MergeReducer.class);

        FileInputFormat.setInputPaths(conf, tempVectorPath);
        FileOutputFormat.setOutputPath(conf, nextVectorPath);

        conf.setNumReduceTasks(numOfReducers);

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);

        return JobClient.runJob(conf);
    }

    protected long countChanged(FileSystem fs, Path vecPath, Path nextVectorPath, Path stateCheckTempPath, int numOfReducers) throws Exception {
        deleteIfExists(fs, stateCheckTempPath);

        JobConf conf = new JobConf(getConf(), ConnectedComponents.class);

        conf.setJobName("ConnectedComponents_StateCheck");

        conf.setMapperClass(StateCheckMapper.class);
        conf.setReducerClass(StateCheckReducer.class);

        FileInputFormat.setInputPaths(conf, vecPath, nextVectorPath);
        FileOutputFormat.setOutputPath(conf, stateCheckTempPath);

        conf.setNumReduceTasks(numOfReducers);

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);

        RunningJob stateCheckJob =  JobClient.runJob(conf);
        return stateCheckJob.getCounters().getCounter(FLAGS.CHANGED);
    }
}