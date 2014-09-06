DROP FUNCTION IF EXISTS components;

ADD JAR ${env:PROJECT_HOME}/udaf/target/connected_components-0.1-jar-with-dependencies.jar;

create temporary function components as 'me.tingri.hive.udaf.ConnectedComponents';

CREATE TABLE edges(node1 STRING, node2 STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

LOAD DATA LOCAL INPATH '${env:PROJECT_HOME}/edges.csv' OVERWRITE INTO TABLE edges;

SELECT components(node1, node2) from edges;