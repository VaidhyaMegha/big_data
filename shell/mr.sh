#!/bin/bash
# Running the famous wordcount example to verify installation
rm -rf input
mkdir input
echo "This is one line" >> input/file
echo "This is another line" >> input/file

# Add this directory to HDFS:
hdfs dfs -copyFromLocal input /input
# Run wordcount example provided:
hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.*.jar wordcount /input /output

# Check the output:
echo "Word Count Output"
echo "-----------------"
hdfs dfs -cat /output/*