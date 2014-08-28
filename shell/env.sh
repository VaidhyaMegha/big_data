#!/bin/bash
export HDP_VER=2.4.1
export HIVE_VER=0.13.1
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
export HADOOP_ROOT=/home/$USER/tools/hadoop
export MAVEN_HOME=/home/sandeep/tools/maven
export HADOOP_HOME=$HADOOP_ROOT/$HDP_VER
export HADOOP_MAPRED_HOME=$HADOOP_HOME
export HADOOP_COMMON_HOME=$HADOOP_HOME
export HADOOP_HDFS_HOME=$HADOOP_HOME
export HADOOP_YARN_HOME=$HADOOP_HOME
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export HIVE_HOME=/home/$USER/tools/hive/$HIVE_VER
export NAME_NODE_DATA_DIR=/home/$USER/tools/hadoop/data/hdfs/namenode
export DATA_NODE_DATA_DIR=/home/$USER/tools/hadoop/data/hdfs/datanode
