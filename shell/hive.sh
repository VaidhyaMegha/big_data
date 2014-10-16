#!/bin/bash
# Teardown
$HIVE_HOME/bin/hive -v -f $HQL_HOME/cleanup.sql
#Simple
$HIVE_HOME/bin/hive -v -f $HQL_HOME/simple_test.sql
# Serde
$HIVE_HOME/bin/hive -v -f $HQL_HOME/serde.sql
# UDTF
$HIVE_HOME/bin/hive -v -f $HQL_HOME/udtf.sql
# Custom UDF
cd $PROJECT_HOME/udf/
$MAVEN_HOME/bin/mvn clean assembly:assembly
cd $HADOOP_YARN_HOME
$HIVE_HOME/bin/hive -v -f $HQL_HOME/custom_udf.sql
# Custom UDAF
cd $PROJECT_HOME/udaf/
$MAVEN_HOME/bin/mvn clean assembly:assembly
cd $HADOOP_YARN_HOME
$HIVE_HOME/bin/hive -v -f $HQL_HOME/custom_udaf.sql
# Analytics
$HIVE_HOME/bin/hive -v -f $HQL_HOME/analytics.sql
# columnar
$HIVE_HOME/bin/hive -v -f $HQL_HOME/columnar.sql


