#!/bin/bash
# steps to install Hadoop 2.x release on single node cluster setup
# Prerequisites:
# Java 6 installed
echo "###############"
echo "Setup"
echo "###############"
# Download tarball and extract contents to say /home/sandeep/tools/hadoop/2.4.1
# Setup Environment Variables
# Add these to your ~/.bashrc or other shell start-up script
export USER=sandeep
export PROJECT_HOME=/home/$USER/projects/big_data
export SHELL_HOME=$PROJECT_HOME/shell
export HQL_HOME=$PROJECT_HOME/hql

source $SHELL_HOME/env.sh

cd $HADOOP_ROOT
sudo chown -R $USER $HDP_VER

# Create two directories to be used by namenode and datanode.
rm -rf $NAME_NODE_DATA_DIR
rm -rf $DATA_NODE_DATA_DIR

mkdir -p $NAME_NODE_DATA_DIR
mkdir -p $DATA_NODE_DATA_DIR

# Set up config files
cd $HADOOP_YARN_HOME
source $SHELL_HOME/config.sh

echo "###############"
echo "# Start HDFS processes"
echo "###############"
source $SHELL_HOME/start.sh

echo "###############"
echo "# Web interface"
echo "###############"
# Browse HDFS and check health using http://localhost:50070 in the browser:
xdg-open http://localhost:50070
# You can check the status of the applications running using the following URL:
xdg-open http://localhost:8088

echo "##############"
echo "# Map Reduce"
echo "##############"
# 8. Running the famous wordcount example to verify installation
rm -rf input 
mkdir input
echo "This is one line" >> input/file
echo "This is another line" >> input/file
# Add this directory to HDFS:
bin/hdfs dfs -copyFromLocal input /input
# Run wordcount example provided:
bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.*.jar wordcount /input /output
# Check the output:
echo "Word Count Output"
echo "-----------------"
bin/hdfs dfs -cat /output/*

echo "#######"
echo "# HIVE"
echo "#######"
# Setup
$HIVE_HOME/bin/hive -v -f $HQL_HOME/cleanup.sql
#Simple
echo "1321314314,4,http://www.page.com,http://www.referrer.com,10.200.13.110" >> input/page_view.csv
#$HIVE_HOME/bin/hive -v -f $HQL_HOME/simple_test.sql
# UDTF
echo "a:d:e|z:y:q|1:s:p|6:6:r" >> input/strange_string.csv
echo "f:q:l|m:j:p|3:r:b" >> input/strange_string.csv
#$HIVE_HOME/bin/hive -v -f $HQL_HOME/udtf.sql
# Custom UDF
cd $PROJECT_HOME/udf/
$MAVEN_HOME/bin/mvn clean assembly:assembly
cd $HADOOP_YARN_HOME
#$HIVE_HOME/bin/hive -v -f $HQL_HOME/custom_udf.sql
# Custom UDAF
cd $PROJECT_HOME/udaf/
$MAVEN_HOME/bin/mvn clean assembly:assembly
cd $HADOOP_YARN_HOME
$HIVE_HOME/bin/hive -v -f $HQL_HOME/custom_udaf.sql
# Analytics
#$HIVE_HOME/bin/hive -v -f $HQL_HOME/analytics.sql
# Teardown
$HIVE_HOME/bin/hive -v -f $HQL_HOME/cleanup.sql

echo "########################"
echo "# Stop the processes"
echo "########################"
source $SHELL_HOME/stop.sh