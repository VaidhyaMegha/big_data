#!/bin/bash

# Teardown
hive -v -f $HQL_HOME/cleanup.sql

############
# stop HBase
############
#stop-hbase.sh

############
# stop Kylin
############
#kylin.sh stop

hadoop-daemon.sh stop namenode
hadoop-daemon.sh stop datanode
yarn-daemon.sh stop resourcemanager
yarn-daemon.sh stop nodemanager
mr-jobhistory-daemon.sh stop historyserver

