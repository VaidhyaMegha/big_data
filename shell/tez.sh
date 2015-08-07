#!/bin/bash

hdfs dfs -rm -r /apps/tez-0.4.0
hdfs dfs -rm -r /apps

hdfs dfs -mkdir /apps
hdfs dfs -mkdir /apps/tez-0.4.0

hdfs dfs -rm -r /user/root
hdfs dfs -rm -r /user

hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/root

hdfs dfs -chmod g+w /user/root

hdfs dfs -copyFromLocal $PROJECT_HOME/tez_binaries/*.jar /apps/tez-0.4.0
hdfs dfs -copyFromLocal $PROJECT_HOME/tez_binaries/lib/*.jar /apps/tez-0.4.0
hdfs dfs -chmod g+w /apps/tez-0.4.0
