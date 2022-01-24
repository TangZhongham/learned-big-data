package sources;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.connector.source.Boundedness;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import static org.apache.flink.api.connector.source.Boundedness.CONTINUOUS_UNBOUNDED;

public class FakerJob {
    public static void main(String[] args) {

        ParameterTool parameters = ParameterTool.fromArgs(args);

        int parallelism = Integer.getInteger(parameters.get("mapParallelism", "2"));

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        int numSplits = 1;
        int numRecordsPerSplit = 1;
        int startingValue = 0;
        Boundedness boundedness = CONTINUOUS_UNBOUNDED;
        FakerSource fakerSource = new FakerSource(
                numSplits,
                numRecordsPerSplit,
                startingValue,
                boundedness);

        DataStream<Integer> stream = env.fromSource(
                fakerSource,
                WatermarkStrategy.noWatermarks(),
                "MySourceName")
                ;

        stream.print().setParallelism(parallelism);;

    }
}
