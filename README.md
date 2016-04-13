Big Data
========
The purpose of this project is to explore hadoop ecosystem (ex: MapReduce, Hive) and solve specific problems.

Scope
-----
* Graphs
    * 'Dynamic Connectivity' and 'Connected Components' problems
    * Implementing 'Weighted Quick Union with Path Compression' algorithm (for 'Dynamic Connectivity' problem) on Hadoop (as HIVE UDAF) 
        - module udaf, hql/custom_udaf.sql 
    * PEGASUS library - http://www.cs.cmu.edu/~pegasus/
* Hive
    * User Defined Functions
        - Handling rows with a header (udf) - module udf, hql/custom_udf.sql
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
    * Windowing and Analytics Functions
        - rank within a group
        - top 3 within a group
* Leverage Tez execution engine library (use "-e tez" option while running server and client commands)
* Data warehouse patterns (and possibly anti-patterns)
* MapReduce programs

How-to use/run this project
---------------------------
* Prerequisites:
    * Install JDK 6 or higher verssion
    * Download Hadoop tarball 
        - hadoop-2.7.1.tar.gz  from http://archive.apache.org/dist/hadoop/core/hadoop-2.7.1/
        - and extract contents to a folder say /home/sandeep/tools/hadoop/2.7.1
    * Download Hive tarball 
        - apache-hive-1.2.1-bin.tar.gz  from https://archive.apache.org/dist/hive/hive-1.2.1/
        - and extract contents to a folder say /home/sandeep/tools/hive/1.2.1
    * Download PEGASUS
        - from http://www.cs.cmu.edu/~pegasus/PEGASUSH-2.0.tar.gz
        - and extract contents to a folder say /home/sandeep/tools/PEGASUS
    * Install protobuf - http://www.confusedcoders.com/random/how-to-install-protocol-buffer-2-5-0-on-ubuntu-13-04
    * Clone Tez - https://github.com/apache/tez
        - and install it - http://tez.apache.org/install.htm
* Command to start hadoop
    
        sudo ./run.sh -m server -c start -e mr
* Command to run wordcount MapReduce program and get hdfs REPL
        
        sudo ./run.sh -m client -c mapreduce -e mr
* Command to run all hive scripts including connected components programs
        
        sudo ./run.sh -m client -c start -e mr
* Command to run GIMV-CC, PEGASUS CC and get PEGASUS REPL
        
        sudo ./run.sh -m client -c pegasus -e mr
* Command to stop hadoop
    
        sudo ./run.sh -m server -c stop -e mr

Technology Stack
----------------
* OS Version        : Ubuntu wily(15.10)
* Hadoop Version    : 2.7.1
* Hive Version      : 1.2.1
* HBase Version     : 0.98.18 (changed from 1.2.0 as hive support)
* Tez Version       : 0.8.2-SNAPSHOT
* PEGASUS           : 2.0

Algorithms & Papers
-------------------
* Weighted Quick Union With Path Compression : An attempt has been made to parallelize it in the form of a hive  UDAF.

        * Union Find - http://algs4.cs.princeton.edu/15uf/
        
* GIM-V based algorithms - http://www.cs.cmu.edu/~pegasus/        
                         
        * Paper - http://www.cs.cmu.edu/~ukang/papers/PegasusICDM2009.pdf
        * Getting Started - http://www.cs.cmu.edu/~pegasus/getting%20started.htm
        
* GIMV HBASE
        
        * Paper - Fast Graph Mining with HBase - http://datalab.snu.ac.kr/~ukang/papers/LeeSKunicorn.pdf
        
* 'MapReduce' - programming model
        
        *  Paper and presentation - http://research.google.com/archive/mapreduce.html

DataSets
--------
* India power generation stats for FEB-2013 from http://data.gov.in/
* Apache logs from http://tingri.me

TODO
----
* Kylin
* Giraph
* Drill
* More SQL patterns