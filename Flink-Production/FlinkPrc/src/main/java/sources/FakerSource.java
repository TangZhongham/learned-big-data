package sources;

import org.apache.flink.api.connector.source.*;
import org.apache.flink.core.io.SimpleVersionedSerializer;

import java.util.ArrayList;
import java.util.List;

public class FakerSource implements Source {

    private final int numSplits;
    private final int numRecordsPerSplit;
    private final int startingValue;
    private final Boundedness boundedness;

    public FakerSource(
            int numSplits,
            int numRecordsPerSplit,
            int startingValue,
            Boundedness boundedness) {
        this.numSplits = numSplits;
        this.numRecordsPerSplit = numRecordsPerSplit;
        this.startingValue = startingValue;
        this.boundedness = boundedness;
    }

    @Override
    public Boundedness getBoundedness() {
        return null;
    }

    @Override
    public FakerSourceReader createReader(SourceReaderContext readerContext) throws Exception {
        return new FakerSourceReader();
    }

    @Override
    public FakerEnumerator createEnumerator(SplitEnumeratorContext context) throws Exception {
        List<FakerSourceSplit> splits = new ArrayList<>();
        return new FakerEnumerator(splits, context);
    }

    @Override
    public SplitEnumerator restoreEnumerator(
            SplitEnumeratorContext enumContext,
            Object checkpoint) throws Exception {
        return null;
    }

    @Override
    public SimpleVersionedSerializer getSplitSerializer() {
        return null;
    }

    @Override
    public SimpleVersionedSerializer getEnumeratorCheckpointSerializer() {
        return null;
    }
}
