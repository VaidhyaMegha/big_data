ADD JAR ${env:PROJECT_HOME}/udaf/target/connected_components-0.1-jar-with-dependencies.jar;

create temporary function components as 'me.tingri.hive.udaf.ConnectedComponents';
create temporary function components_wqupc as 'me.tingri.hive.udaf.ConnectedComponentsWQUPC';

CREATE TABLE edges_string(node1 STRING, node2 STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

CREATE TABLE nodes_string(node STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

CREATE TABLE edges(id1 INT, id2 INT)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

-- Lookup table with simulated id
CREATE TABLE nodes(id INT, node STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

LOAD DATA LOCAL INPATH '${env:DATA_SETS_FOLDER}/edges.csv' OVERWRITE INTO TABLE edges_string;

-- {"N9":["N9","N4","N20"],"N7":["N7","N8"],"N1":["N1","N3","N5","N2","N6"],"N10":["N10","N11"]}
SELECT components(node1, node2) from edges_string;

-- get nodes from edges
INSERT  into table nodes_string
select distinct node FROM
  (
    select node1 as node from edges_string
    UNION ALL select node2 as node from edges_string
  ) new_table;

-- Populate Lookup table with simulated id
Insert into table nodes
SELECT  row_number() over() as id, node
from nodes_string;

-- Use Lookup table twice to convert edges into a table with purely ids(ints)
Insert into table edges
select id1, id as id2
from ( select  id as id1, node2
       from edges_string, nodes
       where node1 = node ) new_table, nodes
where node2 = node;

-- {"N9":["N9","N4","N20"],"N7":["N7","N8"],"N1":["N1","N3","N5","N2","N6"],"N10":["N10","N11"]}
-- []
SELECT components_wqupc(id1, id2) as cluster
from edges;


