#!/bin/bash

$HADOOP_HOME/bin/hdfs dfs -rm -r /apps/tez-0.4.0
$HADOOP_HOME/bin/hdfs dfs -rm -r /apps

$HADOOP_HOME/bin/hdfs dfs -mkdir /apps
$HADOOP_HOME/bin/hdfs dfs -mkdir /apps/tez-0.4.0

$HADOOP_HOME/bin/hdfs dfs -rm -r /user/root
$HADOOP_HOME/bin/hdfs dfs -rm -r /user

$HADOOP_HOME/bin/hdfs dfs -mkdir /user
$HADOOP_HOME/bin/hdfs dfs -mkdir /user/root

$HADOOP_HOME/bin/hdfs dfs -chmod g+w /user/root

$HADOOP_HOME/bin/hdfs dfs -copyFromLocal $PROJECT_HOME/tez_binaries/*.jar /apps/tez-0.4.0
$HADOOP_HOME/bin/hdfs dfs -copyFromLocal $PROJECT_HOME/tez_binaries/lib/*.jar /apps/tez-0.4.0
$HADOOP_HOME/bin/hdfs dfs -chmod g+w /apps/tez-0.4.0
