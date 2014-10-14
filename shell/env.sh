#!/bin/bash
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
export MAVEN_HOME=/home/$USER/tools/maven
export DATA_SETS_FOLDER=/home/$USER/datasets

export HDP_VER=2.4.1
export HIVE_VER=0.13.1

export HADOOP_ROOT=/home/$USER/tools/hadoop
export HADOOP_HOME=$HADOOP_ROOT/$HDP_VER
export HIVE_HOME=/home/$USER/tools/hive/$HIVE_VER

export HADOOP_MAPRED_HOME=$HADOOP_HOME
export HADOOP_COMMON_HOME=$HADOOP_HOME
export HADOOP_HDFS_HOME=$HADOOP_HOME
export HADOOP_YARN_HOME=$HADOOP_HOME
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib"

export TEZ_CONF_DIR=$HADOOP_CONF_DIR
export TEZ_JARS=$PROJECT_HOME/tez_binaries/tez_jars
export HADOOP_CLASSPATH=${TEZ_CONF_DIR}:${TEZ_JARS}/*:${TEZ_JARS}/lib/*

export NAME_NODE_DATA_DIR=/home/$USER/tools/hadoop/data/hdfs/namenode
export DATA_NODE_DATA_DIR=/home/$USER/tools/hadoop/data/hdfs/datanode

export HQL_HOME=$PROJECT_HOME/hql