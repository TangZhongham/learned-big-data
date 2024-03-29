import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class JDBCExample {

    //    public static Connection conn;
    public static File file;
    public static ResultSet rs;
    public static Statement stmt;
    public static Connection conn;


    public static void main(String[] args) throws SQLException, IOException {
//        ./JDBC/conf/setting.properties
//        jdbc_driver=org.apache.hive.jdbc.HiveDriver
//        jdbcURL=jdbc:hive2://172.26.4.13:10000/default
//        user=hive
//        password=123456
//        query=select * from orctzh;

        if (args.length > 0) {
            file = new File(args[0]);
        } else {
            file = new File("./JDBC/conf/setting.properties");
            System.out.println("use default config in ./conf/setting.properties");
        }
        Properties props = new Properties();

        // if file does not exist:FileNotFoundException
        if (file.exists()) {
            props.load(new FileReader(file));
        } else {
            System.out.println("setting.properties NOT FOUND");
            System.exit(1);
        }

        try {
            conn = Utils.getConnection(props);
            // connect
            stmt = conn.createStatement();

//            PreparedStatement ps = conn.prepareStatement("select * from tsadf;");

            // Start to query
            if (props.containsKey("query")) {
                String query = props.getProperty("query");
                System.out.println("executing query\n" + query + "\n结果:");
                rs = stmt.executeQuery(query);
            } else {
                System.out.println("query does not exist，execute default sql: select * from system.dual;");
                rs = stmt.executeQuery("select * from system.dual;");
            }

            if (rs != null) {

                ResultSetMetaData rsmd = rs.getMetaData();
                int size = rsmd.getColumnCount();
                while(rs.next()) {
                    StringBuffer value = new StringBuffer();
                    for(int i = 0; i < size; i++) {
                        value.append(rs.getString(i+1)).append("\t");
                    }
                    System.out.println(value.toString());
                } }
        }
        catch (SQLException ex) {
            System.err.println(new java.util.Date()+" : "+ex.getMessage());
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            stmt.close();
            conn.close();
        }
    }
}
