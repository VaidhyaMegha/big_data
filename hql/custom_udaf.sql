DROP FUNCTION IF EXISTS components;

ADD JAR ${env:PROJECT_HOME}/udaf/target/connected_components-0.1-jar-with-dependencies.jar;

create temporary function components as 'me.tingri.hive.udaf.ConnectedComponents';

CREATE TABLE edges(node1 STRING, node2 STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

LOAD DATA LOCAL INPATH '${env:PROJECT_HOME}/edges.csv' OVERWRITE INTO TABLE edges;

SELECT components(node1, node2) from edges;
-- {"N9":["N9","N4","N20"],"N7":["N7","N8"],"N1":["N1","N3","N5","N2","N6"],"N10":["N10","N11"]}