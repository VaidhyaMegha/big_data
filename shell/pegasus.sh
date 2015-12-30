#!/bin/bash

hdfs dfs -rm -r /user/root
hdfs dfs -rm -r /user

hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/root

hdfs dfs -chmod g+w /user/root

# mr-gimv-cc
pushd $PROJECT_HOME/mr-gimv-cc/
mvn clean assembly:assembly
popd

pushd $PEGASUS_HOME

hdfs dfs -rm -r cc_edge
hdfs dfs -rm -r concmpt_new_output

hdfs dfs -mkdir cc_edge

hdfs dfs -put catepillar_star.edge cc_edge

hadoop jar $PROJECT_HOME/mr-gimv-cc/target/mr-gimv-cc-1.0-SNAPSHOT.jar me.tingri.graphs.cc.ConnectedComponents \
 cc_edge concmpt_new_output 16 3 makesym

make demo_ccmpt

echo "New Output"
echo "----------"
hadoop dfs -cat  concmpt_new_output/*

echo "Old Output"
echo "----------"
hadoop dfs -cat  concmpt_curbm/*

pegasus.sh

popd