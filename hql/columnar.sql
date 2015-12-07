CREATE TABLE apachelog_text (
  host STRING,
  identity STRING,
  user2 STRING,
  time STRING,
  request STRING,
  status STRING,
  size STRING,
  referer STRING,
  agent STRING)
  ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.RegexSerDe'
WITH SERDEPROPERTIES (
"input.regex" = "([^ ]*) ([^ ]*) ([^ ]*) (-|\\[[^\\]]*\\]) ([^ \"]*|\"[^\"]*\") (-|[0-9]*) (-|[0-9]*)(?: ([^ \"]*|\"[^\"]*\") ([^ \"]*|\"[^\"]*\"))?",
"output.format.string" = "%1$s %2$s %3$s %4$s %5$s %6$s %7$s %8$s %9$s"
)
  STORED AS TEXTFILE; -- TEXTFILE is the default storage format.

LOAD DATA LOCAL INPATH '${env:DATA_SETS_FOLDER}/tingri' OVERWRITE INTO TABLE apachelog_text;

select distinct host from apachelog_text;
select distinct status from apachelog_text;
select distinct agent from apachelog_text;
select distinct referer from apachelog_text;
select distinct user2 from apachelog_text;

-- CTAS orc table from apachelog_text
CREATE TABLE apachelog_orc
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
stored as orc tblproperties ("orc.compress"="SNAPPY") AS
select host, identity, user2, time, request, status, size, referer, agent
from apachelog_text ;

-- Enabling vectorized query execution
set hive.vectorized.execution.enabled = true;

select distinct host from apachelog_orc;
select distinct status from apachelog_orc;
select distinct agent from apachelog_orc;
select distinct referer from apachelog_orc;
select distinct user2 from apachelog_orc;