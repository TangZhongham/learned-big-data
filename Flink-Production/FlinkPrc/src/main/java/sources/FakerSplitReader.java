package sources;

import org.apache.flink.connector.base.source.reader.RecordsWithSplitIds;
import org.apache.flink.connector.base.source.reader.splitreader.SplitReader;
import org.apache.flink.connector.base.source.reader.splitreader.SplitsChange;

import java.io.IOException;

public class FakerSplitReader implements SplitReader {
    // 取数逻辑
    // 参考 MockSplitReader
    @Override
    public RecordsWithSplitIds fetch() throws IOException {
        return null;
    }

    @Override
    public void handleSplitsChanges(SplitsChange splitsChanges) {

    }

    @Override
    public void wakeUp() {

    }

    @Override
    public void close() throws Exception {

    }
}
