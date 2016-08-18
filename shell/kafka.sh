#!/usr/bin/env bash

# kafka
pushd $PROJECT_HOME/kafka/
mvn clean assembly:assembly
popd

java -cp $PROJECT_HOME/kafka/target/kafka-0.2-jar-with-dependencies.jar me.tingri.big_data.kafka.example._Topic TestTopic
java -cp $PROJECT_HOME/kafka/target/kafka-0.2-jar-with-dependencies.jar me.tingri.big_data.kafka.example._Producer 100 TestTopic
java -cp $PROJECT_HOME/kafka/target/kafka-0.2-jar-with-dependencies.jar me.tingri.big_data.kafka.example._Consumer TestTopic