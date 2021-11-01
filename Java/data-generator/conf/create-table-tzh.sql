--创建table1外表
create database if not exists tzh;
use tzh;
drop  table tzh.table1_ext;
create external table tzh.table1_ext
(
financial_num string,
id string,
account_num string,
other_id string,
other_account string,
amount_of_money string,
trade_date string,
trade_time string,
loan_flag string,
area_number string
)
row format delimited fields terminated by ','
location '/tzh/data/table1_ext';



--创建table1 Holodesk表
drop  table tzh.table1;
create  table tzh.table1
(
financial_num string,
id string,
account_num string,
other_id string,
other_account string,
amount_of_money string,
trade_date string,
trade_time string,
loan_flag string,
area_number string
)stored as holodesk with tablesize 100GB;


--将没有倾斜的数据插入至table1表中
set mapred.reduce.tasks=269;
insert into tzh.table1 select * from tzh.table1_ext distribute by rand();