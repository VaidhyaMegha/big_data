#!/bin/bash

hdfs dfs -rm -r /user/root
hdfs dfs -rm -r /user

hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/root

hdfs dfs -chmod g+w /user/root

# source $SHELL_HOME/hbase_sample.sh

if [ "$3" == "tez" ]; then
    # Teardown
    hive -v --hiveconf hive.execution.engine=tez -f  $HQL_HOME/hbase/cleanup.sql
    #Simple
    hive -v --hiveconf hive.execution.engine=tez -f $HQL_HOME/hbase/hbase.sql
else
    # Teardown
    hive -v -f $HQL_HOME/hbase/cleanup.sql
    # jline error
    # http://stackoverflow.com/questions/28997441/hive-startup-error-terminal-initialization-failed-falling-back-to-unsupporte
    # https://cwiki.apache.org/confluence/display/Hive/Hive+on+Spark%3A+Getting+Started
    # hive

    # Simple
    hive -v -f $HQL_HOME/hbase/hbase.sql
fi

hbase shell

