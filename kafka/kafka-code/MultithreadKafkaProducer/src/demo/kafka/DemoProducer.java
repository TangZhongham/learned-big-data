package demo.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class DemoProducer {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis(); //获取开始时间

		Properties props = new Properties();
		props.put("bootstrap.servers", "172.26.4.7:9092");
		props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

//		props.put("security.protocol","SASL_PLAINTEXT");
//		props.put("sasl.mechanism", "GSSAPI");
//		props.put("sasl.kerberos.service.name","kafka");

		//======JAAS 
        //String jaasconfig = " com.sun.security.auth.module.Krb5LoginModule required "
		//+ " useKeyTab=true "
		//+ " storeKey=true "
		//+ " keyTab=\"conf236/kafka.keytab\" "
		//+ " useTicketCache=false "
		//+ " principal=\"kafka/tdh-236@TDH\";" ;  
	    
	    //System.out.println(jaasconfig);
		//props.put("sasl.jaas.config", jaasconfig);
		
		//======System Property
//		System.setProperty("java.security.auth.login.config", "conf236/jaas.conf");
//
//	    System.setProperty("java.security.krb5.conf", "conf236/krb5.conf");

        //生产者进程
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        //发送topic
        String topic="multi2";
        String msg="";

        System.out.println("开始发送");
        for(int i=0;i<100000;i++){
            msg="kafka producer test"+i;
            //不指定分区发送
            //producer.send(new ProducerRecord<String, String>(topic,msg));

            //指定分区发送
            String key=String.valueOf(i);
            try {
				producer.send(new ProducerRecord<String, String>(topic,key,msg)).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            System.out.println(msg);
        }
		long endTime = System.currentTimeMillis(); //获取结束时间
		System.out.println("发送结束");
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间

		producer.close();

	}

}
