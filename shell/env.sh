#!/bin/bash
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export TOOLS_HOME=/home/$USER/tools
export MAVEN_HOME=$TOOLS_HOME/maven
export TEZ_BINARIES=$PROJECT_HOME/tez_binary_files
export DATA_SETS_FOLDER=$PROJECT_HOME/datasets

export HDP_VER=2.7.1
export HIVE_VER=1.2.1
export TEZ_VER=0.8.2-SNAPSHOT
export HBASE_VER=1.2.0
export KYLIN_VER=1.1.1

export HADOOP_ROOT=$TOOLS_HOME/hadoop
export HADOOP_HOME=$HADOOP_ROOT/$HDP_VER
export HIVE_HOME=$TOOLS_HOME/hive/$HIVE_VER
export PEGASUS_HOME=$TOOLS_HOME/PEGASUS
export HBASE_HOME=$TOOLS_HOME/hbase/$HBASE_VER
export KYLIN_HOME=$TOOLS_HOME/kylin/$KYLIN_VER


export HADOOP_MAPRED_HOME=$HADOOP_HOME
export HADOOP_COMMON_HOME=$HADOOP_HOME
export HADOOP_HDFS_HOME=$HADOOP_HOME
export HADOOP_YARN_HOME=$HADOOP_HOME
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export TEZ_CONF_DIR=$HADOOP_CONF_DIR
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib"

#export GIRAPH_HOME=$TOOLS_HOME/giraph/giraph-dist/target/giraph-1.2.0-SNAPSHOT-for-hadoop-2.5.1-bin/giraph-1.2.0-SNAPSHOT-for-hadoop-2.5.1
#export GIRAPH_JARS=:$(echo "$GIRAPH_HOME"/*.jar | tr ' ' ':'):$(echo "$GIRAPH_HOME"/lib/*.jar | tr ' ' ':')

#export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar:${GIRAPH_JARS}:${HADOOP_CLASSPATH}
export HBASE_CLASSPATH=${HBASE_HOME}/lib/*:${HBASE_CLASSPATH}
export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar:${HBASE_CLASSPATH}:${HADOOP_CLASSPATH}
export HADOOP_CLASSPATH=`${HBASE_HOME}/bin/hbase classpath`

#export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin:$GIRAPH_HOME/bin:$HBASE_HOME/bin:$KYLIN_HOME/bin:$PATH
export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin:$HBASE_HOME/bin:$PEGASUS_HOME:$PATH

export NAME_NODE_DATA_DIR=$TOOLS_HOME/hadoop/data/hdfs/namenode
export DATA_NODE_DATA_DIR=$TOOLS_HOME/hadoop/data/hdfs/datanode
export ZOOKEEPER_DATA_DIR=$TOOLS_HOME/hadoop/data/zookeper

export HQL_HOME=$PROJECT_HOME/hql

if [ "$3" == "tez" ]; then
    export TEZ_JARS=$(echo "$TEZ_BINARIES"/*.jar | tr ' ' ':'):$(echo "$TEZ_BINARIES"/lib/*.jar | tr ' ' ':')

    if [ -z "$HIVE_AUX_JARS_PATH" ]; then
        export HIVE_AUX_JARS_PATH="$TEZ_JARS"
    else
        export HIVE_AUX_JARS_PATH="$HIVE_AUX_JARS_PATH:$TEZ_JARS"
    fi

    export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar:${TEZ_CONF_DIR}:${TEZ_JARS}
fi


