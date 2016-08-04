package me.tingri.big_data.spark;

import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;

/**
 * Created by sandeepkunkunuru on 8/1/16.
 * SimpleApp.java
 */
public class SimpleApp {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Simple Application").setMaster("local[4]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> logData = sc.textFile(args[0]).cache();

        long numAs = logData.filter((Function<String, Boolean>) s -> s.contains("a")).count();

        long numBs = logData.filter((Function<String, Boolean>) s -> s.contains("b")).count();

        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
    }
}
