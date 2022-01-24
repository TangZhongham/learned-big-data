package sources;

import org.apache.flink.api.connector.source.SourceReaderContext;
import org.apache.flink.api.connector.source.SourceSplit;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.base.source.reader.RecordEmitter;
import org.apache.flink.connector.base.source.reader.SourceReaderBase;
import org.apache.flink.connector.base.source.reader.fetcher.SplitFetcherManager;
import org.apache.flink.connector.base.source.reader.synchronization.FutureCompletingBlockingQueue;

import java.util.Map;

public class FakerSourceReader extends SourceReaderBase {

    // TM 的线程模型

    public FakerSourceReader(
            FutureCompletingBlockingQueue elementsQueue,
            SplitFetcherManager splitFetcherManager,
            RecordEmitter recordEmitter,
            Configuration config,
            SourceReaderContext context) {
        super(elementsQueue, splitFetcherManager, recordEmitter, config, context);
    }

    @Override
    protected void onSplitFinished(Map finishedSplitIds) {

    }

    @Override
    protected Object initializedState(SourceSplit split) {
        return null;
    }

    @Override
    protected SourceSplit toSplitType(String splitId, Object splitState) {
        return null;
    }
}
