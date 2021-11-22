import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class OracleJDBC {

    //    public static Connection conn;
    public static File file;
    public static ResultSet rs;
    public static Statement stmt;
    public static Connection conn;
    public static int factor = 5;
    public static int loop = 100;

    public static int counter = 0;

    public static String insertSQL = "INSERT INTO LD.test_tzh2 VALUES(?,?)";
    public static String deleteSQL = "DELETE FROM LD.test_tzh2 WHERE id = ?";
    public static String updateSQL = "UPDATE LD.test_tzh2 SET name=? WHERE id = ?";

    public static long start_time;
    public static long end_time;


    public static void main(String[] args) throws SQLException, IOException {
        start_time = System.currentTimeMillis();

//        InsertOracle("test");

        if (args.length > 0) {
            file = new File(args[0]);
        } else {
            file = new File("./JDBC/conf/setting_oracle.properties");
            System.out.println("use default config in ./conf/setting_oracle.properties");
        }
        Properties props = new Properties();

        // if file does not exist:FileNotFoundException
        if (file.exists()) {
            props.load(new FileReader(file));
        } else {
            System.out.println("setting_oracle.properties NOT FOUND");
            System.exit(1);
        }

        try {
            conn = Utils.getConnection(props);
            // connect
            stmt = conn.createStatement();
            stmt.executeQuery("select * from LD.test_tzh2");

            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
            PreparedStatement deleteStatement = conn.prepareStatement(deleteSQL);
            PreparedStatement updateStatement = conn.prepareStatement(updateSQL);

            for (int x = 0; x < 10; x++) {
                for (int i = 0; i < 110; i++) {
                    if(i < 100) {
                        preparedStatement.setInt(1, i);
//                        preparedStatement.setString(2, "Rick");
                        preparedStatement.setString(2, "Rick"+String.valueOf(i));
                        System.out.println(preparedStatement);
                        preparedStatement.execute();
                        counter++;
                    } else if ( i >100 && i < 105) {
                        // delete
                        // DELETE FROM test_tzh2 WHERE id = 2;
                        deleteStatement.setInt(1, i-5);
                        deleteStatement.execute();
                    } else {
                        // update
                        // UPDATE test_tzh2 SET name='Morty' WHERE id = 2;
                        updateStatement.setString(1, "Morty"+(String.valueOf(i-10)));
                        updateStatement.setInt(2, i-10);
                        updateStatement.execute();
                    }
                }
            }
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
            end_time = System.currentTimeMillis();
            System.out.println("Time" + (end_time-start_time) + " ms...");
            System.out.println("插入"+ counter + "次");
        }
    }


    public static void InsertOracle(String args) throws SQLException, IOException {

//        for (int k = 0; k < 100; k++) {
//
//        }

        for (int i = 0; i < 10; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO test_tzh2 VALUES(");
//            long timestamp = System.currentTimeMillis();
//            String ts = String.valueOf(df.format(timestamp));
            List<String> line = Arrays.asList(String.valueOf(i), "'Rick"+String.valueOf(i)+"'");
            String names = String.join(",", line);
            sb.append(names);
            sb.append(")");
            System.out.println(sb);
        }
    }};


//    public static void setBatchinsert(String[] args) throws SQLException, IOException {
//        StringBuilder sb = new StringBuilder();
//        sb.append("BATCHINSERT INTO user_info BATCHVALUES(");
//        for (int i = 0; i < loop; i++) {
//            long timestamp = System.currentTimeMillis();
//            List<String> line = Arrays.asList(String.valueOf(i), "Rick", String.valueOf(timestamp));
//            String names = String.join(",", line);
//            sb.append("VALUES(").append(names).append(")");
//
//        }
//        sb.append(")");
//        System.out.println(sb);
//
//    }}
