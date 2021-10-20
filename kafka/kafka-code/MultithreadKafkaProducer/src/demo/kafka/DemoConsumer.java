package demo.kafka;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class DemoConsumer {
	
	public static void main(String[] args)
	{
		Properties props = new Properties();
		
		props.put("bootstrap.servers", "tdh-236:9092");
	    props.put("group.id", "guo_test");
//	    props.put("enable.auto.commit", "true");
//	    props.put("auto.commit.interval.ms", "1000");
//	    props.put("session.timeout.ms", "30000");
	    props.put("key.deserializer",
	                "org.apache.kafka.common.serialization.StringDeserializer");
	    props.put("value.deserializer",
	                "org.apache.kafka.common.serialization.StringDeserializer");
	    
//	    String jaasconfig = "com.sun.security.auth.module.Krb5LoginModule required "
//	    		+ " useKeyTab=true "
//	    		+ " storeKey=true "
//	    		+ " keyTab=\"conf24/kafka.keytab\" "
//	    		+ " principal=\"kafka/tdh-24@TDH\";" ;  
//
//	    props.put("security.protocol","SASL_PLAINTEXT");  
//	    props.put("sasl.mechanism", "GSSAPI");
//	    props.put("sasl.kerberos.service.name","kafka");  
//	    props.put("sasl.jaas.config", jaasconfig);
	    
	    
	    //安全配置
	    props.put("security.protocol", "SASL_PLAINTEXT");
	    props.put("sasl.mechanism", "GSSAPI");
	    props.put("sasl.kerberos.service.name", "kafka");
        System.setProperty("java.security.auth.login.config", "conf236/jaas.conf");
        System.setProperty("java.security.krb5.conf", "conf236/krb5.conf");

		
		
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList("guo"));
		
		System.out.println("Subscribed to topic " + "guo");
	        
	    while (true) 
	    {
	    	ConsumerRecords<String, String> records = consumer.poll(1000);
	        for (ConsumerRecord<String, String> record : records)
	        {
	            System.out.println("offset=" + record.offset() + ", " +
	                		           "key="    + record.key()    + ", " +
	                		           "value="  + record.value());
	        }
	    }
	}

}
