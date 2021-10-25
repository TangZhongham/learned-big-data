import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;
import org.apache.kafka.common.utils.Exit;

import java.util.Properties;

public class KafkaWindowCount {

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
            Integer windowTime = Integer.parseInt(res.getString("windowTime"));

            String Flink_job_name;
            String DEFAULT_JOB_NAME = "Flink Kafka Window Count";

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

            env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

            FlinkKafkaProducer<String> producer = new FlinkKafkaProducer<String>(
                    toTopic,
                    new SimpleStringSchema(), properties);

            producer.setWriteTimestampToKafka(true);

            stream
//                    .flatMap(new KafkaWordCount.Tokenizer())
//                    .windowAll(TumblingEventTimeWindows.of(Time.seconds(5))
                    .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(windowTime)))
                    .aggregate(new AggregateFunction<String, Tuple2<Integer, String>, String>() {
                        @Override
                        public Tuple2<Integer, String> createAccumulator() {
                            return new Tuple2<>(0, "");
                        }

                        @Override
                        public Tuple2<Integer, String> add(String s, Tuple2<Integer, String> accumulator) {
                            if (!s.isEmpty()) {
                                String time = String.valueOf(System.currentTimeMillis());
                                return new Tuple2<>(accumulator.f0 + 1, time);
                            }
                            return new Tuple2<>(0, "");
                        }

                        @Override
                        public String getResult(Tuple2<Integer, String> o) {
                            return o.toString();
                        }

                        @Override
                        public Tuple2<Integer, String> merge(Tuple2<Integer, String> a, Tuple2<Integer, String> b) {
                            return new Tuple2<>(a.f0+b.f0, "");
                        }
                    })
//                    .process(new OwnProcessWindowFunction())
                    .addSink(producer);
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
}
