package common;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import common.utils.ExerciseBase;
import common.datatypes.Order;
import common.sources.OrderGenerator;

import java.util.HashMap;
import java.util.Map;

public class JoinDemoHashMap {

    public static void main(String[] args) throws Exception {

        // set up streaming execution environment
        Configuration conf = new Configuration();
//        conf.setInteger("rest.port", 9999);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        env.setParallelism(ExerciseBase.parallelism);

        DataStream<String> orders = env.addSource(new OrderGenerator())
                .map(new MapJoinDemo());

        orders.print();

        env.execute("单纯使用 HashMap 进行缓存");

    }

    static class MapJoinDemo extends RichMapFunction<Order, String> {
        //定义一个变量，用于保存维表数据在内存
        Map<Long, Integer> dim;

        @Override
        public void open(Configuration parameters) throws Exception {
            //在open方法中读取维表数据，可以从数据中读取、文件中读取、接口中读取等等。
            dim = new HashMap<>();
            dim.put(55L, 999);
            dim.put(54L, 999);
            dim.put(56L, 56);
            dim.put(34L, 33);
        }

        @Override
        public String map(Order value) throws Exception {
            //在map方法中进行主流和维表的关联
            Integer price = 0;
            // 如过存在 orderId
            if (dim.containsKey(value.getOrderId())) {
                price = dim.get(value.getOrderId());
                value.setPrice(price);
            }
            return value.toString();
        }
    }
}