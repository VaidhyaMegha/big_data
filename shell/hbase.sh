#!/bin/bash

hdfs dfs -rm -r /user/root
hdfs dfs -rm -r /user

hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/root

hdfs dfs -chmod g+w /user/root

hdfs dfs -rm -r input
hdfs dfs -rm -r output

hdfs dfs -mkdir input
hdfs dfs -mkdir output

rm  sample_commands.txt
touch  sample_commands.txt
echo "
    create 'test', 'cf'
    list 'test'
    put 'test', 'row1', 'cf:a', 'value1'
    put 'test', 'row2', 'cf:b', 'value2'
    put 'test', 'row3', 'cf:c', 'value3'
    put 'test', 'row4', 'cf:d', 'value4'
    scan 'test'
    get 'test', 'row1'
    disable 'test'
    enable 'test'
    exit
    " >> sample_commands.txt

hbase shell sample_commands.txt

${HADOOP_HOME}/bin/hadoop jar ${HBASE_HOME}/lib/hbase-server-1.2.0.jar rowcounter test

echo "
    disable 'test'
    drop 'test'
    exit
    " > sample_commands.txt

hbase shell sample_commands.txt
rm  sample_commands.txt

hbase shell
