package demo.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class SecureKafkaProducer {

    public static void main(String[] args) {
        SecureKafkaProducer kafkaTools=new SecureKafkaProducer();
        kafkaTools.SecureProducer();
    }

    public void SecureProducer(){
        //指定jaas文件
        String jaasconfig= "com.sun.security.auth.module.Krb5LoginModule required  useKeyTab=true storeKey=true keyTab=\"confDemo/kafka.keytab\" principal=\"kafka/linux-pm-0-37@TDH\";";
        //Consumer配置属性
        Properties properties=new Properties();
        //consumer配置
        properties.put("bootstrap.servers","172.26.0.37:9092,172.26.0.38:9092");
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        //安全认证配置
        properties.put("security.protocol","SASL_PLAINTEXT");
        properties.put("sasl.mechanism","GSSAPI");
        properties.put("sasl.kerberos.service.name","kafka");
        properties.put("sasl.jaas.config", jaasconfig);

        System.setProperty("java.security.krb5.conf", "confDemo/krb5.conf");


        //生产者进程
        KafkaProducer producer = new KafkaProducer<String, String>(properties);
        //发送topic
        String topic="joy";
        String msg="";

        System.out.println("开始发送");
        for(int i=0;i<20000;i++) try {
            msg="kafka producer test"+i;
            //不指定分区发送
            //producer.send(new ProducerRecord<String, String>(topic,msg));

            //指定分区发送
            String key=String.valueOf(i);
            producer.send(new ProducerRecord<String, String>(topic,key,msg));

            System.out.println(msg);
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException ie){
            ie.printStackTrace();
        }
        System.out.println("发送结束");
        producer.close();

    }
}
