package demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by sunsh on 2018/2/5.
 */
public class SecureKafkaDemo {

    public static void main(String[] args) {
        SecureKafkaDemo kafkaTools=new SecureKafkaDemo();
        kafkaTools.SecureProducer();
//        kafkaTools.SecureConsumer();
    }

    public void SecureProducer(){
        //指定jaas文件
        String jaasconfig= "com.sun.security.auth.module.Krb5LoginModule required  useKeyTab=true storeKey=true keyTab=\"conf_tzh/kafka.keytab\" principal=\"kafka/linux-158-14@TDH\";";
        //Consumer配置属性
        Properties properties=new Properties();
        //consumer配置
        properties.put("bootstrap.servers","172.16.158.14:9092");
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        //安全认证配置
        properties.put("security.protocol","SASL_PLAINTEXT");
        properties.put("sasl.mechanism","GSSAPI");
        properties.put("sasl.kerberos.service.name","kafka");
        properties.put("sasl.jaas.config", jaasconfig);

        System.setProperty("java.security.krb5.conf", "conf_tzh/krb5.conf");


        //生产者进程
        KafkaProducer producer = new KafkaProducer<String, String>(properties);
        //发送topic
        String topic="ha";
        String msg="";

        System.out.println("开始发送");
        for(int i=0;i<21;i++){
            msg="kafka producer test"+i;
            //不指定分区发送
            //producer.send(new ProducerRecord<String, String>(topic,msg));


            //指定分区发送
            String key=String.valueOf(i);
            producer.send(new ProducerRecord<String, String>(topic,key,msg));

            System.out.println(msg);
        }
        System.out.println("发送结束");
        producer.close();

    }
    public void SecureConsumer(){
        //指定jaas文件
        String jaasconfig= "com.sun.security.auth.module.Krb5LoginModule required  useKeyTab=true storeKey=true keyTab=\"conf_tzh/kafka.keytab\" principal=\"kafka/linux-158-14@TDH\";";
        //Consumer配置属性
        Properties properties=new Properties();
        //consumer配置
        properties.put("bootstrap.servers","linux-158-14:9092");
//        properties.put("bootstrap.servers","node-0-80:9092,node-0-81:9092,node-0-82:9092,node-0-83:9092");
        properties.put("group.id","test888");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        //安全认证配置
        properties.put("security.protocol","SASL_PLAINTEXT");
        properties.put("sasl.mechanism","GSSAPI");
        properties.put("sasl.kerberos.service.name","kafka");
        properties.put("sasl.jaas.config", jaasconfig);

        System.setProperty("java.security.krb5.conf", "conf_tzh/krb5.conf");

        //消费者进程
        KafkaConsumer<String,String> consumer=new KafkaConsumer<String, String>(properties);
        //发送topic
        consumer.subscribe(Arrays.asList("ha"));
        System.out.println("开始消费");
        try {
            for(int i=0;i<21;i++){
            //每次poll可拉取多个消息
            ConsumerRecords<String,String> records=consumer.poll(200);
            for(ConsumerRecord<String,String> record : records){
                System.out.printf("offset=%d,key=%s,value=%s\n",record.offset(),record.key(),record.value());
            }
        }
    } finally {
        consumer.close();
        System.out.println("消费结束");
    }

}

}
