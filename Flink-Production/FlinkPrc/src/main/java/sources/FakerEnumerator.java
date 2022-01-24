package sources;

import org.apache.flink.api.connector.source.SplitEnumerator;
import org.apache.flink.api.connector.source.SplitEnumeratorContext;
import org.apache.flink.api.connector.source.SplitsAssignment;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakerEnumerator implements SplitEnumerator {
    // JM 的获取partition 逻辑
    // 可以把Faker 具体数据场景定义到这里？

    // 负责 定义 Faker 的并行度，可以设置成 读 用户设置的 parallel

    private final List<FakerSourceSplit> splits;
    private final SplitEnumeratorContext<FakerSourceSplit> context;

    public FakerEnumerator(
            List<FakerSourceSplit> splits,
            SplitEnumeratorContext<FakerSourceSplit> context) {
        this.splits = splits;
        this.context = context;
    }

    @Override
    public void start() {

    }

    @Override
    public void handleSplitRequest(int subtaskId, @Nullable String requesterHostname) {

    }

    @Override
    public void addSplitsBack(List splits, int subtaskId) {
        this.splits.addAll(splits);
    }

    @Override
    public void addReader(int subtaskId) {
        if (context.registeredReaders().size() == context.currentParallelism()) {
            int numReaders = context.registeredReaders().size();
            Map<Integer, List<FakerSourceSplit>> assignment = new HashMap<>();
            for (int i = 0; i < splits.size(); i++) {
                assignment
                        .computeIfAbsent(i % numReaders, t -> new ArrayList<>())
                        .add(splits.get(i));
            }
            context.assignSplits(new SplitsAssignment<>(assignment));
            splits.clear();
            for (int i = 0; i < numReaders; i++) {
                context.signalNoMoreSplits(i);
            }
        }
    }

    @Override
    public List<FakerSourceSplit> snapshotState() throws Exception {
        return splits;
    }

    @Override
    public void close() throws IOException {

    }
}
