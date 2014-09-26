#!/bin/bash
# Teardown
$HIVE_HOME/bin/hive -v -f $HQL_HOME/cleanup.sql
#Simple
echo "1321314314,4,http://www.page.com,http://www.referrer.com,10.200.13.110" >> input/page_view.csv
$HIVE_HOME/bin/hive -v -f $HQL_HOME/simple_test.sql
# UDTF
echo "a:d:e|z:y:q|1:s:p|6:6:r" >> input/strange_string.csv
echo "f:q:l|m:j:p|3:r:b" >> input/strange_string.csv
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


