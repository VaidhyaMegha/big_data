DROP FUNCTION IF EXISTS rowWithHeader;

ADD JAR /home/sandeep/projects/big_data/udf/target/tingri_hive-0.1-jar-with-dependencies.jar;

create temporary function rowWithHeader as 'me.tingri.hive.udf.TransformRowWithHeader';

SELECT split(entry, ":")[0],split(entry, ":")[1], split(entry, ":")[2],split(entry, ":")[3],split(entry, ":")[4], split(entry, ":")[5]
FROM strange_string LATERAL VIEW explode(rowWithHeader(split(strange, "\\|"), 0)) entryTable AS entry;
-- NULL	NULL	NULL	NULL	NULL	NULL
-- a	d	e	z	y	q
-- a	d	e	1	s	p
-- a	d	e	6	6	r
-- NULL	NULL	NULL	NULL	NULL	NULL
-- f	q	l	m	j	p
-- f	q	l	3	r	b

SELECT split(t1.entry, ":")[0],split(t1.entry, ":")[1], split(t1.entry, ":")[2], split(t1.entry, ":")[3],split(t1.entry, ":")[4], split(t1.entry, ":")[5]
FROM  (select explode(rowWithHeader(split(strange, "\\|"), 0)) as entry from strange_string) as t1
where t1.entry is not null and t1.entry <> '';
-- a	d	e	z	y	q
-- a	d	e	1	s	p
-- a	d	e	6	6	r
-- f	q	l	m	j	p
-- f	q	l	3	r	b

INSERT OVERWRITE LOCAL DIRECTORY '/home/sandeep/projects/big_data/output' row format delimited fields terminated by ','
SELECT distinct(t1.entry), split(t1.entry, ":")[0],split(t1.entry, ":")[1], split(t1.entry, ":")[2]
FROM  (select explode(split(strange, "\\|")) as entry from strange_string) as t1
where t1.entry is not null and t1.entry <> '';
-- 1:s:p,1,s,p
-- 3:r:b,3,r,b
-- 6:6:r,6,6,r
-- a:d:e,a,d,e
-- f:q:l,f,q,l
-- m:j:p,m,j,p
-- z:y:q,z,y,q