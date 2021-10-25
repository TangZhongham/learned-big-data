import models.OrderMessage;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;
import org.apache.kafka.common.utils.Exit;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/*
华为测试， ETL 过滤
使用方法:
 */
public class KafkaFilter {
    public static void main(String[] args) throws Exception {
        ArgumentParser parser = ArgumentsParser.argParser();
        try {
            Namespace res = parser.parseArgs(args);

            // parse args
            String sourceTopic = res.getString("sourceTopic");
            String bootstrapServers = res.getString("bootstrapServers");
            String groupId = res.getString("groupId");
            String brokerList = res.getString("brokerList");
            String toTopic = res.getString("toTopic");
            String jobName = res.getString("jobName");

            String Flink_job_name;
            String DEFAULT_JOB_NAME = "Flink Kafka Filter type < 1000 && id > -1";

            if (jobName != null && jobName.length() != 0) {
                Flink_job_name = jobName;
            } else {
                Flink_job_name = DEFAULT_JOB_NAME;
            }

            Properties properties = new Properties();
            properties.setProperty("bootstrap.servers", bootstrapServers);
            properties.setProperty("group.id", groupId);
//            properties.setProperty("broker.list", brokerList);
            properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            DataStream<String> stream = env.
                    addSource(new FlinkKafkaConsumer<String>(sourceTopic, new SimpleStringSchema(), properties));

            FlinkKafkaProducer<String> producer = new FlinkKafkaProducer<String>(
                    toTopic,
                    new SimpleStringSchema(), properties);

            producer.setWriteTimestampToKafka(true);

            stream.flatMap(new FlatMapFunction<String, String>() {
                @Override
                public void flatMap(String s, Collector<String> collector) throws Exception {
                    List<String> result = Arrays.asList(s.split(","));
                    Integer id = Integer.valueOf(result.get(0));
                    String name = result.get(1);
                    Integer type = Integer.valueOf(result.get(2));
//                    Integer time = Integer.valueOf(result.get(3));
                    Long time = Long.parseLong(result.get(3));
                    // 这里故意拖慢性能看看
                    OrderMessage omg = new OrderMessage(id, name, type, time);

                    // 判断条件
                    if (type < 1000 && id > -1 ) {
                        collector.collect(omg.toString());
                    }
                }
            }).addSink(producer);
            env.execute(Flink_job_name);}

        catch (ArgumentParserException e) {
            if (args.length == 0) {
                parser.printHelp();
                Exit.exit(0);
            } else {
                parser.handleError(e);
                Exit.exit(1);
            }
        }


    }

    public static final class Tokenizer implements FlatMapFunction<String, String> {
        @Override
        public void flatMap(String s, Collector<String> collector) throws Exception {
            long timestamp = System.currentTimeMillis();// 会比date快
            StringBuilder builder = new StringBuilder();
            builder.append(s);
            builder.append(String.valueOf(timestamp));
//            String date =
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//            String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
            collector.collect(builder.toString());
        }
    }


    // 读取kafka消息， 打时间戳， 写入kafka
    public class MsgSplitter implements FlatMapFunction<String, String> {
        @Override
        public void flatMap(String value, Collector<String> collector) throws Exception {
            if (value != null && value.contains(",")) {
                String[] parts = value.split(",");
                collector.collect(value);
            }
        }
    }
}
