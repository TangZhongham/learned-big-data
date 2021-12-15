 -- check how many id is not unique
 select  count(*) from
 (select aa03csno, count(*) as numbeer from pvtest.BDFMHQAA group by aa03csno )
  T where T.numbeer > 1 FETCH FIRST 20 ROWS ONLY;

-- delete those id which is not unique and keep 1 record
delete from ODSDB.BDFMHQAA
where aa03csno not in (select * from (select max(aa74date), *
from ODSDB.BDFMHQAA group by aa03csno having count(aa03csno) > 1) as b);

-- make sure table key is unique.
-- 901
select count(*) from ODSDB.dimension_1;
--901
select count(*) from (select distinct name1 from ODSDB.dimension_1 ) ;


-- window function
-- Some functions pretty popular in OLAP

-- avg
select
T1.ISIDISID,
T1.CINOCSNO,
T1.ADSNPTSN,
T1.ADTYADTP,

avg(T1.BUDIDATE),
avg(T1.UPDTDATE)

from ODSDB.TABLE_1 T1
GROUP BY
T1.ISIDISID,
T1.CINOCSNO,
T1.ADSNPTSN,
T1.ADTYADTP;

-- rank
select
T1.ISIDISID,
T1.CINOCSNO,
T1.ADSNPTSN,
T1.ADTYADTP,

sum(T1.BUDIDATE)
,RANK() OVER(PARTITION BY ISIDISID ORDER BY CINOCSNO) AS "rank"

from ODSDB.TABLE_1 T1
GROUP BY
T1.ISIDISID,
T1.CINOCSNO,
T1.ADSNPTSN,
T1.ADTYADTP
;