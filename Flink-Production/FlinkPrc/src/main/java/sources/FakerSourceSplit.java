package sources;

import org.apache.flink.api.connector.source.SourceSplit;

public class FakerSourceSplit implements SourceSplit {
    @Override
    public String splitId() {
        return null;
    }
}
