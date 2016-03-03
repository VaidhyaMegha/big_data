* Tool interface for MR - https://hadoopi.wordpress.com/2013/06/05/hadoop-implementing-the-tool-interface-for-mapreduce-driver/
* MapReduce Counter - http://diveintodata.org/2011/03/15/an-example-of-hadoop-mapreduce-counter/

* Graph eccentricity - http://mathworld.wolfram.com/GraphEccentricity.html

**Tez -- Changes in retrieving Counter**

* MapReduce on TEZ - my counter not available after job - https://mail-archives.apache.org/mod_mbox/incubator-tez-user/201405.mbox/%3C18954C73-3121-4AAF-9D9F-3D96C6F8042B@apache.org%3E
* Tez compatibility with MR - http://mail-archives.apache.org/mod_mbox/incubator-tez-dev/201401.mbox/%3C7342DCA7-6576-4595-A8D5-AAA8EADD5EF6%40apache.org%3E
* Tez Counters - https://cwiki.apache.org/confluence/display/Hive/Hive+on+Tez#HiveonTez-Counters
        
        API for retrieving counters will be different in Tez and we will thus add a shim api for that.
        Incrementing counters at execution time will work unchanged.
