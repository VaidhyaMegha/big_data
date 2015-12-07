ADD JAR ${env:PROJECT_HOME}/udaf/target/connected_components-0.1-jar-with-dependencies.jar;

create temporary function components as 'me.tingri.hive.udaf.ConnectedComponents';
create temporary function components_wqupc as 'me.tingri.hive.udaf.ConnectedComponentsWQUPC';
set components_wqupc_max_num_nodes=100;

CREATE TABLE edges_string(node1 STRING, node2 STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

CREATE TABLE nodes_string(node STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

CREATE TABLE edges(id1 INT, id2 INT)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

-- Lookup table with simulated id
CREATE TABLE nodes(id INT, node STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

-- Roots of tree to which the ith node belongs
CREATE TABLE components(root_array ARRAY<INT>)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

LOAD DATA LOCAL INPATH '${env:DATA_SETS_FOLDER}/edges.csv' OVERWRITE INTO TABLE edges_string;

-- {"N9":["N20","N4","N9"],"N5":["N1","N3","N5","N2","N6"],"N6":["N1","N3","N5","N2","N6"],
-- "N7":["N7","N8"],"N8":["N7","N8"],"N1":["N1","N3","N5","N2","N6"],"N2":["N1","N3","N5","N2","N6"],
-- "N3":["N1","N3","N5","N2","N6"],"N20":["N20","N4","N9"],"N4":["N20","N4","N9"],"N10":["N10","N11"],
-- "N11":["N10","N11"]}
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

-- [6,2,2,5,5,6,5,6,5,10,10,5]
insert into table components
SELECT components_wqupc(id1, id2) as cluster
from edges;

-- N9	N4
-- N8	N8
-- N7	N8
-- N6	N5
-- N5	N5
-- N4	N4
-- N3	N5
-- N20	N4
-- N2	N5
-- N11	N11
-- N10	N11
-- N1	N5
select table3.node, nodes.node as root from
(select * from nodes JOIN
  (select posexplode(root_array) as (node_id, root_id) from components) table1
  on nodes.id = table1.node_id) table3 JOIN nodes on (nodes.id = table3.root_id) ;




