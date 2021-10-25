package models;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class KafkaMsgSchema implements KafkaDeserializationSchema<Tuple4<Integer, String, Integer, Integer>> {
    @Override
    public boolean isEndOfStream(Tuple4<Integer, String, Integer, Integer> s) {
        return false;
    }

    @Override
    public Tuple4<Integer, String, Integer, Integer> deserialize(ConsumerRecord<byte[], byte[]> consumerRecord) throws Exception {

        return new Tuple4<Integer, String, Integer, Integer>(1, "x", 2, 3);
    }

    @Override
    public TypeInformation<Tuple4<Integer, String, Integer, Integer>> getProducedType() {
        return null;
    }
}
