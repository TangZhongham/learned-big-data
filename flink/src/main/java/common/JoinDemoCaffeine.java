package common;

import com.github.benmanes.caffeine.cache.*;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.training.exercises.common.utils.ExerciseBase;
import org.apache.flink.training.tzh.common.datatypes.Order;
import org.apache.flink.training.tzh.common.sources.OrderGenerator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JoinDemoCaffeine {

    public static void main(String[] args) throws Exception {

        // set up streaming execution environment
        Configuration conf = new Configuration();
//        conf.setInteger("rest.port", 9999);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        env.setParallelism(ExerciseBase.parallelism);

        DataStream<String> orders = env.addSource(new OrderGenerator())
                .map(new MapJoinDemo());

        orders.print();

        env.execute("单纯使用 Caffeine 进行缓存");

    }

    static class MapJoinDemo extends RichMapFunction<Order, String> {
        LoadingCache<Long, Integer> caches;

        @Override
        public void open(Configuration parameters) throws Exception {
            caches = Caffeine
                    .newBuilder()
                    .maximumSize(100) // 设置缓存的最大容量
                    .expireAfterWrite(1, TimeUnit.MINUTES) // 设置缓存在写入一分钟后失效
                    //.concurrencyLevel(10) // 设置并发级别为10
                    .recordStats() // 开启缓存统计
                    .removalListener(new RemovalListener<Long, Integer>() {
                        @Override
                        public void onRemoval(@Nullable Long key, @Nullable Integer value, @NonNull RemovalCause cause) {
                            System.out.println(key + "被移除了，值为：" + value);
                            System.out.println(cause.toString());

                        }

                    })
                    .build(
                            //指定加载缓存的逻辑
                            new CacheLoader<Long, Integer>() {
                                @Override
                                public @Nullable Integer load(Long orderId) throws Exception {
                                    Integer price = readFromHbase(orderId);
                                    return price;
                                }
                            }
                    );
        }


        private static Integer readFromHbase(Long orderId) {
            //读取hbase
            //这里写死，模拟从hbase读取数据
            Map<Long, Integer> temp = new HashMap<>();

            temp.put(51L, 9999);
            temp.put(52L, 9999);
            temp.put(53L, 9999);
            temp.put(54L, 9999);
            Integer price = 0;
            if (temp.containsKey(orderId)) {
                price = temp.get(orderId);
            }

            return price;
        }

        @Override
        public String map(Order value) throws Exception {
            //在map方法中进行主流和维表的关联
            Integer price = 0;
            // 如过存在 orderId
            if (caches.get(value.getOrderId())  != 0) {
                price = caches.get(value.getOrderId());
                value.setPrice(price);
            }
            return value.toString();
        }
    }


}
