#!/bin/bash

function PrintHelp()
{
	echo ""
	echo "        hdfs xxx xxx xxx "
	echo "            run hdfs command"
	echo "        exit"
	echo "            exit MR prompt"
	echo "        help"
	echo "            show this screen"
	echo ""
}

hdfs dfs -rm -r /user/root
hdfs dfs -rm -r /user

hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/root

hdfs dfs -chmod g+w /user/root

hdfs dfs -rm -r input
hdfs dfs -rm -r output

hdfs dfs -mkdir input
hdfs dfs -mkdir output

# Add this directory to HDFS:
hdfs dfs -copyFromLocal datasets/* input


echo "-----------"
echo "Word count "
echo "-----------"
echo "Content"
cat datasets/wordcount_file

# Run wordcount example provided:
hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.*.jar wordcount input/wordcount_file output/wc

echo "---Output----"
hdfs dfs -cat output/wc/*

# mr-gimv-cc
pushd $PROJECT_HOME/mr-gimv-cc/
mvn clean assembly:assembly
popd

echo "---------------------------------"
echo "GIMV examples"
echo "---------------------------------"
echo "Test graph used for WQUPC implementation in hive UDAF"
cat datasets/graphs/edges.tsv

hadoop jar $PROJECT_HOME/mr-gimv-cc/target/mr-gimv-cc-1.2.jar me.tingri.graphs.cc.ConnectedComponents \
 input/graphs/edges.tsv output/wqupc_test_graph 20 3 makesym


echo "---Output----"
hdfs dfs -cat output/wqupc_test_graph/*

echo "Radius is half of diameter --- straight line"
cat datasets/graphs/eccentricity/straight_line.tsv

hadoop jar $PROJECT_HOME/mr-gimv-cc/target/mr-gimv-cc-1.2.jar me.tingri.graphs.cc.ConnectedComponents \
 input/graphs/eccentricity/straight_line.tsv output/straight_line 12 3 makesym

hadoop jar $PROJECT_HOME/mr-gimv-cc/target/mr-gimv-cc-1.2.jar me.tingri.graphs.cc.ConnectedComponents \
 input/graphs/eccentricity/straight_line.tsv output/straight_line 12 3 makesym norestart 6

echo "---Output----"
hdfs dfs -cat output/straight_line/*

while [ 1 -eq 1 ]; do
	echo -n "MR> "
	read CMD REST
	case "$CMD" in
	"") continue
	    ;;
    help) PrintHelp
        continue
        ;;
	hdfs) echo "hdfs $REST"; hdfs $REST
	    continue
	    ;;
	exit)
	    break
	    ;;
	*) echo "Invalid command. Type \`help\` for available commands."
	    ;;
	esac
done