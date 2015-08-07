#!/bin/bash

hdfs dfs -rm -r -f /giraph

hdfs dfs -mkdir /giraph

hdfs dfs -copyFromLocal $PROJECT_HOME/giraph/tiny_graph.txt /giraph/tiny_graph.txt

giraph $GIRAPH_HOME/giraph-examples-1.2.0-SNAPSHOT.jar org.apache.giraph.examples.SimpleShortestPathsComputation -vif org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat -vip /giraph/tiny_graph.txt -vof org.apache.giraph.io.formats.IdWithValueTextOutputFormat -op /giraph/shortestpaths -w 1 -ca giraph.SplitMasterWorker=false

hdfs dfs -cat /giraph/shortestpaths