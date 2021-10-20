# Kerberos-Kafka

This is a conclusion of how-to-use kafka scripts with Kerberos.

I tested these scripts under Kafka 2.3.0.

## Preparation


add configs  ```consumer.properties``` and ```producer.properties```：

```shell
security.protocol=SASL_PLAINTEXT
sasl.mechanism=GSSAPI
sasl.kerberos.service.name=kafka
```

export env.

```shell
export KAFKA_OPTS="-Djava.security.auth.login.config=/etc/kafka1/conf/jaas.conf -Djava.security.krb5.conf=/etc/kafka1/conf/krb5.conf"
```

**ATTENTION**：assure ```/etc/kafka1/conf/jaas.conf``` **principal** equals **keytab**'s principal ，you may need：

```shell
klist -kt /etc/kafka1/conf/kafka.keytab
```

## Kafka shells with Kerberos

### List all topics

```shell
./kafka-topics.sh --zookeeper linux-pm-0-37:2181 --list
```

###  create topic

**Attention**：```./kafka-broker-topics.sh``` ```bootstrap-server```**should**kafka cluster's**controller**.

```shell
./kafka-broker-topics.sh --bootstrap-server linux-pm-0-38:9092 --create --topic joytwo --partitions 1 --replication-factor 3 --consumer.config ../config/consumer.properties


if not controller：
Failed to create topic "joytwo"
Error code: 41
Message: This is not the correct controller for this cluster.
```

### check topic

```shell
./kafka-topics.sh --zookeeper linux-pm-0-37:2181 --describe --topic joy
```

### produce/consume data

Produce:

```shell
./kafka-console-producer.sh --topic joy --broker-list linux-pm-0-37:9092 --producer.config ../config/producer.properties
```

Consumer:

```shell
./kafka-console-consumer.sh --topic joy --bootstrap-server linux-pm-0-37:9092 --consumer.config ../config/consumer.properties
if you want to start from the beginning instead: --from-beginning
```

### check all the consumer group

```shell
./kafka-consumer-groups.sh --list --bootstrap-server linux-pm-0-38:9092 --command-config ../config/consumer.properties
```

### check consumer group's consuming detail

```shell
./kafka-consumer-groups.sh --describe --group test-consumer-group  --bootstrap-server linux-pm-0-38:9092 --command-config ../config/consumer.properties
```

### Kafka Performance Test

```shell
export KAFKA_OPTS="-Djava.security.auth.login.config=/etc/kafka1/conf/jaas.conf -Djava.security.krb5.conf=/etc/kafka1/conf/krb5.conf"

./kafka-producer-perf-test.sh --num-records 50000000  --topic perftest --producer-props bootstrap.servers=linux-pm-0-37:9092 --throughput 20000000 --record-size 20 --producer.config ../config/producer.properties

./kafka-consumer-perf-test.sh  --broker-list linux-pm-0-37:9092  --messages 10000000 --topic perftest --group g6 --threads 6 --consumer.config ../config/consumer.properties
```