import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final String user = "hdfs/linux-4-35@TDH";

    public static void main(String[] args) throws IOException {
//        HDFSOperation.listDir("/", user);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 1000; i++) {
            executor.submit(new Task("" + i));
        }

        executor.shutdown();

    }

    static class Task implements Runnable {
        private final String name;

        public Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("start task " + name);
            try {
                HDFSOperation.listDir("/", user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("end task " + name);
        }
    }
}
