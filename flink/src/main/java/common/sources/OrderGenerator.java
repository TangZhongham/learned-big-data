package common.sources;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import common.datatypes.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderGenerator implements SourceFunction<Order> {

    public static final int SLEEP_MILLIS_PER_EVENT = 10;
    private static final int BATCH_SIZE = 5;
    private volatile boolean running = true;

    @Override
    public void run(SourceContext<Order> ctx) throws Exception {

//        PriorityQueue<Order> pq = new PriorityQueue<>(100);
        long orderId = 0;
        long maxStartTime = 0;

        while (running) {

            List<Order> startEvents = new ArrayList<>(BATCH_SIZE);
            for (int i = 1; i < BATCH_SIZE; i++) {
                Order order = new Order(orderId + 1);
                startEvents.add(order);

                maxStartTime = Math.max(maxStartTime, order.getTime().toEpochMilli());
            }

            startEvents.iterator().forEachRemaining(r -> ctx.collectWithTimestamp(r, r.getTime().toEpochMilli()));
            ctx.emitWatermark(new Watermark(maxStartTime));

            // 下一批数据
            orderId += 1;

            // don't go too fast
            Thread.sleep(BATCH_SIZE * SLEEP_MILLIS_PER_EVENT);
        }

    }

    @Override
    public void cancel() {
        running = false;
    }
}
