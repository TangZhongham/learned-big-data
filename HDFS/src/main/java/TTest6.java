//import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Slf4j
//@Component

//  -Dsun.security.krb5.debug=true
// This is the right version。。。
public class TTest6 {
    private static final GenericObjectPool<FileSystem> hdfsConnectionPool;
    private static MyKerberosConfig myKerberosConfig;
    private static Configuration conf;

    static {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(1000);
        poolConfig.setMinIdle(5);
        poolConfig.setMaxIdle(-1);
//        poolConfig.setBlockWhenExhausted(false);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestOnCreate(false);

        hdfsConnectionPool = new GenericObjectPool<>(new BasePooledObjectFactory<FileSystem>() {


            @Override
            public FileSystem create() throws Exception {
                return initConnection();
            }

            @Override
            public PooledObject<FileSystem> wrap(FileSystem obj) {
                return new DefaultPooledObject<>(obj);
            }

            @Override
            public boolean validateObject(PooledObject<FileSystem> pooledObject) {
                try{
                    UserGroupInformation.getCurrentUser().checkTGTAndReloginFromKeytab();
                    return true;
                }catch (IOException e){
//                    log.error("hdfs conn test failed ", e);
                    return false;
                }
            }

            @Override
            public void destroyObject(PooledObject<FileSystem> pooledObject) throws Exception {
                pooledObject.getObject().close();
            }
        }, poolConfig);

        conf = new Configuration();
//            conf.addResource(new Path("file:///opt/ficczx/core-site.xml"));
//            conf.addResource(new Path("file:///opt/ficczx/hdfs-site.xml"));
//            conf.addResource(new Path("file:///opt/ficczx/yarn-site.xml"));


        conf.addResource(new Path("/Users/tangmoumou/java_projects/kerberoslogin/core-site.xml"));
        conf.addResource(new Path("/Users/tangmoumou/java_projects/kerberoslogin/hdfs-site.xml"));

        System.setProperty("java.security.krb5.conf", "/Users/tangmoumou/java_projects/kerberoslogin/krb5.conf");

        UserGroupInformation.setConfiguration(conf);
//            UserGroupInformation.loginUserFromKeytab(myKerberosConfig.getPrincipal(), myKerberosConfig.getKeyTabPath());
        try {
            UserGroupInformation.loginUserFromKeytab("hdfs/linux-4-35@TDH",
                    "/Users/tangmoumou/java_projects/kerberoslogin/hdfs.keytab");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  GenericObjectPool<FileSystem> getPool(){
        return hdfsConnectionPool;
    }

    public static FileSystem borrowObject() throws Exception {
        return hdfsConnectionPool.borrowObject();
    }

    public void returnObject(FileSystem fileSystem) {
        if (fileSystem != null) {
            hdfsConnectionPool.returnObject(fileSystem);
        }
    }

    //    @Autowired
    public void setMyKerberosConfig(MyKerberosConfig myKerberosConfig) {
//        FileSystemConnection.myKerberosConfig = myKerberosConfig;
        myKerberosConfig = myKerberosConfig;
    }

    // 这个地方每一个线程都会执行一次，loginUserFromKeytab 可能问题就出在这里
    private static FileSystem initConnection() {
        try {

            FileSystem fs = FileSystem.get(conf);
            return fs;
        } catch (Exception e) {
            throw new RuntimeException("get hdfs connection failure", e);
        }
    }


    public class MyKerberosConfig {
        private String principal = "xtyf_typt";
        private String keyTabPath = "file:///opt/ficczx/xtyf_typt.keytab";

        public String getPrincipal() {
            return principal;
        }

        public String getKeyTabPath() {
            return keyTabPath;
        }
    }

    public static void main(String[] args) throws Exception {
//        FileSystem fileSystem = borrowObject();
//        Path path = new Path("/");
//        fileSystem.listStatus(path);

        ExecutorService executor = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 10000; i++) {
            executor.submit(new Task("" + i));
        }

        executor.shutdown();

//        GenericObjectPool pool =
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
                FileSystem fileSystem = borrowObject();
                Path path = new Path("/");
                fileSystem.listStatus(path);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("end task " + name);
        }
    }
}
