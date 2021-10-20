package demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class SecureKafkaConsumer {

    public static void main(String[] args) {
        SecureKafkaConsumer kafkaTools = new SecureKafkaConsumer();
        kafkaTools.SecureConsumer();
    }

    public void SecureConsumer() {
        //指定jaas文件
        String jaasconfig = "com.sun.security.auth.module.Krb5LoginModule required  useKeyTab=true storeKey=true keyTab=\"confDemo/kafka.keytab\" principal=\"kafka/linux-pm-0-37@TDH\";";
        //Consumer配置属性
        Properties properties = new Properties();
        //consumer配置
        properties.put("bootstrap.servers", "172.26.0.37:9092,172.26.0.38:9092");
//        properties.put("bootstrap.servers","node-0-80:9092,node-0-81:9092,node-0-82:9092,node-0-83:9092");
        properties.put("group.id", "joy_tzh");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //安全认证配置
        properties.put("security.protocol", "SASL_PLAINTEXT");
        properties.put("sasl.mechanism", "GSSAPI");
        properties.put("sasl.kerberos.service.name", "kafka");
        properties.put("sasl.jaas.config", jaasconfig);

        System.setProperty("java.security.krb5.conf", "confDemo/krb5.conf");

        //消费者进程
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        //发送topic
        consumer.subscribe(Arrays.asList("joy"));
        System.out.println("开始消费");
        try {
            for (int i = 0; i < 21; i++) {
                //每次poll可拉取多个消息
                ConsumerRecords<String, String> records = consumer.poll(200);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset=%d,key=%s,value=%s\n", record.offset(), record.key(), record.value());
                }
            }
        } finally {
            consumer.close();
            System.out.println("消费结束");
        }
    }
}
