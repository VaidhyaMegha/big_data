#!/bin/bash
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
export TOOLS_HOME=/home/$USER/tools
export MAVEN_HOME=$TOOLS_HOME/maven
export DATA_SETS_FOLDER=$PROJECT_HOME/datasets

export HDP_VER=2.4.1
export HIVE_VER=0.13.1

export HADOOP_ROOT=$TOOLS_HOME/hadoop
export HADOOP_HOME=$HADOOP_ROOT/$HDP_VER
export HIVE_HOME=$TOOLS_HOME/hive/$HIVE_VER

export HADOOP_MAPRED_HOME=$HADOOP_HOME
export HADOOP_COMMON_HOME=$HADOOP_HOME
export HADOOP_HDFS_HOME=$HADOOP_HOME
export HADOOP_YARN_HOME=$HADOOP_HOME
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib"

export GIRAPH_HOME=$TOOLS_HOME/giraph/giraph-dist/target/giraph-1.2.0-SNAPSHOT-for-hadoop-2.1.1-SNAPSHOT-bin/giraph-1.2.0-SNAPSHOT-for-hadoop-2.1.1-SNAPSHOT
export GIRAPH_JARS=$(echo "$GIRAPH_HOME"/*.jar | tr ' ' ':'):$(echo "$GIRAPH_HOME"/lib/*.jar | tr ' ' ':')

export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar:${GIRAPH_JARS}:${HADOOP_CLASSPATH}

export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$GIRAPH_HOME/bin:$PATH

export NAME_NODE_DATA_DIR=$TOOLS_HOME/hadoop/data/hdfs/namenode
export DATA_NODE_DATA_DIR=$TOOLS_HOME/hadoop/data/hdfs/datanode

export HQL_HOME=$PROJECT_HOME/hql

if [ "$3" == "tez" ]; then
    export TEZ_INSTALL_DIR=$PROJECT_HOME/tez_binaries
    export TEZ_JARS=$(echo "$TEZ_INSTALL_DIR"/*.jar | tr ' ' ':'):$(echo "$TEZ_INSTALL_DIR"/lib/*.jar | tr ' ' ':')

    if [ -z "$HIVE_AUX_JARS_PATH" ]; then
        export HIVE_AUX_JARS_PATH="$TEZ_JARS"
    else
        export HIVE_AUX_JARS_PATH="$HIVE_AUX_JARS_PATH:$TEZ_JARS"
    fi
    export TEZ_CONF_DIR=$HADOOP_CONF_DIR
    export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar:${TEZ_JARS}
fi


