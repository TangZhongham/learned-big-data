package demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaConConsumer {
    private static final int CONCURRENT_PARTITIONS_COUNT = 2;
    private static CountDownLatch countDownLatch
            = new CountDownLatch(100000);
    private static ExecutorService executorService
            = Executors.newFixedThreadPool(CONCURRENT_PARTITIONS_COUNT);

    private static class ConsumerWorker implements Runnable{

        private KafkaConsumer<String,String> consumer;

        public ConsumerWorker(Properties properties, String topic) {
            this.consumer = new KafkaConsumer(properties);
            consumer.subscribe(Collections.singletonList(topic));
        }

        public void run() {
            final String id = Thread.currentThread().getId()
                    +"-"+System.identityHashCode(consumer);
            try {
                while(true){
                    ConsumerRecords<String, String> records
                            = consumer.poll(200);
                    for(ConsumerRecord<String, String> record:records){
                        System.out.println(id+"|"+String.format(
                                "主题：%s，分区：%d，偏移量：%d，" +
                                        "key：%s，value：%s",
                                record.topic(),record.partition(),
                                record.offset(),record.key(),record.value()));
                        //do our work
                    }
                    countDownLatch.countDown();
                }
            } finally {
                consumer.close();
            }
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis(); //获取开始时间

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"172.26.4.7:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"concurrent1");
        properties.put("auto.offset.reset","earliest");

        try {
            for(int i = 0; i<CONCURRENT_PARTITIONS_COUNT; i++){
                executorService.submit(new ConsumerWorker(properties,
                        "multi1"));
            }
            countDownLatch.await();
        } catch (Exception e ) {
                e.printStackTrace();
        }
        finally {
            executorService.shutdown();
            long endTime = System.currentTimeMillis(); //获取结束时间
            System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
        }

//        for(int i = 0; i<CONCURRENT_PARTITIONS_COUNT; i++){
//            executorService.submit(new ConsumerWorker(properties,
//                    "multi1"));
//        }

    }
}