//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.pool2.BasePooledObjectFactory;
//import org.apache.commons.pool2.PooledObject;
//import org.apache.commons.pool2.impl.DefaultPooledObject;
//import org.apache.commons.pool2.impl.GenericObjectPool;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.FileStatus;
//import org.apache.hadoop.fs.FileSystem;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.security.UserGroupInformation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Slf4j
//@Component
//public class FileSystemConnection {
//    private static final GenericObjectPool<FileSystem> hdfsConnectionPool;
//    private static MyKerberosConfig myKerberosConfig;
//
//    static {
//        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
//        poolConfig.setMaxTotal(10);
//        poolConfig.setMinIdle(5);
//        poolConfig.setMaxIdle(-1);
//        poolConfig.setBlockWhenExhausted(false);
//        poolConfig.setTestOnBorrow(true);
//        poolConfig.setTestOnReturn(false);
//        poolConfig.setTestOnCreate(false);
//
//        hdfsConnectionPool = new GenericObjectPool<>(new BasePooledObjectFactory<FileSystem>() {
//
//
//            @Override
//            public FileSystem create() throws Exception {
//                return initConnection();
//            }
//
//            @Override
//            public PooledObject<FileSystem> wrap(FileSystem obj) {
//                return new DefaultPooledObject<>(obj);
//            }
//
//            @Override
//            public boolean validateObject(PooledObject<FileSystem> pooledObject) {
//                try{
//                    UserGroupInformation.getCurrentUser().checkTGTAndReloginFromKeytab();
//                    return true;
//                }catch (IOException e){
//                    log.error("hdfs conn test failed ", e);
//                    return false;
//                }
//            }
//
//            @Override
//            public void destroyObject(PooledObject<FileSystem> pooledObject) throws Exception {
//                pooledObject.getObject().close();
//            }
//        }, poolConfig);
//    }
//
//    public  GenericObjectPool<FileSystem> getPool(){
//        return hdfsConnectionPool;
//    }
//
//    public FileSystem borrowObject() throws Exception {
//        return hdfsConnectionPool.borrowObject();
//    }
//
//    public void returnObject(FileSystem fileSystem) {
//        if (fileSystem != null) {
//            hdfsConnectionPool.returnObject(fileSystem);
//        }
//    }
//
//    @Autowired
//    public void setMyKerberosConfig(MyKerberosConfig myKerberosConfig) {
//        FileSystemConnection.myKerberosConfig = myKerberosConfig;
//    }
//
//    private static FileSystem initConnection() {
//        try {
//            Configuration conf = new Configuration();
//            conf.addResource(new Path("file:///opt/ficczx/core-site.xml"));
//            conf.addResource(new Path("file:///opt/ficczx/hdfs-site.xml"));
//            conf.addResource(new Path("file:///opt/ficczx/yarn-site.xml"));
//
//            UserGroupInformation.setConfiguration(conf);
//            UserGroupInformation.loginUserFromKeytab(myKerberosConfig.getPrincipal(), myKerberosConfig.getKeyTabPath());
//            FileSystem fs = FileSystem.get(conf);
//            return fs;
//        } catch (Exception e) {
//            throw new RuntimeException("get hdfs connection failure", e);
//        }
//    }
//
//
//    public class MyKerberosConfig {
//        private String principal = "xtyf_typt";
//        private String keyTabPath = "file:///opt/ficczx/xtyf_typt.keytab";
//
//        public String getPrincipal() {
//            return principal;
//        }
//
//        public String getKeyTabPath() {
//            return keyTabPath;
//        }
//    }
//}
