[gpadmin@edge ~]$ gpsql
psql (8.2.15)
Type "help" for help.

template1=# create table transaction_test(col1 varchar(50));
CREATE TABLE
template1=# begin transaction;
BEGIN
template1=# insert into transaction_test values('123');                                                                                                                                                                                                                                                                INSERT 0 1
template1=# commit;
COMMIT
template1=# select * from transaction_test;
 col1
------
 123
(1 row)

template1=# begin transaction;
BEGIN
template1=# insert into transaction_test values('456');
INSERT 0 1
template1=# rollback;
ROLLBACK
template1=# select * from transaction_test;
 col1
------
 123
(1 row)

template1=# begin transaction;
BEGIN
template1=# delete from transaction_test;
ERROR:  Delete append-only table statement not supported yet
template1=# select * from transaction_test;
ERROR:  current transaction is aborted, commands ignored until end of transaction block
template1=# end
template1-# ;
ROLLBACK
template1=#
template1=# begin transaction;
BEGIN
template1=# update transaction_test set col1 = '456';
ERROR:  Update append-only table statement not supported yet
template1=# end;
ROLLBACK
template1=# 