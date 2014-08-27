CREATE TABLE strange_string(strange STRING COMMENT 'a:d:e|z:y:q|1:s:p|6:6:r') ROW FORMAT DELIMITED;

LOAD DATA LOCAL INPATH '${env:HADOOP_HOME}/input/strange_string.csv' OVERWRITE INTO TABLE strange_string;

select explode(split(strange, "\\|")) as entry from strange_string;
--a:d:e
--z:y:q
--1:s:p
--6:6:r
--f:q:l
--m:j:p
--3:r:b

 SELECT split(t1.entry, ":")[0],split(t1.entry, ":")[1], split(t1.entry, ":")[2]  FROM (select explode(split(strange, "\\|")) as entry from strange_string) as t1;
--a	d	e
--z	y	q
--1	s	p
--6	6	r
--f	q	l
--m	j	p
--3	r	b

 SELECT split(entry, ":")[0],split(entry, ":")[1], split(entry, ":")[2]  FROM strange_string LATERAL VIEW explode(split(strange, "\\|")) entryTable AS entry;
--a	d	e
--z	y	q
--1	s	p
--6	6	r
--f	q	l
--m	j	p
--3	r	b