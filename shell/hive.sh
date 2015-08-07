#!/bin/bash
if [ "$3" == "tez" ]; then
    # Teardown
    hive -v --hiveconf hive.execution.engine=tez -f  $HQL_HOME/cleanup.sql
    #Simple
    hive -v --hiveconf hive.execution.engine=tez -f $HQL_HOME/simple_test.sql
    # Serde
    hive -v --hiveconf hive.execution.engine=tez -f $HQL_HOME/serde.sql
    # UDTF
    hive -v --hiveconf hive.execution.engine=tez -f $HQL_HOME/udtf.sql
    # Custom UDF
    pushd $PROJECT_HOME/udf/
    mvn clean assembly:assembly
    popd
    hive -v --hiveconf hive.execution.engine=tez -f $HQL_HOME/custom_udf.sql
    # Custom UDAF
    pushd $PROJECT_HOME/udaf/
    mvn clean assembly:assembly
    popd
    hive -v --hiveconf hive.execution.engine=tez -f $HQL_HOME/custom_udaf.sql
    # Analytics
    hive -v --hiveconf hive.execution.engine=tez -f $HQL_HOME/analytics.sql
    # columnar
    hive -v --hiveconf hive.execution.engine=tez -f $HQL_HOME/columnar.sql
else
    # Teardown
    hive -v -f $HQL_HOME/cleanup.sql
    #Simple
    hive -v -f $HQL_HOME/simple_test.sql
    # Serde
    hive -v -f $HQL_HOME/serde.sql
    # UDTF
    hive -v -f $HQL_HOME/udtf.sql
    # Custom UDF
    pushd $PROJECT_HOME/udf/
    mvn clean assembly:assembly
    popd
    hive -v -f $HQL_HOME/custom_udf.sql
    # Custom UDAF
    pushd $PROJECT_HOME/udaf/
    mvn clean assembly:assembly
    popd
    hive -v -f $HQL_HOME/custom_udaf.sql
    # Analytics
    hive -v -f $HQL_HOME/analytics.sql
    # columnar
    hive -v -f $HQL_HOME/columnar.sql
fi