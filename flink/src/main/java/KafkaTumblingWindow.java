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

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class KafkaTumblingWindow {

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
            String DEFAULT_JOB_NAME = "Kafka Lag Counting";

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
                    .aggregate(new AggregateFunction<String, Tuple2<Long, Integer>, String>() {
                        @Override
                        public Tuple2<Long, Integer> createAccumulator() {
                            return new Tuple2<>(0L, 0);
                        }

                        @Override
                        public Tuple2<Long, Integer> add(String s, Tuple2<Long, Integer> accumulator) {
                            List<String> result = Arrays.asList(s.split(","));
                            Long time1 = Long.parseLong(result.get(3));
                            Long time2 = Long.parseLong(result.get(4));
                            return new Tuple2<>(accumulator.f0 + (time2-time1), accumulator.f1 + 1);
                        }

                        @Override
                        public String getResult(Tuple2<Long, Integer> o) {
                            //                            sb.append(o.f0);
//                            sb.append(",");
//                            sb.append(o.f1);
//                            return sb.toString();
                            return o.f0 / o.f1 +
                                    "," +
                                    o.f1;
                        }

                        @Override
                        public Tuple2<Long, Integer> merge(Tuple2<Long, Integer> a, Tuple2<Long, Integer> b) {
                            return new Tuple2<>(a.f0+b.f0, a.f1+b.f1);
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
