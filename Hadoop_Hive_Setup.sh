#!/bin/bash
# steps to install Hadoop 2.x release on single node cluster setup
# Prerequisites:
# Java 6 installed

# 1. Download tarball and extract contents to say /home/sandeep/tools/hadoop/2.4.1
# 2. Setup Environment Variables
# Add these to your ~/.bashrc or other shell start-up script
export USER=sandeep
export PROJECT_HOME=/home/$USER/projects/big_data

source $PROJECT_HOME/env.sh

cd $HADOOP_ROOT
sudo chown -R $USER $HDP_VER

# 3. Create directories

# Create two directories to be used by namenode and datanode.

rm -rf $NAME_NODE_DATA_DIR
rm -rf $DATA_NODE_DATA_DIR

mkdir -p $NAME_NODE_DATA_DIR
mkdir -p $DATA_NODE_DATA_DIR

# 4. Set up config files
cd $HADOOP_YARN_HOME

source $PROJECT_HOME/config.sh

# 6. Start HDFS processes
source $PROJECT_HOME/start.sh

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
bin/hdfs dfs -cat /output/*
# This 2
# another 1
# is 2
# line 1
# one 2

# 9. Web interface
# Browse HDFS and check health using http://localhost:50070 in the browser:
xdg-open http://localhost:50070
# You can check the status of the applications running using the following URL:
xdg-open http://localhost:8088

#######
# HIVE
#######
# Setup
$HIVE_HOME/bin/hive -v -f $PROJECT_HOME/Hive_Cleanup.sql

#Simple
echo "1321314314,4,http://www.page.com,http://www.referrer.com,10.200.13.110" >> input/page_view.csv
$HIVE_HOME/bin/hive -v -f $PROJECT_HOME/Hive_Test_Simple.sql

# UDTF and custom udf
echo "a:d:e|z:y:q|1:s:p|6:6:r" >> input/strange_string.csv
echo "f:q:l|m:j:p|3:r:b" >> input/strange_string.csv
$HIVE_HOME/bin/hive -v -f $PROJECT_HOME/Hive_Test_UDTF.sql

cd $PROJECT_HOME/udf/
$MAVEN_HOME/bin/mvn clean assembly:assembly
cd $HADOOP_YARN_HOME

$HIVE_HOME/bin/hive -v -f $PROJECT_HOME/Hive_Custom_UDF.sql

$HIVE_HOME/bin/hive -v -f $PROJECT_HOME/Hive_Test_Analytics.sql

# Teardown
$HIVE_HOME/bin/hive -v -f $PROJECT_HOME/Hive_Cleanup.sql

#######
# 10. Stop the processes
#######
source $PROJECT_HOME/stop.sh