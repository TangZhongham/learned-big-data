import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.Time;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FileSystemConnection4 {

    private static boolean validateObject() {
        try{
            UserGroupInformation.getCurrentUser().checkTGTAndReloginFromKeytab();
            System.out.println("end task " + UserGroupInformation.isLoginKeytabBased());
//            UserGroupInformation.getLoginUser().reloginFromKeytab();

            // 大概理解了，就是说 jdk 里面和 方法里面的冲突了 checkTGTAndReloginFromKeytab()（this.isKeytab 疑似这里），
            // 导致 永远都执行不到 this.reloginFromKeytab(); 我猜的，reloginFromKeytab() 里面也有 this.isKeytab

//            if (shouldAuthenticateOverKrb()) {
//                if (currRetries < maxRetries) {
//                    if (LOG.isDebugEnabled()) {
//                        LOG.debug("Exception encountered while connecting to " + "the server : " + ex);
//                    }
//                    //try re-login
//                    if (UserGroupInformation.isLoginKeytabBased()) {
//                        UserGroupInformation.getLoginUser().reloginFromKeytab();
//                    } else {
//                        UserGroupInformation.getLoginUser().reloginFromTicketCache();
//                    }

            //try re-login
//            if (UserGroupInformation.isLoginKeytabBased()) {
//                UserGroupInformation.getLoginUser().reloginFromKeytab();
//            } else {
//                UserGroupInformation.getLoginUser().reloginFromTicketCache();
//            }

//            if (UserGroupInformation.getCurrentUser().getCredentials() == null
////                    || shouldRenewImmediatelyForTests
//                    || Time.now() >= UserGroupInformation.getRefreshTime(tgt)) {
//                this.reloginFromKeytab();
//            }



            // 明天参考 org.apache.hadoop.ipc.Client.Connection.handleSaslConnectionFailure

            // 知乎
//            UserGroupInformation.getLoginUser().checkTGTAndReloginFromKeytab();

            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }


    private static FileSystem initConnection() {
        try {
            Configuration conf = new Configuration();
//            conf.addResource(new Path("file:///opt/ficczx/core-site.xml"));
//            conf.addResource(new Path("file:///opt/ficczx/hdfs-site.xml"));
//            conf.addResource(new Path("file:///opt/ficczx/yarn-site.xml"));

            conf.addResource(new Path("/Users/tangmoumou/java_projects/kerberoslogin/core-site.xml"));
            conf.addResource(new Path("/Users/tangmoumou/java_projects/kerberoslogin/hdfs-site.xml"));
//            conf.addResource(new Path("file:///opt/ficczx/yarn-site.xml"));

            // 本机没有 /etc/krb5.conf
//            private static final String krb5conf = "/Users/tangmoumou/java_projects/kerberoslogin/krb5.conf";
//            conf.addResource(new Path("/Users/tangmoumou/java_projects/kerberoslogin/krb5.conf"));
            String krb5conf = "/Users/tangmoumou/java_projects/kerberoslogin/krb5.conf";
            System.setProperty("java.security.krb5.conf", krb5conf);

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
    // kadmin -q "modprinc -maxlife 1minutes hdfs/linux-4-35@TDH" -s linux-4-35

    public static void main(String[] args) {
        FileSystem fileSystem = initConnection();
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10000; i++) {
            executor.submit(new Task("" + i));
        }

        executor.shutdown();
    }


    static class Task implements Runnable {
        private final String name;

        public Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("start task " + name);
            boolean result = validateObject();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("True 表示 checkTGTAndReloginFromKeytab：  " + result);
            System.out.println("end task " + name);
        }
    }
}
