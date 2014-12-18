Big Data
========
* Problem Scope 
    * connected components of a graph (udaf) - module udaf, hql/custom_udaf.sql  
    * Handling rows with a header (udf) - module udf, hql/custom_udf.sql
    * rank within a group
    * top 3 within a group
    * All hive features and techniques
    * Leverage Tez execution engine library (use -tez option to run.sh to set tez)
    * Data warehouse patterns (and possibly anti-patterns)
    
Technology Stack
----------------
* OS Version : Ubuntu utopic(14.10)
* Hadoop Version : 2.4.1
* Hive Version : 0.13.1
* Tez Version : 0.5.1
* Giraph Version : 1.1.0

Algorithms
----------
* Weighted Quick Union With Path Compression : An attempt has been made to parallelize it in the form of a hive  UDAF.

Hive features
-------------
* DDL - Create, show, drop, alter
* Ingestion - Load, CTAS, Insert into Select 
* Serde - Regular Expression ex: Apache log
* Columnar - ORC and Vectorized execution.
* Select - count(*), distinct, nested select, filters
* Functions - UDF, UDTF, Custom UDF, Custom UDAF
* Aggregation functions - count, sum
* Windowing and analytic functions - row_number(), rank(), over with partition by and order by, 
* Export - overwrite local
* Unclassified Features - Lateral Views, Parameterizing scripts

DataSets
--------
* India power generation stats for FEB-2013 from http://data.gov.in/