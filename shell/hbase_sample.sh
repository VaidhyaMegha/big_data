#!/bin/bash

hbase shell hbase/filter.txt

hadoop jar ${HBASE_HOME}/lib/hbase-server-1.2.0.jar rowcounter test

echo "
    disable 'test'
    drop 'test'
    exit
    " > sample_commands.txt

hbase shell sample_commands.txt

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
