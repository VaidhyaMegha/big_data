CREATE TABLE apachelog (
  host STRING,
  identity STRING,
  user STRING,
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

LOAD DATA LOCAL INPATH '${env:DATA_SETS_FOLDER}/tingri' OVERWRITE INTO TABLE apachelog;

select distinct host from apachelog;
select distinct status from apachelog;
select distinct agent from apachelog;
select distinct referer from apachelog;
select distinct user from apachelog;