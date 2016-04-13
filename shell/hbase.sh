#!/bin/bash

hdfs dfs -rm -r /user/root
hdfs dfs -rm -r /user

hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/root

hdfs dfs -chmod g+w /user/root

rm  sample_commands.txt

echo "
    create 'edges', 'neighbors'
    list 'edges'
    exit
    " >> sample_commands.txt

hbase shell sample_commands.txt

# hdfs dfs -mkdir cc_edge
# hdfs dfs -put $PEGASUS_HOME/catepillar_star.edge cc_edge

# hadoop jar $PROJECT_HOME/hbase-mr/target/hbase-mr-0.1-jar-with-dependencies.jar cc_edge edges 11 3 makesym

rm  sample_commands.txt

echo "
    disable 'edges'
    drop 'edges'
    exit
    " > sample_commands.txt

hbase shell sample_commands.txt

rm  sample_commands.txt

echo "
    create 'test', 'cf'
    list 'test'
    put 'test', 'row1', 'cf:a', 'value1'
    put 'test', 'row2', 'cf:b', 'value2'
    put 'test', 'row3', 'cf:c', 'value3'
    put 'test', 'row4', 'cf:d', 'value4'
    scan 'test'
    get 'test', 'row1'
    exit
    " >> sample_commands.txt

hbase shell sample_commands.txt

hadoop jar ${HBASE_HOME}/lib/hbase-server-1.2.0.jar rowcounter test

echo "
    disable 'test'
    drop 'test'
    exit
    " > sample_commands.txt

hbase shell sample_commands.txt

rm sample_commands.txt
