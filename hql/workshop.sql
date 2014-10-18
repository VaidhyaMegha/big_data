create table weather(country_name string, zip_name string, day_name string, temperature int) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TEXTFILE;
LOAD DATA LOCAL INPATH '${env:DATA_SETS_FOLDER}/workshop/weather_data' OVERWRITE INTO TABLE weather;
select distinct day_name from weather;
select distinct country_name from weather;
select country_name, count(1), sum(temperature), avg(temperature) from weather group by country_name;
select country_name,zip_name, count(1), sum(temperature), avg(temperature) from weather group by country_name, zip_name;


$ sudo ./run.sh -m server -c start -e mr

$ sudo ./run.sh -m client


hive> LOAD DATA LOCAL INPATH '${env:DATA_SETS_FOLDER}/workshop/weather_data' OVERWRITE INTO TABLE weather;


-- on another window
$ $HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main datasets/workshop/playground/src/WeatherJob.java

$ cd datasets/workshop/playground/src/

$ jar cf Weather.jar WeatherJob*.class

$ sudo su root

root@sandeep-Latitude-6430U:/home/sandeep/projects/big_data/datasets/workshop/playground/src# export USER=sandeep

root@sandeep-Latitude-6430U:/home/sandeep/projects/big_data/datasets/workshop/playground/src# source /home/sandeep/projects/big_data/shell/env.sh

root@sandeep-Latitude-6430U:/home/sandeep/projects/big_data/datasets/workshop/playground/src# $HADOOP_HOME/bin/hadoop jar Weather.jar WeatherJob /user/hive/warehouse/weather/weather_data /weather_output

