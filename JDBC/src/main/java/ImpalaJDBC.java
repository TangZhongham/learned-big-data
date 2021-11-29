import java.io.File;
        import java.io.FileReader;
        import java.io.IOException;
        import java.sql.*;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;
        import java.util.Properties;

public class ImpalaJDBC {

    //    public static Connection conn;
    public static File file;
    public static ResultSet rs;
    public static Statement stmt;
    public static Connection conn;
    public static int factor = 5;
    public static int loop = 100;

    public static int counter = 0;
    public static int counter2 = 0;

    //    public static String insertSQL = "INSERT INTO LD.test_tzh2 VALUES(?,?,?,?)";
    // kudu 必需加库名
    public static String insertSQL = "INSERT INTO default.test_tzh2 VALUES(?,?)";
    public static String deleteSQL = "DELETE FROM default.test_tzh2 WHERE id = ?";
    public static String updateSQL = "UPDATE default.test_tzh2 SET name=? WHERE id = ?";

    public static String database_config = "";

    // Argo compare result
    public static String compareSQL = "select count(*) from test_tzh2";

    // seed
    public static ArrayList<Integer> seed_list = new ArrayList<Integer>() {
        {
            // 进行105次 insert、 110-105=5次 delete、5 次 update
            add(115);
            add(105);
            add(110);
        }
    };

    public static long start_time;
    public static long end_time;
    public static long start_count;
    public static long end_count;

    // 总时间
    public static long insert_time;
    public static long delete_time;
    public static long update_time;


    // 总记录数
    public static int total_num = 10000;
    public static boolean is_argo = false;

    // 1 表示开启
    public static boolean delete_ = true;
    public static boolean update_ = true;

    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        start_time = System.currentTimeMillis();

        // 支持插入 Oracle/ArgoDB
//        String database_config = "setting_oracle.properties";
        // 可用来测 同样情况argo 执行完的性能（10000条 oracle 需要60s）
        // 测argo 要把前面 trunacte 去掉
        // 发现argo insert 慢到难以接受。 10000条 argodb 449s
//        String database_config = "setting.properties";
        if(is_argo) {
            database_config = "setting.properties";
        } else {
//            database_config = "setting_oracle.properties";
            database_config = "setting_impala.properties";
        }


        if (args.length > 0) {
            file = new File(args[0]);
        } else {
            file = new File("./JDBC/conf/" + database_config);
            System.out.println("use default config in ./conf/" + database_config);
        }
        Properties props = new Properties();

        // if file does not exist:FileNotFoundException
        if (file.exists()) {
            props.load(new FileReader(file));
        } else {
            System.out.println(database_config + " NOT FOUND");
            System.exit(1);
        }

        try {
            conn = Utils.getConnection(props);
            // connect
            stmt = conn.createStatement();
            // 1. delete old data
            if (!is_argo){
//                stmt.executeQuery("show tables");
//                conn.createStatement().execute("create table if not exists test_tzh2(id int, name string,primary key(id)) stored as kudu");
            }

            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
            PreparedStatement deleteStatement = conn.prepareStatement(deleteSQL);
            PreparedStatement updateStatement = conn.prepareStatement(updateSQL);

            while (counter < total_num && !is_argo) {

                for (int i = 0; i < seed_list.get(0); i++) {
                    if(i < seed_list.get(1)) {
                        long _start_time = System.currentTimeMillis();
                        conn.createStatement().execute(String.format("insert into default.test_tzh2 values(%s,'Rick')", counter));
//                        preparedStatement.setInt(0, counter);
//                        preparedStatement.setString(2, "Rick");
//                        preparedStatement.setString(1, "Rick"+ counter);
//                    preparedStatement.setString(3, "Buck"+ counter);
//                    preparedStatement.setString(4, "Sick"+ counter);
//                        preparedStatement.execute();
                        insert_time = insert_time + (System.currentTimeMillis() - _start_time);
                        System.out.println("Rick"+ counter);
                        counter++;
                    } else if (i < seed_list.get(2) && delete_==true) {
                        // delete
                        // DELETE FROM test_tzh2 WHERE id = 2;
                        long _start_time2 = System.currentTimeMillis();
                        conn.createStatement().execute(String.format("DELETE FROM default.test_tzh2 WHERE id=%s", counter-5));
//                        deleteStatement.setInt(1,  counter-5);
//                        deleteStatement.execute();
                        delete_time = delete_time + (System.currentTimeMillis() - _start_time2);
                        System.out.println("DELETE "+"Rick"+ counter);
                        counter--;
                    } else if (update_==true){
                        // update
                        // UPDATE test_tzh2 SET name='Morty' WHERE id = 2;
                        long _start_time3 = System.currentTimeMillis();
                        conn.createStatement().execute(String.format("UPDATE default.test_tzh2 SET name='%s' where id=%s", "Rrrr", counter-20));
//                        updateStatement.setString(1, "Morty"+(String.valueOf(counter-20)));
//                        updateStatement.setInt(2, counter-20);
//                        updateStatement.execute();
                        update_time = update_time + (System.currentTimeMillis() - _start_time3);
                        System.out.println("UPDATE "+"Rick"+ (counter - 20));
                    }
                }}

            // ArgoDB batchinsert
            if (is_argo){

                for (int i = 0; i < total_num/100; i++) {
                    long _start_time = System.currentTimeMillis();
                    conn.createStatement().execute(String.format("insert into default.test_tzh2 values(%s,'Rick')", counter));

//                    String batch_sql = get_Batch_String("test");
//                    stmt.execute(batch_sql);
                    insert_time = insert_time + (System.currentTimeMillis() - _start_time);
                    counter = counter + 100;
                    System.out.println("已插入 " + counter + "条");
                    for (int j = 0; j < 5 ; j++) {
                        if (delete_) {
                            long _start_time2 = System.currentTimeMillis();
                            deleteStatement.setInt(1, counter - 5 - j);
                            deleteStatement.execute();
                            delete_time = delete_time + (System.currentTimeMillis() - _start_time2);
                            System.out.println("DELETE " + "Rick" + (counter - 5 - j));
                        }
                        if (update_) {
                            long _start_time3 = System.currentTimeMillis();
                            updateStatement.setString(1, "Morty" + (String.valueOf(counter - 20 - j)));
                            updateStatement.setInt(2, counter - 20 - j);
                            updateStatement.execute();
                            update_time = update_time + (System.currentTimeMillis() - _start_time3);
                            System.out.println("UPDATE " + "Rick" + (counter - 20 - j));
                        }
                    }
                }
            }
            // 不计时
//            if (is_argo){
//
//                for (int i = 0; i < total_num/100; i++) {
////                    long _start_time = System.currentTimeMillis();
//                    String batch_sql = get_Batch_String("test");
//                    stmt.execute(batch_sql);
////                    insert_time = insert_time + (System.currentTimeMillis() - _start_time);
//                    counter = counter + 100;
//                    System.out.println("已插入 " + counter + "条");
//                    for (int j = 0; j < 5 ; j++) {
////                        long _start_time2 = System.currentTimeMillis();
//                        deleteStatement.setInt(1,  counter-5-j);
//                        deleteStatement.execute();
////                        delete_time = delete_time + (System.currentTimeMillis() - _start_time2);
//                        System.out.println("DELETE "+"Rick"+ (counter-5-j));
////                        long _start_time3 = System.currentTimeMillis();
//                        updateStatement.setString(1, "Morty"+(String.valueOf(counter-20-j)));
//                        updateStatement.setInt(2, counter-20-j);
//                        updateStatement.execute();
////                        update_time = update_time + (System.currentTimeMillis() - _start_time3);
//                        System.out.println("UPDATE "+"Rick"+ (counter - 20-j));
//                    }
//                }
//            }

        }
        catch (SQLException ex) {
            System.err.println(new java.util.Date()+" : "+ex.getMessage());
            ex.printStackTrace();
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            stmt.close();
            conn.close();
            end_time = System.currentTimeMillis();
            System.out.println(counter + "条数据总耗时：" + (end_time-start_time) + " ms...");
            System.out.println("插入"+ counter + "次\n" + "修改5次\ndelete5次");
            System.out.println("插入总时间："+ insert_time + "\n" + "修改总时间：" + update_time + "\n" + "删除总时间：" +delete_time
                    + "\n");


            // check result
            if (!is_argo){
                start_count = System.currentTimeMillis();
                Statement statement = getArgoConnection();

                int count = 0;

                while (count != total_num) {
                    ResultSet resultSet = statement.executeQuery(compareSQL);
                    if (resultSet.next()) {
                        count = resultSet.getInt(1);
                    }
                    if (count == total_num/2){
                        System.out.println("消费一半耗时" + (System.currentTimeMillis()-start_count) + " ms...");
                    }
                    Thread.sleep(1000);
                }
                System.out.println(count);
                end_count = System.currentTimeMillis();
                System.out.println("同步耗时" + (end_count-start_count) + " ms...");
                statement.close();}
        }
    }


    public static void InsertOracle(String args) throws SQLException, IOException {
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
    }

    public static Statement getArgoConnection() throws SQLException, IOException {
        file = new File("./JDBC/conf/setting.properties");
        Properties props = new Properties();
        props.load(new FileReader(file));
        conn = Utils.getConnection(props);
        // connect
        return conn.createStatement();
    }

    // ArgoDB batchinsert
    public static String get_Batch_String(String args) throws SQLException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("BATCHINSERT INTO ld.test_tzh2 BATCHVALUES(");
        for (int i = 0; i < loop; i++) {
            long timestamp = System.currentTimeMillis();
//            List<String> line = Arrays.asList(String.valueOf(i), "'Rick'", String.valueOf(timestamp));
            List<String> line = Arrays.asList(String.valueOf(i+counter), "'Rick'");
            String names = String.join(",", line);
            sb.append("VALUES(").append(names).append(")");
            if(i != loop -1) {
                sb.append(",");
            }
        }
//        sb.deleteCharAt(sb.length()-1);
        sb.append(")");
        System.out.println(sb);
        return sb.toString();
    }
}

