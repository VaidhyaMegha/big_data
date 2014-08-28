#!/bin/bash
############
# Name node:
############
# starting namenode, logging to $HADOOP_HOME/logs/hadoop-$USER-namenode-pc3-laptop.out
sbin/hadoop-daemon.sh start namenode
jps
############
# Data node:
############
# starting datanode, logging to $HADOOP_HOME/logs/hadoop-$USER-datanode-pc3-laptop.out
sbin/hadoop-daemon.sh start datanode
jps

## 7. Start Hadoop Map-Reduce Processes

############
# Resource Manager:
############
# starting resourcemanager, logging to $HADOOP_HOME/logs/yarn-$USER-resourcemanager-pc3-laptop.out
sbin/yarn-daemon.sh start resourcemanager
jps
############
# Node Manager:
############
# starting nodemanager, logging to $HADOOP_HOME/logs/yarn-$USER-nodemanager-pc3-laptop.out
sbin/yarn-daemon.sh start nodemanager
jps
############
# Job History Server:
############
# starting historyserver, logging to $HADOOP_HOME/logs/yarn-$USER-historyserver-pc3-laptop.out
sbin/mr-jobhistory-daemon.sh start historyserver
jps
# 18509 Jps
# 17107 NameNode
# 17170 DataNode
# 17252 ResourceManager
# 17309 NodeManager
# 17626 JobHistoryServer
