# Kafka Multi-thread Producer

How to use it: 

```shell
java -jar Kafka-Producer.jar -h
usage: Kafka data generator

       [-h] --bootstrap-servers BOOTSTRAP_SERVERS --topic TOPIC [--msg-num MSG_NUM]
       [--time-type TIME_TYPE]

This tool is used for producing Kafka msgs during huawei test.
Message type: id,name,type,unix-time

named arguments:
  -h, --help             show this help message and exit
  --bootstrap-servers BOOTSTRAP_SERVERS
                         kafka bootstrap-server, multi-proved
  --topic TOPIC          Produce msg to this topic
  --msg-num MSG_NUM      produce xx  nums  of  msg  to  this  topic,  default  1e  (default:
                         100000000)
  --time-type TIME_TYPE  unix-time or timestamp, default  unix-time, timestamps like: "yyyy-
                         MM-dd HH:mm:ss" or "MM/dd/yyyy HH:mm:ss"  (default: unix-time)
  --thread-num THREAD_NUM
                           use num of thread to produce. (default: 10)
  --producer.config CONFIG-FILE
                           producer config properties file.
```