package common;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.training.exercises.common.utils.ExerciseBase;
import org.apache.flink.training.tzh.common.datatypes.Order;
import org.apache.flink.training.tzh.common.sources.OrderGenerator;

public class JoinDemoBroadcast {

    public static void main(String[] args) throws Exception {

        // set up streaming execution environment
        Configuration conf = new Configuration();
//        conf.setInteger("rest.port", 9999);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        env.setParallelism(ExerciseBase.parallelism);

        DataStream<Order> orders = env.addSource(new OrderGenerator());

    }

    }
