--创建没有倾斜数据的JY1外表
create database if not exists ICBC;
use ICBC;
drop  table ICBC.jy1_noskew_external;
create external table ICBC.jy1_noskew_external
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
row format delimited fields terminated by '|'
location '/kqp/pfsimulator/data/JY1_noskew';



--创建JY1 Holodesk表
drop  table ICBC.jy1;
create  table ICBC.jy1
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


--将没有倾斜的数据插入至jy1表中
set mapred.reduce.tasks=269;
insert into ICBC.jy1 select * from ICBC.jy1_noskew_external distribute by rand();

set mapred.reduce.tasks=-1;
--将插入至jy1表中的没有倾斜的数据的account_num值为'3213126783079'先删除掉
delete  from  ICBC.jy1 where account_num='3213126783079';



--创建有倾斜数据的JY1外表
drop  table ICBC.jy1_skew_external;
create external table ICBC.jy1_skew_external
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
row format delimited fields terminated by '|'
location '/kqp/pfsimulator/data/JY1_skew';


--将倾斜的数据插入至jy1表中
set mapred.reduce.tasks=269;
insert into ICBC.jy1 select * from ICBC.jy1_skew_external distribute by rand();

set mapred.reduce.tasks=-1;
--将JY1表account_num字段生成6XXX+area_number+account_num拼接成的字段
update ICBC.JY1 set account_num=concat('620',area_number,account_num) where area_number<10;
update ICBC.JY1 set account_num=concat('62',area_number,account_num) where area_number>=10 and area_number<100;
update ICBC.JY1 set account_num=concat('6300',account_num) where area_number=100;


--创建blacklist表
drop table ICBC.blacklist1;
create table ICBC.blacklist1
(
account string,
update_date string
)stored as holodesk with tablesize 100GB;

insert into ICBC.blacklist1 select account_num,trade_date from ICBC.Jy1  where area_number!=25 limit 60000;


--创建jy2join表准备jy1和jy2能关联上的数据
drop table ICBC.jy2join;
create  table ICBC.JY2join
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


insert into ICBC.JY2join select * from ICBC.Jy1 where  account_num <> '62253213126783079' and area_number>10 and area_number<13;


--创建jy2表，通过改变字段顺序的方式，使jy2表满足25%的数据倾斜,且能够跟jy1有1%的关联数据
drop  table ICBC.JY2;
create  table ICBC.JY2
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
stored as holodesk with tablesize 100GB;


set mapred.reduce.tasks=269;

insert into ICBC.JY2 select financial_num ,other_id ,other_account ,id ,account_num ,amount_of_money ,trade_date ,trade_time ,loan_flag ,area_number from ICBC.jy1_skew_external distribute by rand();
insert into ICBC.JY2 select financial_num ,other_id ,other_account ,id ,account_num ,amount_of_money ,trade_date ,trade_time ,loan_flag ,area_number from ICBC.jy1_noskew_external distribute by rand();

set mapred.reduce.tasks=-1;
Insert into ICBC.JY2 select financial_num ,other_id ,other_account ,id ,account_num ,amount_of_money ,trade_date ,trade_time ,loan_flag ,area_number  from ICBC.JY2join;


--创建acccity表
use ICBC;
drop table ICBC.acccity;
create table ICBC.acccity(
area_number string,
account_prefix string
)
stored as holodesk with tablesize 10GB
tblproperties
(
"holodesk.index"="account_prefix,area_number"
);

insert into acccity values(1,6201);
insert into acccity values(2,6202);
insert into acccity values(3,6203);
insert into acccity values(4,6204);
insert into acccity values(5,6205);
insert into acccity values(6,6206);
insert into acccity values(7,6207);
insert into acccity values(8,6208);
insert into acccity values(9,6209);
insert into acccity values(10,6210);
insert into acccity values(11,6211);
insert into acccity values(12,6212);
insert into acccity values(13,6213);
insert into acccity values(14,6214);
insert into acccity values(15,6215);
insert into acccity values(16,6216);
insert into acccity values(17,6217);
insert into acccity values(18,6218);
insert into acccity values(19,6219);
insert into acccity values(20,6220);
insert into acccity values(21,6221);
insert into acccity values(22,6222);
insert into acccity values(23,6223);
insert into acccity values(24,6224);
insert into acccity values(25,6225);
insert into acccity values(26,6226);
insert into acccity values(27,6227);
insert into acccity values(28,6228);
insert into acccity values(29,6229);
insert into acccity values(30,6230);
insert into acccity values(31,6231);
insert into acccity values(32,6232);
insert into acccity values(33,6233);
insert into acccity values(34,6234);
insert into acccity values(35,6235);
insert into acccity values(36,6236);
insert into acccity values(37,6237);
insert into acccity values(38,6238);
insert into acccity values(39,6239);
insert into acccity values(40,6240);
insert into acccity values(41,6241);
insert into acccity values(42,6242);
insert into acccity values(43,6243);
insert into acccity values(44,6244);
insert into acccity values(45,6245);
insert into acccity values(46,6246);
insert into acccity values(47,6247);
insert into acccity values(48,6248);
insert into acccity values(49,6249);
insert into acccity values(50,6250);
insert into acccity values(51,6251);
insert into acccity values(52,6252);
insert into acccity values(53,6253);
insert into acccity values(54,6254);
insert into acccity values(55,6255);
insert into acccity values(56,6256);
insert into acccity values(57,6257);
insert into acccity values(58,6258);
insert into acccity values(59,6259);
insert into acccity values(60,6260);
insert into acccity values(61,6261);
insert into acccity values(62,6262);
insert into acccity values(63,6263);
insert into acccity values(64,6264);
insert into acccity values(65,6265);
insert into acccity values(66,6266);
insert into acccity values(67,6267);
insert into acccity values(68,6268);
insert into acccity values(69,6269);
insert into acccity values(70,6270);
insert into acccity values(71,6271);
insert into acccity values(72,6272);
insert into acccity values(73,6273);
insert into acccity values(74,6274);
insert into acccity values(75,6275);
insert into acccity values(76,6276);
insert into acccity values(77,6277);
insert into acccity values(78,6278);
insert into acccity values(79,6279);
insert into acccity values(80,6280);
insert into acccity values(81,6281);
insert into acccity values(82,6282);
insert into acccity values(83,6283);
insert into acccity values(84,6284);
insert into acccity values(85,6285);
insert into acccity values(86,6286);
insert into acccity values(87,6287);
insert into acccity values(88,6288);
insert into acccity values(89,6289);
insert into acccity values(90,6290);
insert into acccity values(91,6291);
insert into acccity values(92,6292);
insert into acccity values(93,6293);
insert into acccity values(94,6294);
insert into acccity values(95,6295);
insert into acccity values(96,6296);
insert into acccity values(97,6297);
insert into acccity values(98,6298);
insert into acccity values(99,6299);
insert into acccity values(100,6300);


--创建accuser表

drop table ICBC.accuser;
create table ICBC.accuser(
username string,
id string,
account string
)stored as holodesk with tablesize 100GB
tblproperties
(
"holodesk.index"="username,account"
);

drop table icbc.accuser_tmp;
create table ICBC.accuser_tmp stored as holodesk with tablesize 100GB as select financial_num,id,account_num from ICBC.jy1 group by financial_num,id,account_num ;

insert into ICBC.accuser select * from ICBC.accuser_tmp;






