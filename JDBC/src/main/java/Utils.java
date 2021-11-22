import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Utils {

    public static Connection conn;

    public static Connection getConnection(Properties props) throws IOException, SQLException {
        // TODO 更加详尽的日志打印
        //        org.apache.log4j.PropertyConfigurator.configure("./JDBC/conf/log4j.properties");

        String driverName = props.getProperty("jdbc_driver");

        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Driver not found in libs");
            System.exit(1);
        }
        String jdbcURL = props.getProperty("jdbcURL");
        System.out.println("Server address：" + jdbcURL);

        System.out.println("Start connecting with " + driverName);

        if (props.containsKey("user") && props.containsKey("password")) {
            String user = props.getProperty("user");
            String password = props.getProperty("password");
            conn = DriverManager.getConnection(jdbcURL, user, password);
            System.out.println("Connecting with ldap: " + user + ": " + password);

        } else {
            conn = DriverManager.getConnection(jdbcURL);
            System.out.println("Server Connected!");
        }
        return conn;
    }

    public static List<Integer> getSeed(int a, int b, int c) {
        // 这个方案有点问题，关键在于插入和update/delete 必需分开。如果batch 比较小可以试试。
        // 获取 100 里面 多少次 insert / update
        List<Integer> SeedList = new ArrayList<>();
        for (int i = 0; i < b+c; i++) {
            int Random_num = new Random().nextInt(a);
            if(!SeedList.contains(Random_num)) {
                SeedList.add(Random_num);
            } else {
                i = i - 1;
            }
        }
        for (int i = 0; i < SeedList.size(); i++) {
            System.out.println(SeedList.indexOf(i));
        }
        return SeedList;
    }

    public static void main(String[] args) {
        List<Integer> x = getSeed(100, 10,10);
        System.out.println("长度" + x.size());
    }
}
