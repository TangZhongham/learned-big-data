import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.common.utils.Exit;

import java.util.Properties;

public class KafkaLatencyCount {
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
            String DEFAULT_JOB_NAME = "Flink Kafka Window";

            if (jobName != null && jobName.length() != 0) {
                Flink_job_name = jobName;
            } else {
                Flink_job_name = DEFAULT_JOB_NAME;
            }

            Properties properties = new Properties();
            properties.setProperty("bootstrap.servers", bootstrapServers);
            properties.setProperty("group.id", groupId);
            properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            DataStream<String> stream = env.
                    addSource(new FlinkKafkaConsumer<String>(sourceTopic, new SimpleStringSchema(), properties));

            FlinkKafkaProducer<String> producer = new FlinkKafkaProducer<String>(
                    toTopic,
                    new SimpleStringSchema(), properties);

            producer.setWriteTimestampToKafka(true);

            stream
//                    .flatMap(new KafkaWordCount.Tokenizer())
//                    .windowAll(TumblingEventTimeWindows.of(Time.seconds(5))
                    .windowAll(TumblingEventTimeWindows.of(Time.minutes(1)))
                    .reduce(new ReduceFunction<String>() {
                        @Override
                        public String reduce(String s, String t1) throws Exception {

                            return t1;
                        }
                    })
//                    .process(new KafkaTumblingWindow.OwnProcessWindowFunction())
                    .addSink(producer);
            env.execute(Flink_job_name);
        }
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
}
