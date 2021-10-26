package common.sources;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import common.datatypes.Goods;

import java.util.ArrayList;
import java.util.List;

public class GoodsGenerator implements SourceFunction<Goods> {
    public static final int SLEEP_MILLIS_PER_EVENT = 10;
    private static final int BATCH_SIZE = 5;
    private volatile boolean running = true;

    @Override
    public void run(SourceContext<Goods> ctx) throws Exception {
        long goodId = 0;

        while (running) {
            List<Goods> startEvents = new ArrayList<>(BATCH_SIZE);
            for (int i = 1; i < BATCH_SIZE; i++) {
                Goods goods = new Goods(goodId + 1);
                startEvents.add(goods);
            }

            startEvents.forEach(ctx::collect);

            goodId += BATCH_SIZE;

            // don't go too fast
            Thread.sleep(BATCH_SIZE * SLEEP_MILLIS_PER_EVENT);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
