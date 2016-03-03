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


hbase shell
