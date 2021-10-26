package common.datatypes;

import lombok.Data;
import org.apache.flink.training.tzh.common.sources.DataGenerator;

import java.time.Instant;

@Data
public class Order {

    // 订单时间
    Instant time;

    // 订单 id
    long orderId;

    // 商品 id
    long goodsId;

    // price
    long price;

    // city
    // 可后续关联维表
    int cityId;

//    String order

    public Order(long orderId) {
        DataGenerator g = new DataGenerator(orderId);

        this.orderId = orderId;
        this.price = g.price();
        this.time = g.time();
        this.goodsId = orderId + 1;
    }
}
