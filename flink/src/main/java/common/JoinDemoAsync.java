package common;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import common.datatypes.Order;
import common.sources.OrderGenerator;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JoinDemoAsync {
    public static void main(String[] args) throws Exception {

        // set up streaming execution environment
        Configuration conf = new Configuration();
//        conf.setInteger("rest.port", 9999);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        env.setParallelism(ExerciseBase.parallelism);

        DataStream<Order> orders = env.addSource(new OrderGenerator());

        DataStream<Tuple3<String, Integer, String>> async = AsyncDataStream
                .orderedWait(orders, new JoinDemoAsyncFunction(), 1000L
                , TimeUnit.MILLISECONDS, 2);


        async.print();

        env.execute("单纯使用 异步每次去 mysql 查询");

    }

    static class JoinDemoAsyncFunction extends RichAsyncFunction<Order, Tuple3<String, Integer, String>> {

        // 链接
        private static String jdbcUrl = "jdbc:mysql://192.168.145.1:3306?useSSL=false";
        private static String username = "root";
        private static String password = "123";
        private static String driverName = "com.mysql.jdbc.Driver";
        java.sql.Connection conn;
        PreparedStatement ps;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);

            Class.forName(driverName);
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            ps = conn.prepareStatement("select city_name from tmp.city_info where id = ?");
        }

        // 异步查询方法
        @Override
        public void asyncInvoke(Order input, ResultFuture<Tuple3<String, Integer, String>> resultFuture) throws Exception {
            // 使用 city id 查询
            ps.setInt(1, (int) input.getOrderId());
            ResultSet rs = ps.executeQuery();
            String cityName = null;
            if (rs.next()) {
                cityName = rs.getString(1);
            }
            List list = new ArrayList<Tuple2<Integer, String>>();
            list.add(new Tuple3<>(input.getOrderId(),input.getPrice(), cityName));
            resultFuture.complete(list);
        }

        @Override
        public void close() throws Exception {
            super.close();
            conn.close();
        }
    }
}
