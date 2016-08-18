#!/bin/bash

# Teardown
hive -v -f $HQL_HOME/cleanup.sql
hive -v -f $HQL_HOME/hbase/cleanup.sql

############
# stop HBase
############
#local-regionservers.sh stop 2 3 4 5
stop-hbase.sh

############
# stop Kylin
############
#kylin.sh stop

hadoop-daemon.sh stop namenode
hadoop-daemon.sh stop datanode
yarn-daemon.sh stop resourcemanager
yarn-daemon.sh stop nodemanager
mr-jobhistory-daemon.sh stop historyserver
kafka-server-stop.sh
