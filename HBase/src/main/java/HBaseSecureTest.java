import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;

public class HBaseSecureTest {
    private static Configuration conf = null;

    static {
        Configuration HBASE_CONFIG = new Configuration();
        HBASE_CONFIG.set("hbase.zookeeper.quorum", "linux-4-35");
        HBASE_CONFIG.set("hbase.master.kerberos.principal", "hbase/_HOST@TDH");
        HBASE_CONFIG.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@TDH");
        HBASE_CONFIG.set("hbase.security.authentication", "kerberos");
        HBASE_CONFIG.set("zookeeper.znode.parent", "/hyperbase1");
        HBASE_CONFIG.set("hadoop.security.authentication", "kerberos");

        conf = HBaseConfiguration.create(HBASE_CONFIG);


        // Kerberos Config
        String krb5conf = "/Users/tangmoumou/bigdata_projects/learned-big-data/HBase/krb5.conf";
        System.setProperty("java.security.krb5.conf", krb5conf);
        System.setProperty("java.security.auth.login.config", "/Users/tangmoumou/bigdata_projects/learned-big-data/HBase/jaas.conf");

    }

    public static void main(String[] args) throws Exception {


        UserGroupInformation.setConfiguration(conf);
        try {
            UserGroupInformation.loginUserFromKeytab("hbase/linux-4-35@TDH", "/Users/tangmoumou/java_projects/hyperbase-demo/hyperbase.keytab");
        } catch (IOException e) {
            e.printStackTrace();
        }


        String tablename = "tzhjava";
        String[] familys = {"grade", "course"};

        creatTable(tablename, familys);

        //add record zkb
        addRecord(tablename,"zkb","grade","","5");
        addRecord(tablename,"zkb","course","","90");
        addRecord(tablename,"zkb","course","math","97");
        addRecord(tablename,"zkb","course","art","87");
        //add record  baoniu
        addRecord(tablename,"baoniu","grade","","4");
        addRecord(tablename,"baoniu","course","math","89");

        System.out.println("===========get one record========");
        getOneRecord(tablename, "zkb");

        System.out.println("===========show all record========");
        getAllRecord(tablename);

        System.out.println("===========del one record========");
        delRecord(tablename, "baoniu");
        getAllRecord(tablename);

        System.out.println("===========show all record========");
        getAllRecord(tablename);
    }

    /**
     * Create Table
     */
    public static void creatTable(String tableName, String[] familys) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        if (admin.tableExists(tableName)) {
            System.out.println("table already exists!");
        } else {
            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            for(int i=0; i<familys.length; i++){
                tableDesc.addFamily(new HColumnDescriptor(familys[i]));
            }
            admin.createTable(tableDesc);
            System.out.println("create table " + tableName + " ok.");
        }
    }

    /**
     * Delete Table
     */
    public static void deleteTable(String tableName) throws Exception {
        try {
            HBaseAdmin admin = new HBaseAdmin(conf);
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
            System.out.println("delete table " + tableName + " ok.");
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a row
     */
    public static void addRecord (String tableName, String rowKey, String family, String qualifier, String value)
            throws Exception{
        try {
            HTable table = new HTable(conf, tableName);
            Put put = new Put(Bytes.toBytes(rowKey));
            put.add(Bytes.toBytes(family),Bytes.toBytes(qualifier),Bytes.toBytes(value));
            table.put(put);
            System.out.println("insert recored " + rowKey + " to table " + tableName +" ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a row
     */
    public static void delRecord (String tableName, String rowKey) throws IOException{
        HTable table = new HTable(conf, tableName);
        List list = new ArrayList();
        Delete del = new Delete(rowKey.getBytes());
        list.add(del);
        table.delete(list);
        System.out.println("del recored " + rowKey + " ok.");
    }

    /**
     * Search a row
     */
    public static void getOneRecord (String tableName, String rowKey) throws IOException{
        HTable table = new HTable(conf, tableName);
        Get get = new Get(rowKey.getBytes());
        Result rs = table.get(get);
        for(KeyValue kv : rs.raw()){
            System.out.print(new String(kv.getRow()) + " " );
            System.out.print(new String(kv.getFamily()) + ":" );
            System.out.print(new String(kv.getQualifier()) + " " );
            System.out.print(kv.getTimestamp() + " " );
            System.out.println(new String(kv.getValue()));
        }
    }

    /**
     * Show all records
     */
    public static void getAllRecord (String tableName) {
        try{
            HTable table = new HTable(conf, tableName);
            Scan s = new Scan();
            ResultScanner ss = table.getScanner(s);
            for(Result r:ss){
                for(KeyValue kv : r.raw()){
                    System.out.print(new String(kv.getRow()) + " ");
                    System.out.print(new String(kv.getFamily()) + ":");
                    System.out.print(new String(kv.getQualifier()) + " ");
                    System.out.print(kv.getTimestamp() + " ");
                    System.out.println(new String(kv.getValue()));
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}