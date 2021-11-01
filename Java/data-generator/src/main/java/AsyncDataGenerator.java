import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.concurrent.*;

public class AsyncDataGenerator extends DataGenerator implements Callable {
    ArrayList<String> column_list;
    String delimiter;
    int buffer_io;

    public AsyncDataGenerator(ArrayList<String> column_list, String delimiter, int buffer_io) {
        super("");
        this.column_list = column_list;
        this.delimiter = delimiter;
        this.buffer_io = buffer_io;
    }

    public AsyncDataGenerator() {
        super("");

    }

    @Override
    public StringJoiner call() throws Exception {
        StringJoiner sb = new StringJoiner("", "", "\n");
        for (int k=0;k<buffer_io;k++) {
            // 改成 异步执行？
            sb.merge(AsyncDataGenerator.get_all(column_list, delimiter));
        }
        return sb;
    }
}
