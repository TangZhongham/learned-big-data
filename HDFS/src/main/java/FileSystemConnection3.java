import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;


public class FileSystemConnection3 {

//    static {
//
//        private static boolean validateObject() {
//            try{
//                UserGroupInformation.getCurrentUser().checkTGTAndReloginFromKeytab();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//    }

    private static boolean validateObject() {
        try{
            UserGroupInformation.getCurrentUser().checkTGTAndReloginFromKeytab();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }


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
//    public void setMyKerberosConfig(MyKerberosConfig myKerberosConfig) {
//        FileSystemConnection.myKerberosConfig = myKerberosConfig;
//    }

    private static FileSystem initConnection() {
        try {
            Configuration conf = new Configuration();
//            conf.addResource(new Path("file:///opt/ficczx/core-site.xml"));
//            conf.addResource(new Path("file:///opt/ficczx/hdfs-site.xml"));
//            conf.addResource(new Path("file:///opt/ficczx/yarn-site.xml"));

            conf.addResource(new Path("/Users/tangmoumou/java_projects/kerberoslogin/core-site.xml"));
            conf.addResource(new Path("/Users/tangmoumou/java_projects/kerberoslogin/hdfs-site.xml"));
//            conf.addResource(new Path("file:///opt/ficczx/yarn-site.xml"));

            UserGroupInformation.setConfiguration(conf);
//            UserGroupInformation.loginUserFromKeytab(myKerberosConfig.getPrincipal(), myKerberosConfig.getKeyTabPath());
            UserGroupInformation.loginUserFromKeytab("hdfs/linux-4-35@TDH",
                    "/Users/tangmoumou/java_projects/kerberoslogin/hdfs.keytab");
            FileSystem fs = FileSystem.get(conf);
            return fs;
        } catch (Exception e) {
            throw new RuntimeException("get hdfs connection failure", e);
        }
    }




//    public static class MyKerberosConfig {
//        private String principal = "hdfs/linux-4-35@TDH";
//        private String keyTabPath = "/Users/tangmoumou/java_projects/kerberoslogin/hdfs.keytab";
//
//        public String getPrincipal() {
//            return principal;
//        }
//
//        public String getKeyTabPath() {
//            return keyTabPath;
//        }
//    }
}
