#!/bin/bash
############
# Name node:
############
# starting namenode, logging to $HADOOP_HOME/logs/hadoop-$USER-namenode-sandeep-Latitude-6430U-laptop.out
hadoop-daemon.sh start namenode
jps
############
# Data node:
############
# starting datanode, logging to $HADOOP_HOME/logs/hadoop-$USER-datanode-sandeep-Latitude-6430U-laptop.out
hadoop-daemon.sh start datanode
jps

## 7. Start Hadoop Map-Reduce Processes

############
# Resource Manager:
############
# starting resourcemanager, logging to $HADOOP_HOME/logs/yarn-$USER-resourcemanager-sandeep-Latitude-6430U-laptop.out
yarn-daemon.sh start resourcemanager
jps
############
# Node Manager:
############
# starting nodemanager, logging to $HADOOP_HOME/logs/yarn-$USER-nodemanager-sandeep-Latitude-6430U-laptop.out
yarn-daemon.sh start nodemanager
jps
############
# Job History Server:
############
# starting historyserver, logging to $HADOOP_HOME/logs/yarn-$USER-historyserver-sandeep-Latitude-6430U-laptop.out
mr-jobhistory-daemon.sh start historyserver
jps
# 18509 Jps
# 17107 NameNode
# 17170 DataNode
# 17252 ResourceManager
# 17309 NodeManager
# 17626 JobHistoryServer

############
# Start HBase
############
start-hbase.sh

############
# Start Kylin
############
kylin.sh start