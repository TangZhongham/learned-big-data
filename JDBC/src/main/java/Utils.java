import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

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

        System.out.println("Start connecting with Hive");

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
}
