# References : http://raseshmori.wordpress.com/2012/09/23/install-hadoop-2-0-1-yarn-nextgen/
#
# steps to install Hadoop 2.x release on single node cluster setup

# Prerequisites:
# Java 6 installed

# 1. Download tarball and extract contents to say /home/sandeep/tools/hadoop/2.4.1

export USER=sandeep
export HDP_VER=2.4.1
export HIVE_VER=0.13.1
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
export HADOOP_ROOT=/home/$USER/tools/hadoop

cd $HADOOP_ROOT
sudo chown -R $USER $HDP_VER

# 2. Setup Environment Variables

export HADOOP_HOME=$HADOOP_ROOT/$HDP_VER
export HADOOP_MAPRED_HOME=$HADOOP_HOME
export HADOOP_COMMON_HOME=$HADOOP_HOME
export HADOOP_HDFS_HOME=$HADOOP_HOME
export HADOOP_YARN_HOME=$HADOOP_HOME
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop

export HIVE_HOME=/home/$USER/tools/hive/$HIVE_VER

# Add these to your ~/.bashrc or other shell start-up script

# 3. Create directories

# Create two directories to be used by namenode and datanode.

export NAME_NODE_DATA_DIR=/home/$USER/tools/hadoop/data/hdfs/namenode
export DATA_NODE_DATA_DIR=/home/$USER/tools/hadoop/data/hdfs/datanode

rm -rf $NAME_NODE_DATA_DIR
rm -rf $DATA_NODE_DATA_DIR
mkdir -p $NAME_NODE_DATA_DIR
mkdir -p $DATA_NODE_DATA_DIR

# 4. Set up config files

cd $HADOOP_YARN_HOME

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

# 6. Start HDFS processes

############
# Name node:
############
sbin/hadoop-daemon.sh start namenode
# starting namenode, logging to $HADOOP_HOME/logs/hadoop-$USER-namenode-pc3-laptop.out

jps
# 18509 Jps
#17107 NameNode

############
# Data node:
############

sbin/hadoop-daemon.sh start datanode
# starting datanode, logging to $HADOOP_HOME/logs/hadoop-$USER-datanode-pc3-laptop.out

jps
# 18509 Jps
# 17107 NameNode
# 17170 DataNode


## 7. Start Hadoop Map-Reduce Processes

############
# Resource Manager:
############

sbin/yarn-daemon.sh start resourcemanager
# starting resourcemanager, logging to $HADOOP_HOME/logs/yarn-$USER-resourcemanager-pc3-laptop.out

jps
# 18509 Jps
# 17107 NameNode
# 17170 DataNode
# 17252 ResourceManager

############
# Node Manager:
############

sbin/yarn-daemon.sh start nodemanager
# starting nodemanager, logging to $HADOOP_HOME/logs/yarn-$USER-nodemanager-pc3-laptop.out

jps
# 18509 Jps
# 17107 NameNode
# 17170 DataNode
# 17252 ResourceManager
# 17309 NodeManager

############
# Job History Server:
############

sbin/mr-jobhistory-daemon.sh start historyserver
# starting historyserver, logging to $HADOOP_HOME/logs/yarn-$USER-historyserver-pc3-laptop.out

jps
# 18509 Jps
# 17107 NameNode
# 17170 DataNode
# 17252 ResourceManager
# 17309 NodeManager
# 17626 JobHistoryServer

# 8. Running the famous wordcount example to verify installation

rm -rf input 
mkdir input

echo "This is one line" >> input/file
echo "This is another line" >> input/file

# Add this directory to HDFS:

bin/hdfs dfs -copyFromLocal input /input

# Run wordcount example provided:

bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.*.jar wordcount /input /output

# Check the output:

bin/hdfs dfs -cat /output/*
# This 2
# another 1
# is 2
# line 1
# one 2

# 9. Web interface

# Browse HDFS and check health using http://localhost:50070 in the browser:

xdg-open http://localhost:50070

# You can check the status of the applications running using the following URL:

xdg-open http://localhost:8088

#######
# HIVE
#######

$HIVE_HOME/bin/hive -e "CREATE TABLE page_view(viewTime INT, userid BIGINT,
     page_url STRING, referrer_url STRING,
     ip STRING COMMENT 'IP Address of the User')
 COMMENT 'This is the page view table'
 ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';"

echo "1321314314,4,http://www.page.com,http://www.referrer.com,10.200.13.110" >> input/page_view.csv

$HIVE_HOME/bin/hive -e "LOAD DATA LOCAL INPATH '$HADOOP_YARN_HOME/input/page_view.csv' OVERWRITE INTO TABLE page_view;"

$HIVE_HOME/bin/hive -e "show tables;"

$HIVE_HOME/bin/hive -e "select count(*) from page_view;"

$HIVE_HOME/bin/hive -e "select * from page_view;"

$HIVE_HOME/bin/hive -e "drop table page_view;"

# 10. Stop the processes

sbin/hadoop-daemon.sh stop namenode
sbin/hadoop-daemon.sh stop datanode
sbin/yarn-daemon.sh stop resourcemanager
sbin/yarn-daemon.sh stop nodemanager
sbin/mr-jobhistory-daemon.sh stop historyserver
