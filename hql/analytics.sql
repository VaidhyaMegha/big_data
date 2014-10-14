create table india_pow_gen (REGION_ID STRING, MONTH STRING, REGION STRING,
  STATE STRING,SECTOR STRING,CATEGORY STRING,FUEL STRING,UTILITY STRING,
  STATION STRING,INSTALLED_CAPACITY INT ,PROGRAM_GENERATION INT ,ACTUAL_GENERATION INT )
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

-- DescriptionThe data refers to station-wise details on monthly power generation.
-- The other details pertaining to such stations includes the
-- Location, Sector, Category, Fuel, Utility, Station, Installed Capacity,
-- Programmed Generation and Actual Generation during the month.

-- Note: The Program Generation and Actual Generation are in Giga Watt Hour (GWH).
-- The Installed Capacity is in Mega Watts (MW).

LOAD DATA LOCAL INPATH '${env:DATA_SETS_FOLDER}/datafile.csv' OVERWRITE INTO TABLE india_pow_gen;


-- Top 3 states in actual_generation
select state, sum(actual_generation) as gen
from india_pow_gen
group by state order by gen desc limit 3;

--  Top 3 states in terms of actual/program
select state, sum(actual_generation)/sum(program_generation) as ratio
from india_pow_gen
group by state order by ratio  desc limit 3;

-- Rank stations within state
select state, station, actual_generation, rank() over(partition by state order by actual_generation desc)
from india_pow_gen;

-- Top 3 stations within each state
select t.* from (
  select state, station, actual_generation, rank() over(partition by state order by actual_generation desc) as rank
  from india_pow_gen
  ) t
where t.rank <=3;

-- Each state as compared to average india power generation



-- Closest comparable state to each state with respect to actual generation