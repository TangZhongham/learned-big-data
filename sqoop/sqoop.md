# sqoop

Sqoop is an ETL tool used between RDBMS and dbs in big data area

## how to

### Sqoop with Oracle

List all tables under XE：

```bash
sqoop list-tables  --connect "jdbc:oracle:thin:@$IP:$PORT/$SID" --username xxxx --password xxxx
```

output xxxx.TEST

```bash
sqoop import --connect "jdbc:oracle:thin:@$IP:$PORT/$SID" --username xxxx --password xxxx --target-dir /data/sqoop/database_name/table_name/ -m 1 --query "select $COLUMNS from $ORACLE_USERNAME.$TABLENAME where \$CONDITIONS" --fetch-size 10000 --fields-terminated-by "\\01" --hive-drop-import-delims --null-string '\\N' --null-non-string '\\N'
```

transfer data from oracle to hdfs:

```bash
sqoop import --connect "jdbc:oracle:thin:@$IP:$PORT/$SID" --username xxxx --password xxxx --target-dir /data/sqoop/$ORACLE_USERNAME/$TABLENAME/ -m 20 --split-by "MOD(ORA_HASH(id), 20)" --boundary-query "select 0, 19 from dual" --query "select $COLUMNS from $ORACLE_USERNAME.$TABLENAME where \$CONDITIONS" --fetch-size 10000 --fields-terminated-by "\\01" --hive-drop-import-delims --null-string '\\N' --null-non-string '\\N'
```

partition table in hive:

```bash
sqoop import --connect "jdbc:oracle:thin:@$IP:$PORT/$SID" --username xxxx --password xxxx --target-dir /data/sqoop/$ORACLE_USERNAME/$TABLENAME/$PARTITION -m 1 --query "select $COLUMNS from $ORACLE_USERNAME.$TABLENAME partition($PARTITION) where \$CONDITIONS" --fetch-size 10000 --fields-terminated-by "\\01" --hive-drop-import-delims --null-string '\\N' --null-non-string '\\N'
```
 

from HDFS to Oracle

```bash
sqoop export --connect jdbc:oracle:thin:@$IP:$PORT/$SID --table DCRUN.OMS_CLIENT_COPY --export-dir /user/yarn1/test/ --username xxxx --password xxxx --input-fields-terminated-by ',' -m 1 --input-null-non-string='\\N' --null-non-string='' --input-null-string='\\N' --null-string=''
```

### Sqoop with Mysql

list all databases：

```bash
sqoop list-databases --username xxxx --password xxxx --connect jdbc:mysql://$IP:$PORT/

```

list all tables in database test：

```bash
sqoop list-tables --username xxxx --password xxxx --connect jdbc:mysql://$IP:$PORT/$MYSQL_TABLE
```

import all data from Mysql to HDFS

把数据从test数据库下的表person_all --table 模式 Sqoop 抽取到HDFS目录 /data/sqoop/$DATABASE_NAME/$TABLE_NAME


```bash
sqoop import --username xxxx --password xxxx --connect jdbc:mysql://$IP:$PORT/$DATABASE_NAME --table $TABLE_NAME --target-dir  /data/sqoop/$DATABASE_NAME/$TABLE_NAME --fetch-size 10000 -m 1 -fields-terminated-by "\\01" --null-string '\\N' --null-non-string '\\N' --hive-drop-import-delims --delete-target-dir
```


import all data from HDFS to Mysql

```bash
sqoop export --connect --username xxxx--password xxxx jdbc:mysql://$IP:$PORT/$DATABASE_NAME --table $TABLE_NAME--export-dir /DATA/$DATABASE_NAME/$TABLE_NAME --staging-table $STAGE_TABLE --clear-staging-table

```


### Sqoop with DB2

from DB2 to HDFS

```bash
sudo -u hdfs sqoop import --connect $DB2_CONNECTOR --username $DB2_USERNAME --password $DB2_PASSWORD --fetch-size 1000 -m 4 --target-dir $HDFS_URL/$TABLENAME --split-by $COLUMN --query "select $QUERY from $DB2_DATABASE.$TABLENAME where \$CONDITIONS" --fields-terminated-by "\\01" --hive-drop-import-delims --null-string '\\N' --null-non-string '\\N'
```
