 -- check how many id is not unique
 select  count(*) from
 (select aa03csno, count(*) as numbeer from pvtest.BDFMHQAA group by aa03csno )
  T where T.numbeer > 1 FETCH FIRST 20 ROWS ONLY;

-- delete those id which is not unique and keep 1 record
delete from ODSDB.BDFMHQAA
where aa03csno not in (select * from (select max(aa74date), *
from ODSDB.BDFMHQAA group by aa03csno having count(aa03csno) > 1) as b);