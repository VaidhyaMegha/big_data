ADD JAR ${env:PROJECT_HOME}/udf/target/tingri_hive-0.1-jar-with-dependencies.jar;

create temporary function minArrayofInts as 'me.tingri.hive.udf.MinArrayofInts';

CREATE TABLE edges_string(node1 STRING, node2 STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

CREATE TABLE nodes_string(node STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

CREATE TABLE edges(id1 BIGINT, id2 BIGINT)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

-- Lookup table with simulated id
CREATE TABLE nodes(id BIGINT, node STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

-- component could have been defined as simple type but
-- to coax hive to insert without touching this column its defined as map
CREATE TABLE hbase_edges (node BIGINT, neighbors map<string,BIGINT>, component map<string,BIGINT>)
  STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
  "hbase.colValues.mapping" = ":key, neighbors:, component:"
)
TBLPROPERTIES ("hbase.table.name" = "h_edges", "hbase.mapred.output.outputtable" = "h_edges");


LOAD DATA LOCAL INPATH '${env:DATA_SETS_FOLDER}/edges.csv' OVERWRITE INTO TABLE edges_string;

-- get nodes from edges
INSERT  into table nodes_string
select node FROM
  (
    select node1 as node from edges_string
    UNION
    select node2 as node from edges_string
  ) new_table;

-- Populate Lookup table with simulated id
Insert into table nodes
SELECT  row_number() over() as id, node
from nodes_string;

-- Use Lookup table twice to convert edges into a table with purely ids(BIGINTs)
Insert into table edges
select id1, id as id2
from ( select  id as id1, node2
       from edges_string, nodes
       where node1 = node ) new_table, nodes
where node2 = node;

-- dummy -1 component id
INSERT INTO TABLE hbase_edges
select node, neighbors, component from (
    SELECT id1 as node, map(cast(id2 as string), 1L) as neighbors, map(' ', -1L) as component FROM edges
    UNION ALL
    SELECT id2 as node, map(cast(id1 as string), 1L) as neighbors, map(' ', -1L) as component FROM edges
    UNION ALL
    SELECT id as node, map(cast(id as string), 1L) as neighbors, map(' ', -1L)  as component from nodes
  ) a;

-- sort_array doesnt work as expected since map_keys return string of arrays and not BIGINTs
--      insert into table hbase_components
--      select node, sort_array(map_keys(neighbors))[0] from hbase_edges;
-- UDTF wont work in nested expressions
-- insert into table hbase_components
-- select node, min(cast(key as BIGINT)) as component_id from (
--   select node, explode(map_keys(neighbors)) as key from hbase_edges
-- ) he group by node;


-- Iterations ------

-- Iteration 1 (Repeat and Rinse till convergence) each time components will be updated with new timestamp

-- insert into hbase_edges dummy map and component
insert into table hbase_edges
select node, map(cast(node as string), node) as neighbors,
  map(cast(node as string), minArrayofInts(map_keys(neighbors))) as component
from hbase_edges;

-- insert new edges with suggested component and a dummy map to retain component
insert into table hbase_edges
  select lv.node as node, map(cast(h.component[h.node] as string), 1L) as neighbors,
    map(' ', -1L) as component
  from hbase_edges as h lateral view explode(h.neighbors) lv as node, value;

-- For next set of iterations repeat the steps
