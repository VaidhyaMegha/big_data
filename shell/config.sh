#!/bin/bash
# Add the following properties under configuration tag in the files mentioned below:
#  $HADOOP_CONF_DIR/yarn-site.xml
rm  $HADOOP_CONF_DIR/yarn-site.xml
touch  $HADOOP_CONF_DIR/yarn-site.xml
echo "<?xml version=\"1.0\"?>
<configuration>
<property>
   <name>yarn.nodemanager.aux-services</name>
   <value>mapreduce_shuffle</value>
</property>
<property>
   <name>yarn.nodemanager.aux-services.mapreduce.shuffle.class</name>
   <value>org.apache.hadoop.mapred.ShuffleHandler</value>
</property>
</configuration>"  >>  $HADOOP_CONF_DIR/yarn-site.xml

#  $HADOOP_CONF_DIR/core-site.xml
rm  $HADOOP_CONF_DIR/core-site.xml
touch  $HADOOP_CONF_DIR/core-site.xml
echo "<?xml version=\"1.0\"?>
<configuration>
<property>
   <name>fs.defaultFS</name>
   <value>hdfs://localhost:9000</value>
</property>
</configuration>"  >>  $HADOOP_CONF_DIR/core-site.xml

#  $HADOOP_CONF_DIR/hdfs-site.xml
rm  $HADOOP_CONF_DIR/hdfs-site.xml
touch  $HADOOP_CONF_DIR/hdfs-site.xml
echo "<?xml version=\"1.0\"?>
<configuration>
 <property>
   <name>dfs.replication</name>
   <value>1</value>
 </property>
 <property>
   <name>dfs.namenode.name.dir</name>
   <value>file://$NAME_NODE_DATA_DIR</value>
 </property>
 <property>
   <name>dfs.datanode.data.dir</name>
   <value>file://$DATA_NODE_DATA_DIR</value>
 </property>
</configuration>" >>  $HADOOP_CONF_DIR/hdfs-site.xml

#  $HADOOP_CONF_DIR/mapred-site.xml:
rm  $HADOOP_CONF_DIR/mapred-site.xml
touch  $HADOOP_CONF_DIR/mapred-site.xml
echo "<?xml version=\"1.0\"?>
<configuration>
   <property>
      <name>mapreduce.framework.name</name>
      <value>yarn</value>
   </property>
</configuration>" >>  $HADOOP_CONF_DIR/mapred-site.xml

# 5. Format namenode
# This step is needed only for the first time. Doing it every time will result in loss of content on HDFS.
bin/hdfs namenode -format
