# learned-big-data

> A collection of what I've learned in big data.

 I am a self learner and hope these materials could prove that I am a strong candicate.

TODO

 - [ ] Still some Flink jobs not added up
 - [ ] Some python pandas/backend projects...

## Whole Stack

| Big Data    |  Project |
| ----------- | ----------- |
| [Data warehouse(Java)](#data-warehouse) | ✅Sqoop / SparkSQL / HDFS API / HBase API / JDBC |
| [Real-time Streaming](#real-time-streaming) | ✅Flink / Kafka |
| [Algorithms](#algorithms) | ✅ Java |
| [Java](#java) | ✅ Multi-thread data generator |
| [Python](#python) | ✅ Data faker |
| [Rust](#rust) | ✅ Command line stock watcher |
| [Linux scripts](./scripts/linux.md) | ✅ Useful Linux scripts|

## Data Warehouse

> This is what I've learned to build up a data warehouse.

| Data Warehouse Projects    |  Detail |
| ----------- | ----------- |
| [Sqoop](./sqoop/sqoop.md) | Several ETL scripts I wrote from RDBMS to Spark on Hive | 
| [SparkSQL](./SQL/src/main/java/useful.sql) | Useful SparkSQLs I wrote |
| [HDFS Operations](./HDFS/src/main/java/boot.java) | HDFS operation api in Java |
| [HDFS Kerberos Login](./HDFS/src/main/java/Main.java) | HDFS Kerberos Multi-thread Login method |
| [HBase API with Kerberos](./HBase/src/main/java/HBaseSecureTest.java) | HBase API with Kerberos Login |
## Real-time Streaming

> These are some codes I wrote for streaming analysis.

| Streaming Projects    |  Detail |
| ----------- | ----------- |
| [Flink Filter with Kafka](./flink/src/main/java/KafkaFilter.java) | Flink benchmark test for data filtering with Kafka |
| [Flink Window Count with Kafka](./flink/src/main/java/KafkaWindowCount.java) | Flink benchmark test for window-grouping count with Kafka |
| [Flink Tumbling Window with Kafka](./flink/src/main/java/KafkaTumblingWindow.java) | Flink benchmark test for tumbling-window with Kafka |
| [Kafka Kerberos scripts](./kafka/kerberos/kerberos-kafka.md) | Useful Kafka cmd client scripts I wrote |
| [Kafka Multi-thread Producer](./kafka/kafka-code/MultithreadKafkaProducer/src/demo/kafka/KafkaMultiProducer.java) | Kafka Java producer for benchmark testing|
| [Kafka Java Consumer with Kerberos]() | Kafka Java consumer for benchmark testing|

## Algorithms

> Algorithms I wrote before...

| Algorithms implemented in Java  |
| ----------- | 
| [Bubble Sort](./Algorithms/JavaAlgorithm/src/main/java/BubbleSort.java) |
| [Insert Sort](./Algorithms/JavaAlgorithm/src/main/java/InsertSort.java) |
| [Merge Sort](./Algorithms/JavaAlgorithm/src/main/java/MergeSort.java) |
| [Quick Sort](./Algorithms/JavaAlgorithm/src/main/java/QuickSort.java) |
| [Shell Sort](./Algorithms/JavaAlgorithm/src/main/java/ShellSort.java) |
| [Binary Insert Sort](./Algorithms/JavaAlgorithm/src/main/java/BinaryInsertSort.java) |
| [Insert Sort VS Bubble Sort](./Algorithms/JavaAlgorithm/src/main/java/InsertionSortVsBubbleSort.java) |
| [Selection Sort](./Algorithms/JavaAlgorithm/src/main/java/SelectionSort.java) |
| [Binary Search](./Algorithms/JavaAlgorithm/src/main/java/BinarySearch.java) |

## Java

| Other Java Projects  |
| ----------- | 
| [Data Generator](./Java/data-generator/src/main/java/Main.java) | An Async Multi-thread NIO Java Data producer |
| [Yet Another Data Generator](./Java/yet-another-data-generator/src/main/java/GenerateDataCRM.java) | With specific business model |

## Python

| Python Projects |
| ----------- | 
| [Python data faker](./python/BiProvider.py) | Python data faker |

## Rust

| Rust Projects |
| ----------- | 
| [Cmd Stock watcher](./Rust/stockup/src/main.rs) | An easy-to-use stock watcher in command line with highlight |

## Linux scripts

| Rust Projects |
| ----------- | 
| [Cmd Stock watcher](./Rust/stockup/src/main.rs) | An easy-to-use stock watcher in command line with highlight |
