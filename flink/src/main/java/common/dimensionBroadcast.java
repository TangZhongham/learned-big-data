package common;

import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import common.utils.ExerciseBase;
import common.datatypes.Goods;
import common.datatypes.Order;
import common.sources.GoodsGenerator;
import common.sources.OrderGenerator;
import org.apache.flink.util.Collector;

import java.util.Objects;

public class dimensionBroadcast {
    public static void main(String[] args) throws Exception {

        // set up streaming execution environment
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 9999);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        env.setParallelism(ExerciseBase.parallelism);

        // start the data generator
        DataStream<Order> orders = env.addSource(new OrderGenerator())
                .uid("Order Source")
                .filter(Objects::nonNull)
                ;

        DataStream<Goods> goods = env.addSource(new GoodsGenerator()).
                uid("Goods Detail");
//                .keyBy(Goods::getGoodsId);


        // 存储 维度信息的 MapState
        final MapStateDescriptor<Integer, String> GOODS_STATE = new MapStateDescriptor<>(
                "GOODS_STATE",
                BasicTypeInfo.INT_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO);

        DataStream<String> x = orders
                .connect(goods.broadcast(GOODS_STATE))
                .process(new BroadcastProcessFunction<Order, Goods, String>() {
                    @Override
                    public void processElement(Order order, ReadOnlyContext ctx, Collector<String> out) throws Exception {
                        ReadOnlyBroadcastState<Integer, String> broadcastState = ctx.getBroadcastState(GOODS_STATE);

                        String goodsName = broadcastState.get((int) order.getGoodsId());
//                        Integer goodsId = broadcastState.get(goodsName);

                        out.collect(order.getGoodsId() + goodsName+order.toString());
                    }

                    @Override
                    public void processBroadcastElement(Goods value, Context ctx, Collector<String> out) throws Exception {
                        BroadcastState<Integer, String> broadcastState = ctx.getBroadcastState(GOODS_STATE);

                        broadcastState.put((int) value.getGoodsId(), value.getGoodsName());

                    }
                });

        x.print();

        env.execute("Test broadcast");



    }
}
