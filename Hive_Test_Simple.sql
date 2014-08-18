CREATE TABLE page_view(viewTime INT, userid BIGINT,  page_url STRING, referrer_url STRING,
     ip STRING COMMENT 'IP Address of the User')
     COMMENT 'This is the page view table'
     ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

LOAD DATA LOCAL INPATH '/home/sandeep/tools/hadoop/2.4.1/input/page_view.csv' OVERWRITE INTO TABLE page_view;

show tables;

select count(*) from page_view;

select * from page_view;

drop table page_view;
