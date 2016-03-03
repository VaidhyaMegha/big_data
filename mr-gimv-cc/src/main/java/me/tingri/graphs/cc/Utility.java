package me.tingri.graphs.cc;

import me.tingri.graphs.gimv.VectorGeneratorMapper;
import me.tingri.graphs.gimv.VectorGeneratorReducer;
import me.tingri.util.CONSTANTS;
import me.tingri.util.FLAGS;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

/**
 * Created by sandeep on 2/2/16.
 */
public class Utility {

    public static void rename(FileSystem fs, Path path, Path newPath) throws IOException {
        deleteIfExists(fs, newPath);
        fs.rename(path, newPath);
    }

    public static void deleteIfExists(FileSystem fs, Path path) throws IOException {
        if(fs.exists(path))
            fs.delete(path, true);
    }

    public static RunningJob generateVector(JobConf conf, FileSystem fs, Path edgePath, Path vecPath, String makeSymmetric, int numOfReducers) throws IOException {
        deleteIfExists(fs, vecPath);

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

    public static long stateCheck(JobConf conf, FileSystem fs, Path vecPath, Path nextVectorPath, Path stateCheckTempPath, int numOfReducers) throws Exception {
        deleteIfExists(fs, stateCheckTempPath);

        conf.set(CONSTANTS.VECTOR_INDICATOR, CONSTANTS.DEFAULT_VECTOR_INDICATOR);

        conf.setJobName("ConnectedComponents_StateCheck");

        conf.setMapperClass(StateCheckMapper.class);
        conf.setReducerClass(StateCheckReducer.class);

        FileInputFormat.setInputPaths(conf, vecPath, nextVectorPath);
        FileOutputFormat.setOutputPath(conf, stateCheckTempPath);

        conf.setNumReduceTasks(numOfReducers);

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);

        RunningJob stateCheckJob =  JobClient.runJob(conf);
        long changed = stateCheckJob.getCounters().getCounter(FLAGS.CHANGED);

        ContentSummary summary = fs.getContentSummary(stateCheckTempPath);
        System.out.println(summary);

        if(summary.getLength() !=0 && (changed == 0)) changed = -1; //Tez doesnt read counters instead returns 0

        return changed;
    }
}
