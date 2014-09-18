#!/bin/bash
# Running the famous wordcount example to verify installation
rm -rf input
mkdir input
echo "This is one line" >> input/file
echo "This is another line" >> input/file

# Add this directory to HDFS:
$HADOOP_HOME/bin/hdfs dfs -copyFromLocal input /input
# Run wordcount example provided:
$HADOOP_HOME/bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.*.jar wordcount /input /output

# Check the output:
echo "Word Count Output"
echo "-----------------"
$HADOOP_HOME/bin/hdfs dfs -cat /output/*