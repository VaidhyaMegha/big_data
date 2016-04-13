#!/bin/bash

hdfs dfs -rm -r /user/root
hdfs dfs -rm -r /user

hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/root

hdfs dfs -chmod g+w /user/root

source $SHELL_HOME/hbase_sample.sh