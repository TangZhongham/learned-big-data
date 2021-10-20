package demo.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * Created by tzh on 2020/03/11.
 */

public class SecureChangeOffset {

    public static void main(String[] args) {
        SecureChangeOffset kafkaTools = new SecureChangeOffset();
        kafkaTools.ChangeOffset();
    }

    public void ChangeOffset() {

        //Consumer配置属性
        Properties properties=new Properties();
        //consumer配置
        properties.put("bootstrap.servers","172.26.0.37:9092,172.26.0.38:9092");

        // 指定对应的 consumer group
        properties.put("group.id","joy_tzh");

        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

        //安全认证配置
        //指定jaas文件
        String jaasconfig= "com.sun.security.auth.module.Krb5LoginModule required  useKeyTab=true storeKey=true keyTab=\"confDemo/kafka.keytab\" principal=\"kafka/linux-pm-0-37@TDH\";";

        properties.put("security.protocol","SASL_PLAINTEXT");
        properties.put("sasl.mechanism","GSSAPI");
        properties.put("sasl.kerberos.service.name","kafka");
        properties.put("sasl.jaas.config", jaasconfig);
        System.setProperty("java.security.krb5.conf", "confDemo/krb5.conf");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        // 指定具体 topic
        consumer.subscribe(Arrays.asList("joy"));

        Set<TopicPartition> assignment = new HashSet<>();

        // 在poll()方法内部执行分区分配逻辑，该循环确保分区已被分配。
        // 当分区消息为0时进入此循环，如果不为0，则说明已经成功分配到了分区。
        while (assignment.size() == 0) {
            consumer.poll(100);
            // assignment()方法是用来获取消费者所分配到的分区消息的
            // assignment的值为：topic-demo-3, topic-demo-0, topic-demo-2, topic-demo-1
            assignment = consumer.assignment();
        }
        System.out.println("分区数为" + assignment);

        for (TopicPartition tp : assignment) {
            // 对每个分区轮询的修改 offset
            // 注意：如果该 offset 不存在也会 commit 过去, ./kafka-consumer-groups.sh 可查
            int offset = 66;
            consumer.seek(tp, offset);
            System.out.println("分区 " + tp + " 从 " + offset + " 开始消费");
        }

        // 同步提交会 自动重试
        try {
            consumer.commitSync();
        }
        finally {
            consumer.close();
        }

//        while (true) {
//            ConsumerRecords<String, String> records = consumer.poll(1000);
//            // 消费记录
//            for (ConsumerRecord<String, String> record : records) {
//                System.out.println(record.offset() + ":" + record.value() + ":" + record.partition());
//            }
//        }

    }
}
