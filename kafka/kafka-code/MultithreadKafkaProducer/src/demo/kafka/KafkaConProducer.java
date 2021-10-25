package demo.kafka;

//import com.sun.tools.javah.Util;
import demo.kafka.models.OrderMessage;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaConProducer {



    //发送消息的个数
    private static final int MSG_SIZE = 10000000;
//    private int x = Integer.valueOf(msg);
    //负责发送消息的线程池
    private static ExecutorService executorService
            = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());

    // 单线程
//    private static ExecutorService executorService
//            = Executors.newSingleThreadExecutor();

    private static CountDownLatch countDownLatch
            = new CountDownLatch(MSG_SIZE);


    /*发送消息的任务*/
    private static class ProduceWorker implements Runnable{

        private ProducerRecord<String,String> record;
        private KafkaProducer<String,String> producer;

        public ProduceWorker(ProducerRecord<String, String> record,
                             KafkaProducer<String, String> producer) {
            this.record = record;
            this.producer = producer;
        }

        public void run() {
            final String id = Thread.currentThread().getId()
                    +"-"+System.identityHashCode(producer);
            try {
                producer.send(record, new Callback() {
                    public void onCompletion(RecordMetadata metadata,
                                             Exception e) {
                        if(null!=metadata){
                            System.out.println(id+"|"
                                    +String.format("偏移量：%s,分区：%s",
                                    metadata.offset(),metadata.partition()));
                        }
                        else {
                            e.printStackTrace();
                        }
                    }
                });
//                Thread.sleep(1);
//                System.out.println(id+":数据["+record.key()+"-" + record.value()+"]已发送。");
                countDownLatch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {

        ArgumentParser parser = ArgumentsParser.argParser();
        try {
            Namespace res = parser.parseArgs(args);

            String bootstrapServers = res.getString("BOOTSTRAP_SERVERS");
            String topic = res.getString("Topic");
            String msg_num = res.getString("msg_num");
            String time_type = res.getString("time_type");

            long startTime = System.currentTimeMillis(); //获取开始时间
            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            KafkaProducer<String,String> producer
                    = new KafkaProducer(
                    properties);
            try {
                //循环发送，通过线程池的方式
                for(int i = 0; i<MSG_SIZE; i++){
                    String msg = new OrderMessage(i, "unix-time").toString();
                    System.out.println(msg.getBytes().length);
                    System.out.println(msg);
                    ProducerRecord<String,String> record
                            = new ProducerRecord(
                            topic,null,
                            msg
                    );
                    executorService.submit(new ProduceWorker(record,producer));
                }
                countDownLatch.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                producer.close();
                executorService.shutdown();
                long endTime = System.currentTimeMillis(); //获取结束时间
                System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
            }
        } catch (ArgumentParserException e) {
            if (args.length == 0) {
                parser.printHelp();
                System.exit(0);
            } else {
                parser.handleError(e);
                System.exit(1);
            }
        }


    }
}