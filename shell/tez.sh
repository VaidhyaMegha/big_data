#!/bin/bash

hdfs dfs -rm -r /apps/tez-0.8.2-SNAPSHOT
hdfs dfs -rm -r /apps

hdfs dfs -mkdir /apps
hdfs dfs -mkdir /apps/tez-0.8.2-SNAPSHOT

hdfs dfs -rm -r /user/root
hdfs dfs -rm -r /user

hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/root

hdfs dfs -chmod g+w /user/root

hdfs dfs -copyFromLocal $TEZ_BINARIES/tez-0.8.2-SNAPSHOT.tar.gz /apps/tez-0.8.2-SNAPSHOT/tez-0.8.2-SNAPSHOT.tar.gz
hdfs dfs -chmod g+w /apps/tez-0.8.2-SNAPSHOT
