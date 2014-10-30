#!/bin/bash

$HADOOP_HOME/bin/hdfs dfs -rm -r /apps/tez-0.5.1
$HADOOP_HOME/bin/hdfs dfs -rm -r /apps

$HADOOP_HOME/bin/hdfs dfs -mkdir /apps
$HADOOP_HOME/bin/hdfs dfs -mkdir /apps/tez-0.5.1

$HADOOP_HOME/bin/hdfs dfs -copyFromLocal $PROJECT_HOME/tez_binaries/tez-0.5.1.tar.gz /apps/tez-0.5.1/