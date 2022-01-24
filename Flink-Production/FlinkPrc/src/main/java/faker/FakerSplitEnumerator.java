package faker;

import org.apache.flink.api.connector.source.SplitEnumerator;

import org.apache.flink.api.connector.source.Source;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public class FakerSplitEnumerator implements SplitEnumerator {
    @Override
    public void start() {

    }

    @Override
    public void handleSplitRequest(int i, @Nullable String s) {

    }

    @Override
    public void addSplitsBack(List list, int i) {

    }

    @Override
    public void addReader(int i) {

    }

    @Override
    public Object snapshotState(long l) throws Exception {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
