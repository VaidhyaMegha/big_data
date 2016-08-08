#!/bin/bash

hdfs dfs -rm -r /user/root
hdfs dfs -rm -r /user

hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/root

#hbase shell $PROJECT_HOME/hbase/src/main/scripts/filter.txt
#
#hadoop jar ${HBASE_HOME}/lib/hbase-server-0.98.18-hadoop2.jar rowcounter filter_example
#
#hbase shell $PROJECT_HOME/hbase/src/main/scripts/dynamic_schema.txt
#
#hdfs dfs -mkdir dyn_schema
#
#hdfs dfs -put $PROJECT_HOME/datasets/hbase/schema1.csv dyn_schema
#hdfs dfs -put $PROJECT_HOME/datasets/hbase/schema2.csv dyn_schema
#hdfs dfs -put $PROJECT_HOME/datasets/hbase/schema3.csv dyn_schema
#
#hbase org.apache.hadoop.hbase.mapreduce.ImportTsv -Dimporttsv.separator=, -Dimporttsv.columns=HBASE_ROW_KEY,cf1:1,cf1:2,col2,cf3:1,cf3:2 dynamic_schema dyn_schema/schema1.csv
#hbase org.apache.hadoop.hbase.mapreduce.ImportTsv -Dimporttsv.separator=, -Dimporttsv.columns=HBASE_ROW_KEY,cf1:1,cf1:2,cf3:1,cf3:2 dynamic_schema dyn_schema/schema2.csv
#hbase org.apache.hadoop.hbase.mapreduce.ImportTsv -Dimporttsv.separator=, -Dimporttsv.columns=HBASE_ROW_KEY,cf1:1,cf1:2,col2 dynamic_schema dyn_schema/schema3.csv

hbase shell $PROJECT_HOME/hbase/src/main/scripts/multi_level_filtering.txt

pushd $PROJECT_HOME/hbase/
mvn clean test
popd

#echo "
#    create 'edges', 'neighbors'
#    list 'edges'
#    exit
#    " > sample_commands.txt
#
#hbase shell sample_commands.txt
#
#hdfs dfs -mkdir cc_edge
#hdfs dfs -put $PEGASUS_HOME/catepillar_star.edge cc_edge
#
## hadoop jar $PROJECT_HOME/hbase-mr/target/hbase-mr-0.1-jar-with-dependencies.jar cc_edge edges 11 3 makesym
#
## value against default column in the column family will be overwritten resulting in only one neighbor per node
#hbase org.apache.hadoop.hbase.mapreduce.ImportTsv -Dimporttsv.columns=HBASE_ROW_KEY,neighbors edges  cc_edge
#
#echo "
#    scan 'edges'
#    disable 'edges'
#    drop 'edges'
#    exit
#    " > sample_commands.txt
#
#hbase shell sample_commands.txt

rm  sample_commands.txt

hbase shell
