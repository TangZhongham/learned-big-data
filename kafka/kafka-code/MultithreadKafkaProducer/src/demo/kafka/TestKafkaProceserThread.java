package demo.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TestKafkaProceserThread {
    //Kafka配置文件
    public static final String TOPIC_NAME = "multi";
    public static final String KAFKA_PRODUCER = "kafka-producer.properties";
    public static final int producerNum=50;//实例池大小
    //阻塞队列实现生产者实例池,获取连接作出队操作，归还连接作入队操作
    public static BlockingQueue<KafkaProducer<String, String>> queue=new LinkedBlockingQueue<>(producerNum);
    //初始化producer实例池
    static {
        for (int i = 0; i <producerNum ; i++) {
            Properties props = new Properties();
//            props.put("", "");
            props.put("bootstrap.servers", "172.26.4.7:9092, 172.26.4.8:9092");
            props.put("key.serializer",
                    "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer",
                    "org.apache.kafka.common.serialization.StringSerializer");
//            Properties props = PropertiesUtil.getProperties(KAFKA_PRODUCER);
            KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
            queue.add(kafkaProducer);
        }
    }
    //生产者发送线程
    static class SendThread extends Thread {
        String msg;
        public SendThread(String msg){
            this.msg=msg;
        }
        public void run(){
            ProducerRecord record = new ProducerRecord(TOPIC_NAME, msg);
            try {
                KafkaProducer<String, String> kafkaProducer =queue.take();//从实例池获取连接,没有空闲连接则阻塞等待
                kafkaProducer.send(record);
                queue.put(kafkaProducer);//归还kafka连接到连接池队列
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //测试
    public static void main(String[]args){
        for (int i = 0; i <100 ; i++) {
            SendThread sendThread=new SendThread("test multi-thread producer!");
            sendThread.start();
        }
    }
}