#!/bin/bash

# Teardown
$HIVE_HOME/bin/hive -v -f $HQL_HOME/cleanup.sql

sbin/hadoop-daemon.sh stop namenode
sbin/hadoop-daemon.sh stop datanode
sbin/yarn-daemon.sh stop resourcemanager
sbin/yarn-daemon.sh stop nodemanager
sbin/mr-jobhistory-daemon.sh stop historyserver