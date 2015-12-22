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

# Running the famous wordcount example to verify installation
rm -rf input
mkdir input
echo "This is one line" >> input/file
echo "This is another line" >> input/file

hdfs dfs -rm -r /input
hdfs dfs -rm -r /output

# Add this directory to HDFS:
hdfs dfs -copyFromLocal input /input
# Run wordcount example provided:
hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.*.jar wordcount /input /output

# Check the output:
echo "Word Count Output"
echo "-----------------"
hdfs dfs -cat /output/*


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