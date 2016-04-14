ADD JAR ${env:PROJECT_HOME}/udf/target/tingri_hive-0.1-jar-with-dependencies.jar;

create temporary function minArrayofInts as 'me.tingri.hive.udf.MinArrayofInts';

CREATE TABLE edges_string(node1 STRING, node2 STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

CREATE TABLE nodes_string(node STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

CREATE TABLE edges(id1 Int, id2 Int)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

-- Lookup table with simulated id
CREATE TABLE nodes(id Int, node STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';


CREATE TABLE hbase_edges (node Int, neighbors map<string,Int>)
  STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
  "hbase.columns.mapping" = ":key, neighbors:"
)
TBLPROPERTIES ("hbase.table.name" = "h_edges", "hbase.mapred.output.outputtable" = "h_edges");

CREATE TABLE hbase_temp (neighbors map<string,Int>, node Int)
  STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
  "hbase.columns.mapping" = "neighbors:, :key"
)
  TBLPROPERTIES ("hbase.table.name" = "h_temp", "hbase.mapred.output.outputtable" = "h_temp");

CREATE TABLE hbase_components (node Int, component_id int)
  STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
  "hbase.columns.mapping" = ":key, component:id"
)
  TBLPROPERTIES ("hbase.table.name" = "h_components", "hbase.mapred.output.outputtable" = "h_components");


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

-- Use Lookup table twice to convert edges into a table with purely ids(Ints)
Insert into table edges
select id1, id as id2
from ( select  id as id1, node2
       from edges_string, nodes
       where node1 = node ) new_table, nodes
where node2 = node;

INSERT INTO TABLE hbase_edges
select node, neighbors from (
    SELECT id1 as node, map(cast(id2 as string), 1) as neighbors FROM edges
    UNION ALL
    SELECT id2 as node, map(cast(id1 as string), 1) as neighbors FROM edges
    UNION ALL
    SELECT id as node, map(cast(id as string), 1) as neighbors from nodes
  ) a;

-- Preparation of Vector ---

-- sort_array doesnt work as expected since map_keys return string of arrays and not ints
--      insert into table hbase_components
--      select node, sort_array(map_keys(neighbors))[0] from hbase_edges;
-- UDTF wont work in nested expressions
-- insert into table hbase_components
-- select node, min(cast(key as int)) as component_id from (
--   select node, explode(map_keys(neighbors)) as key from hbase_edges
-- ) he group by node;

insert into table hbase_components
select node, minArrayofInts(map_keys(neighbors)) from hbase_edges;


-- Iterations ------

-- Iteration 1 (Repeat and Rinse till convergence) each time components will be updated with new timestamp
-- while hbase_edges itself can be used temp table is used for readability
-- join is forcably broken down into two steps since it seems lateral view cannot be applied in join.

insert overwrite table hbase_temp
  SELECT neighbors, component_id
  FROM hbase_components, hbase_edges
  where hbase_components.node = hbase_edges.node;

insert overwrite table hbase_edges
  select lv.node, map(cast(hbase_temp.node as string), 1) as neighbors
  from hbase_temp lateral view explode(neighbors) lv as node, value;

insert into table hbase_components
  select node, minArrayofInts(map_keys(neighbors)) from hbase_edges;

-- For next set of iterations repeat the steps
