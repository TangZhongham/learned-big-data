# Flink demo

使用方法

mvn clean package

```shell
./flink run etl.jar -h
usage: Flink kafka ETL tool [-h] [--job-name JOB_NAME] --bootstrap-servers BOOTSTRAP_SERVERS
                            --source-topic SOURCE_TOPIC --group-id GROUP_ID
                            --broker-list BROKER_LIST --to-topic TO_TOPIC

This tool is used to consumer from one kakfa topic and produce to another.
ETL huawei test only

named arguments:
  -h, --help             show this help message and exit
  --job-name JOB_NAME    Flink Job name
  --bootstrap-servers BOOTSTRAP_SERVERS
                         consumer msg from this kafka group
  --source-topic SOURCE_TOPIC
                         consumer msg from this topic
  --group-id GROUP_ID    consumer msg from this kafka group id
  --broker-list BROKER_LIST
                         produce msg to this kafka group
  --to-topic TO_TOPIC    consumer msg to this topic
```