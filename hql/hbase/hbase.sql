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
TBLPROPERTIES ("hbase.table.name" = "hbase_edges", "hbase.mapred.output.outputtable" = "hbase_edges");;


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
    UNION
    SELECT id2 as node, map(cast(id1 as string), 1) as neighbors FROM edges
  ) a;
