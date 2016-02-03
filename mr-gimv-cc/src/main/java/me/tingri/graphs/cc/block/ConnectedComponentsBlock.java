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
import me.tingri.graphs.gimv.block.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import static me.tingri.util.CONSTANTS.*;

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
        System.out.println("CCBlock <edgePath> <curVectorPath> <# of nodes> <# of reducers> <fast or normal> <blockWidth> <makesym> <restart>");
        System.out.println("Block width has to be <= 32,767");

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
        int recurDiagonalMult = (FAST.equalsIgnoreCase(args[4])) ? 1 : 0;
        String makeSymmetric = (MAKE_SYMMETRIC.equalsIgnoreCase(args[5])) ? "1" : "0";
        short blockWidth = Short.parseShort(args[6]);

        System.out.println("\n-----===[PEGASUS: A Peta-Scale Graph Mining System]===-----\n");
        System.out.println("[PEGASUS] Computing connected component using block method. Reducers = " + numOfReducers + ", blockWidth = " + blockWidth);

        long changed = -1;
        FileSystem fs = FileSystem.get(getConf());

        //start from where we stopped i.e. if a vector_path exists and restart is requested jump straight to loop
        if (fs.exists(vecPath) && args.length == 8 && RESTART.equalsIgnoreCase(args[7]))
            Utility.rename(fs, vecPath, curVectorPath);
        else
            Utility.generateVector(new JobConf(getConf(), ConnectedComponentsBlock.class), fs, edgePath, curVectorPath, makeSymmetric, numOfReducers);

        // Iteratively calculate neighborhood function.
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            join(blockWidth, recurDiagonalMult, edgePath, curVectorPath, tempVectorPath, numOfReducers, makeSymmetric, fs);

            merge(blockWidth, tempVectorPath, nextVectorPath, numOfReducers, fs);

            changed = Utility.stateCheck(new JobConf(getConf(), ConnectedComponentsBlock.class), fs, curVectorPath, nextVectorPath, stateCheckTempPath, numOfReducers);

            System.out.println("Iteration " + i + " : changed = " + changed + ", unchanged = " + (numOfNodes - changed));

            Utility.rename(fs, nextVectorPath, curVectorPath);

            // Stop when the minimum neighborhood doesn't change
            if (changed == 0) break;
        }
        Utility.rename(fs, curVectorPath, vecPath);

        if (changed != 0)
            System.out.println("Convergence has not been achieved in " + MAX_ITERATIONS + " iterations. Final Results are in" + vecPath);
        else
            System.out.println("Convergence has been achieved. Final Results are in " + vecPath);

        return 0;
    }

    protected RunningJob join(short blockWidth, int recurDiagonalMult, Path edgePath,
                              Path curVectorPath, Path tempVectorPath, int numOfReducers, String makeSymmetric, FileSystem fs) throws Exception {
        Utility.deleteIfExists(fs, tempVectorPath);

        JobConf conf = new JobConf(getConf(), ConnectedComponentsBlock.class);

        conf.set(BLOCK_WIDTH, "" + blockWidth);
        conf.set(RECURSIVE_DIAG_MULT, "" + recurDiagonalMult);
        conf.set(FIELD_SEPARATOR, DEFAULT_FIELD_SEPARATOR);
        conf.set(VECTOR_INDICATOR, DEFAULT_VECTOR_INDICATOR);
        conf.set(MAKE_SYMMETRIC, makeSymmetric);

        conf.setJobName("CCBlock_join");

        conf.setMapperClass(JoinMapper.class);
        conf.setReducerClass(JoinReducer.class);

        FileInputFormat.setInputPaths(conf, edgePath, curVectorPath);
        FileOutputFormat.setOutputPath(conf, tempVectorPath);

        conf.setNumReduceTasks(numOfReducers);

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);

        return JobClient.runJob(conf);
    }

    protected RunningJob merge(short blockWidth, Path tempVectorPath, Path nextVectorPath, int numOfReducers, FileSystem fs) throws Exception {
        Utility.deleteIfExists(fs, nextVectorPath);

        JobConf conf = new JobConf(getConf(), ConnectedComponentsBlock.class);
        conf.set(BLOCK_WIDTH, "" + blockWidth);
        conf.set(VECTOR_INDICATOR, DEFAULT_VECTOR_INDICATOR);

        conf.setJobName("CCBlock_reduce");

        conf.setMapperClass(MergeMapper.class);
        conf.setReducerClass(MergeReducer.class);

        FileInputFormat.setInputPaths(conf, tempVectorPath);
        FileOutputFormat.setOutputPath(conf, nextVectorPath);

        conf.setNumReduceTasks(numOfReducers);

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);

        return JobClient.runJob(conf);
    }
}
