package common.sources;

import java.time.Instant;
import java.util.Random;

public class DataGenerator {

    private static final int SECONDS_BETWEEN_RIDES = 20;
    private static final int NUMBER_OF_DRIVERS = 200;
    private static final Instant beginTime = Instant.parse("2020-01-01T12:00:00.00Z");

    private transient long orderId;

    public DataGenerator(long orderId) {
        this.orderId = orderId;
    }

    public long price() {
        Random rnd = new Random(orderId);
        return 2013000000 + rnd.nextInt(NUMBER_OF_DRIVERS);
    }

    public Instant time() {
        return Instant.now();
    }

}
