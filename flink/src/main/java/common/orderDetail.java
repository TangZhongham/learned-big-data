package common;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.training.exercises.common.utils.ExerciseBase;
import org.apache.flink.training.tzh.common.datatypes.Goods;
import org.apache.flink.training.tzh.common.datatypes.Order;
import org.apache.flink.training.tzh.common.sources.GoodsGenerator;
import org.apache.flink.training.tzh.common.sources.OrderGenerator;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

public class orderDetail {
    public static void main(String[] args) throws Exception {

        // set up streaming execution environment
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 9999);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        env.setParallelism(ExerciseBase.parallelism);

        // checkpoint
        // start a checkpoint every 1000 ms
        env.enableCheckpointing(1000);
        // set mode to exactly-once (this is the default)
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);


        // start the data generator
        DataStream<Order> orders = env.addSource(new OrderGenerator())
                .flatMap(new RichFlatMapFunction<Order, Order>() {
                    @Override
                    public void open(Configuration parameters) throws Exception {
                        Cache<String, String> caches = Caffeine.newBuilder()
                                .maximumSize(100) // 设置缓存的最大容量
                                .expireAfterWrite(1, TimeUnit.MINUTES) // 设置缓存在写入一分钟后失效
                                //.concurrencyLevel(10) // 设置并发级别为10
                                .recordStats() // 开启缓存统计
                                .build();
                    }

                    @Override
                    public void flatMap(Order value, Collector<Order> out) throws Exception {
                        out.collect(value);
                    }
                })
                .keyBy(Order::getGoodsId);

        DataStream<Goods> goods = env.addSource(new GoodsGenerator()).keyBy(Goods::getGoodsId);

        DataStream<String> x = orders
                .connect(goods)
                .flatMap(new EnrichmentFunction());

        x.print();

        env.execute("随便跑跑");

    }

    public static class EnrichmentFunction extends RichCoFlatMapFunction<Order, Goods, String> {
        // keyed, managed state
        private ValueState<Order> OrderState;
        private ValueState<Goods> GoodsState;

        @Override
        public void open(Configuration config) {
            OrderState = getRuntimeContext().getState(new ValueStateDescriptor<>("saved order", Order.class));
            GoodsState = getRuntimeContext().getState(new ValueStateDescriptor<>("saved goods", Goods.class));
        }

        @Override
        public void flatMap1(Order order, Collector<String> out) throws Exception {
            Goods goods = GoodsState.value();
            if (goods != null) {
                GoodsState.clear();
                String sb = goods.toString() +
                        order.toString();
                out.collect(sb);
            } else {
                OrderState.update(order);
            }
        }

        @Override
        public void flatMap2(Goods goods, Collector<String> out) throws Exception {

            Order order = OrderState.value();

            if (order != null) {
                OrderState.clear();
                String sb = order.toString() +
                        goods.toString();
                out.collect(sb);
            } else {
                GoodsState.update(goods);
            }
        }
    }
}
