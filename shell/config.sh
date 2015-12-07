#!/bash

export CUR_DIR=`pwd`

cd $HADOOP_ROOT
sudo chown -R $USER $HDP_VER

# Create two directories to be used by namenode and datanode.
rm -rf $NAME_NODE_DATA_DIR
rm -rf $DATA_NODE_DATA_DIR

mkdir -p $NAME_NODE_DATA_DIR
mkdir -p $DATA_NODE_DATA_DIR

# Set up config files
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

#  $HADOOP_CONF_DIR/hbase-site.xml
rm  $HADOOP_CONF_DIR/hbase-site.xml
touch  $HADOOP_CONF_DIR/hbase-site.xml
echo "<configuration>
  <property>
    <name>hbase.rootdir</name>
    <value>file:///home/testuser/hbase</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/home/testuser/zookeeper</value>
  </property>
</configuration>" >>  $HADOOP_CONF_DIR/hbase-site.xml

if [ "$3" == "tez" ]; then
    #  $HADOOP_CONF_DIR/mapred-site.xml:
    rm  $HADOOP_CONF_DIR/mapred-site.xml
    touch  $HADOOP_CONF_DIR/mapred-site.xml
    echo "<?xml version=\"1.0\"?>
    <configuration>
       <property>
          <name>mapreduce.framework.name</name>
          <value>yarn-tez</value>
       </property>
    </configuration>" >>  $HADOOP_CONF_DIR/mapred-site.xml

    #  $HADOOP_CONF_DIR/tez-site.xml:
    rm  $HADOOP_CONF_DIR/tez-site.xml
    touch  $HADOOP_CONF_DIR/tez-site.xml
    echo "<?xml version=\"1.0\"?>
    <configuration>
       <property>
          <name>tez.lib.uris</name>
          <value>\${fs.defaultFS}/apps/tez-0.8.2-SNAPSHOT/tez-0.8.2-SNAPSHOT.tar.gz</value>
       </property>
    </configuration>" >>  $HADOOP_CONF_DIR/tez-site.xml

    # Enabling Tez as execution engine after checkign for a flag here
    hive -v -e "set hive.execution.engine=tez;"
else
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
fi

cd ${CUR_DIR}

# 5. Format namenode
# This step is needed only for the first time. Doing it every time will result in loss of content on HDFS.
hdfs namenode -format
