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

/**
 * Created by sandeep on 12/23/15.
 */
public class ConnectedComponents extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int result = ToolRunner.run(new Configuration(), new ConnectedComponents(), args);

        System.exit(result);
    }

    protected static int printUsage() {
        System.out.println("ConnectedComponents <edge_path> <output_path> <# of nodes> <# of reducers> <makesym or nosym>");

        ToolRunner.printGenericCommandUsage(System.out);

        return -1;
    }

    public int run(String[] args) throws Exception {
        if (args.length != 5) return printUsage();

        Path edgePath = new Path(args[0]);
        Path outputPath = new Path(args[1]);
        Path vecPath = new Path(outputPath.toString() + "_WIP");
        Path tempVectorPath = new Path(vecPath.toString() + "_temp");
        Path nextVectorPath = new Path(vecPath.toString() + "_next");
        long numOfNodes = Long.parseLong(args[2]);
        int numOfReducers = Integer.parseInt(args[3]);
        String makeSymmetric = (CONSTANTS.MAKE_SYMMETRIC.equalsIgnoreCase(args[4])) ? "1" : "0";

        FileSystem fs = FileSystem.get(getConf());

        JobClient.runJob(getVectorGeneratorConf(edgePath, vecPath, makeSymmetric, numOfReducers));

        //TODO for debugging retain all intermediate vectors

        long changed;
        int i = 0;
        FLAGS converged = FLAGS.NO;

       // Iteratively calculate neighbor with minimum id.
        while( i++ < CONSTANTS.MAX_ITERATIONS) {
            JobClient.runJob(getJoinConf(edgePath, vecPath, tempVectorPath, makeSymmetric, numOfReducers));
            JobClient.runJob(getMergeConf(tempVectorPath, nextVectorPath, numOfReducers));

            RunningJob stateCheckJob = JobClient.runJob(getStateCheckConf(vecPath, nextVectorPath, numOfReducers));

            changed = stateCheckJob.getCounters().getCounter(FLAGS.CHANGED);

            System.out.println("Iteration " + i + " : changed = " + changed + ", unchanged = " + (numOfNodes - changed));

            // rotate directory
            fs.delete(vecPath, true);
            fs.delete(tempVectorPath, true);
            fs.rename(nextVectorPath, vecPath);

            // Stop when there are no more changes to vector
            if (changed == 0)   {
                System.out.println("All the component ids converged. Finishing...");
                converged = FLAGS.YES;
                break;
            }
        }

        fs.rename(vecPath,outputPath);

        System.out.println("Summarizing connected components information...");

        if(converged == FLAGS.YES)
            System.out.println("Convergence has been achieved in " + i + " iterations. Final Results are in" + outputPath );
        else
            System.out.println("Convergence has not been achieved in " + CONSTANTS.MAX_ITERATIONS + " iterations. Final Results are in" + outputPath );

       return 0;
    }

    private JobConf getVectorGeneratorConf(Path edgePath, Path vecPath, String makeSymmetric, int numOfReducers) {
        JobConf conf = new JobConf(getConf(), ConnectedComponents.class);
        conf.set(CONSTANTS.FIELD_SEPARATOR, CONSTANTS.DEFAULT_FIELD_SEPARATOR );
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

        return conf;
    }


    protected JobConf getJoinConf(Path edgePath, Path vecPath, Path tempVectorPath, String makeSymmetric, int numOfReducers) throws Exception {
        JobConf conf = new JobConf(getConf(), ConnectedComponents.class);
        conf.set(CONSTANTS.FIELD_SEPARATOR, CONSTANTS.DEFAULT_FIELD_SEPARATOR );
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

        return conf;
    }

    protected JobConf getMergeConf(Path tempVectorPath, Path nextVectorPath, int numOfReducers) throws Exception {
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

        return conf;
    }

    protected JobConf getStateCheckConf(Path vecPath, Path nextVectorPath, int numOfReducers) throws Exception {
        JobConf conf = new JobConf(getConf(), ConnectedComponents.class);

        conf.setJobName("ConnectedComponents_StateCheck");

        conf.setMapperClass(StateCheckMapper.class);
        conf.setReducerClass(StateCheckReducer.class);

        FileInputFormat.setInputPaths(conf, vecPath, nextVectorPath);
        FileOutputFormat.setOutputPath(conf, null);

        conf.setNumReduceTasks(numOfReducers);

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);

        return conf;
    }
}