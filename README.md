Big Data
========
* Problem Scope 
    * connected components of a graph (udaf) - module udaf, hql/custom_udaf.sql  
    * Handling rows with a header (udf) - module udf, hql/custom_udf.sql
    * rank within a group
    * top 3 within a group
    * All hive features and techniques
    * Data warehouse patterns (and possibly anti-patterns)
* Configuration
    * OS Version : Ubuntu utopic(14.10)
    
Hadoop
------
* Configuration
    * Version : 2.4.1

Hive
----
* Configuration
    * Version : 0.13.1
* DDL - Create, show, drop, alter
* Ingestion - Load, CTAS, Insert into Select 
* Select - count(*), distinct, nested select, filters
* Functions - UDF, UDTF, Custom UDF, Custom UDAF
* Aggregation functions - count, sum
* Windowing and analytic functions - row_number(), rank(), over with partition by and order by, 
* Export - overwrite local
* Unclassified Features - Lateral Views, Parameterizing scripts

DataSets
--------
* India power generation stats for FEB-2013 from http://data.gov.in/

References
------------
* Setup - http://raseshmori.wordpress.com/2012/09/23/install-hadoop-2-0-1-yarn-nextgen/
* Setup - https://cwiki.apache.org/confluence/display/Hive/AdminManual+Installation
* Setup - http://hadoop.apache.org/docs/r2.4.1/hadoop-project-dist/hadoop-common/ClusterSetup.html
* Subqueries - https://cwiki.apache.org/confluence/display/Hive/LanguageManual+SubQueries
* Array Operations - http://stackoverflow.com/questions/8039751/hadoop-hive-query-to-split-one-column-into-several-ones
* Array Operations - http://stackoverflow.com/questions/17212623/project-array-to-columns-in-hive
* UDTF - http://stackoverflow.com/questions/12160304/hadoop-hive-split-a-single-row-into-multiple-rows
* UDTF - https://cwiki.apache.org/confluence/display/Hive/LanguageManual+UDF#LanguageManualUDF-Built-inTable-GeneratingFunctions(UDTF)
* UDTF - http://stackoverflow.com/questions/11373543/explode-the-array-of-struct-in-hive
* Lateral Views - https://cwiki.apache.org/confluence/display/Hive/LanguageManual+LateralView
* Word Count in hive - http://stackoverflow.com/questions/10039949/word-count-program-in-hive
* Custom UDFs - https://github.com/rathboma/hive-extension-examples
* Custom UDFs - https://cwiki.apache.org/confluence/display/Hive/HivePlugins
* Analytics - http://www.postgresql.org/docs/9.1/static/tutorial-window.html
* Analytics - https://cwiki.apache.org/confluence/display/Hive/LanguageManual+WindowingAndAnalytics
* Analytics - http://www.slideshare.net/Hadoop_Summit/analytical-queries-with-hive
* Parameterizing scripts - http://stackoverflow.com/questions/12464636/how-to-set-variables-in-hive-scripts
* Permanent Functions - https://cwiki.apache.org/confluence/display/Hive/LanguageManual+DDL#LanguageManualDDL-PermanentFunctions
* Error logs - https://cwiki.apache.org/confluence/display/Hive/GettingStarted#GettingStarted-ErrorLogs
* UDAF - http://ragrawal.wordpress.com/2013/10/26/writing-hive-custom-aggregate-functions-udaf-part-ii/
* UDAF - https://cwiki.apache.org/confluence/display/Hive/GenericUDAFCaseStudy#GenericUDAFCaseStudy-WritingGenericUDAFs:ATutorial
* UDAF - https://cwiki.apache.org/confluence/display/Hive/LanguageManual+UDF#LanguageManualUDF-Built-inAggregateFunctions(UDAF)
* Source shell scripts - http://bash.cyberciti.biz/guide/Source_command
* Source shell scripts - http://stackoverflow.com/questions/670191/getting-a-source-not-found-error-when-using-source-in-a-bash-script
* Redirecting standard output, error to log - http://stackoverflow.com/questions/4721635/redirect-standard-output-error-to-log-file
* source vs sh - http://stackoverflow.com/questions/13786499/source-vs-sh-in-linux-what-is-the-difference
* Git - http://stackoverflow.com/questions/173919/is-there-a-theirs-version-of-git-merge-s-ours