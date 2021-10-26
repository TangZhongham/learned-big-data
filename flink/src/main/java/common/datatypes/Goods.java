package common.datatypes;

import lombok.Data;

import java.io.Serializable;
import java.util.Random;

@Data
public class Goods implements Serializable {

    // goods id
    long goodsId;

    // price
    int price;

    // goodsName
    String goodsName;

    public Goods(long goodsId) {
        this.goodsId = goodsId;
        this.price = (int) (goodsId + new Random(goodsId).nextInt());
        this.goodsName = "tzhANDzzw";
    }

}
