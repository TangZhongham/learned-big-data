package datagenerator.transaction;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.security.PrivilegedAction;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.security.UserGroupInformation;

public class HDFS_Out {
    FileSystem fs = null;

    FSDataOutputStream out = null;

    public HDFS_Out(String path) throws IOException {

        Properties props = new Properties();

        String filePath = System.getProperty("user.dir") +File.separator+"conf"+File.separator+"setting.properties";
        System.out.println("使用conf 地址：" + filePath);
        File file2 = new File(filePath);
//        File file2 = new File("./conf/setting.properties");
        props.load(new FileReader(file2));
        String principal = props.getProperty("principal");
        String keytab = props.getProperty("keytab");
        String krb5 = props.getProperty("krb5");
        String uri = props.getProperty("uri");

        Configuration conf = new Configuration();

//        conf.addResource(new Path( System.getProperty("user.dir") +File.separator+"conf"+File.separator+"core-site.xml"));
//        System.out.println("使用core site 地址：" + new Path( System.getProperty("user.dir") +File.separator+"conf"+File.separator+"core-site.xml"));
//        conf.addResource(new Path( System.getProperty("user.dir") +File.separator+"conf"+File.separator+"hdfs-site.xml"));

//        conf.set("dfs.namenode.kerberos.principal","hdfs/tw-node1@SGIDCTDH");
        conf.set("dfs.namenode.kerberos.principal",principal);
        conf.set("hadoop.security.authentication", "kerberos");
        conf.set("hadoop.security.authorization", "true");
        System.setProperty("java.security.krb5.conf", krb5);

        conf.set("fs.hdfs.impl",
                org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()
        );
        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(principal
                ,keytab);


        Path path1 = new Path(path);

        this.fs = path1.getFileSystem(conf);
//        this.out = this.fs.create(path1);
//        this.out = this.fs.create(new Path("/tmp/tzh"));

        FileSystem fs =  ugi.doAs(new PrivilegedAction<FileSystem>() {
            @Override
            public FileSystem run() {
                try {
                    return FileSystem.get(new URI(uri), conf);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        this.out = fs.create(path1);

    }

    public void writeToHdfs(StringBuffer sb) throws IOException {
        if (this.out != null)
            this.out.write(sb.toString().getBytes());
    }

    public void close() throws IOException {
        if (this.fs != null)
            this.fs.close();
        if (this.out != null)
            this.out.close();
    }

    public static void main(String[] args) throws IOException {

        Properties props = new Properties();
        File file = new File("./conf/setting.properties");
        props.load(new FileReader(file));
        String principal = props.getProperty("principal");
        String keytab = props.getProperty("keytab");
        String krb5 = props.getProperty("krb5");

        Configuration conf = new Configuration();

//        conf.set("dfs.namenode.kerberos.principal","hdfs/tw-node1@SGIDCTDH");
        conf.set("dfs.namenode.kerberos.principal",principal);
        conf.set("hadoop.security.authentication", "kerberos");
        conf.set("hadoop.security.authorization", "true");
        conf.set("hadoop.security.group.mapping.ldap.bind.password", "true");
        System.setProperty("java.security.krb5.conf", krb5);

        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(principal
                ,keytab);


//        Configuration conf = new Configuration();
//
//        conf.set("dfs.namenode.kerberos.principal","hdfs/tw-node1@SGIDCTDH");
//        conf.set("hadoop.security.authentication", "kerberos");
//        conf.set("hadoop.security.authorization", "true");
//        System.setProperty("java.security.krb5.conf", "./conf/krb5.conf");
//
//        UserGroupInformation.setConfiguration(conf);
//        UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI("hdfs/tw-node1@SGIDCTDH"
//                ,"./conf/hdfs3.keytab");

        //通过上面的配置得到hdfs连接
        FileSystem fs =  ugi.doAs(new PrivilegedAction<FileSystem>() {
            @Override
            public FileSystem run() {
                try {
                    return FileSystem.get(new URI("hdfs://tw-node2"), conf);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        //统计根目录下文件总大小的信息
        Path rootPath = new Path("/");
        FileStatus[] fileStatuses = fs.listStatus(rootPath);
        for (FileStatus fileStatus : fileStatuses) {
            Path filePath = fileStatus.getPath();
            System.out.println(
                    filePath
                            + ", size : "
                            + fs.getContentSummary(filePath).getLength()
                            + ", size included backup:"
                            + fs.getContentSummary(filePath).getSpaceConsumed());
        }










////        HDFS_Out OUT = new HDFS_Out("/Users/tangmoumou/bigdata_projects/learned-big-data/Java/yet-another-data-generator/conf/core-site.xml");
////        Path p = new Path("/Users/tangmoumou/bigdata_projects/learned-big-data/Java/yet-another-data-generator/conf/core-site.xml");
////        OUT.fs.copyFromLocalFile(new Path("/Users/tangmoumou/bigdata_projects/learned-big-data/Java/yet-another-data-generator/conf/core-site.xml"), p);
////        OUT.close();
//        final Configuration conf = new Configuration();
//
//        conf.addResource(new Path("/Users/tangmoumou/bigdata_projects/learned-big-data/Java/yet-another-data-generator/conf/core-site.xml"));
//        conf.addResource(new Path("/Users/tangmoumou/bigdata_projects/learned-big-data/Java/yet-another-data-generator/conf/hdfs-site.xml"));
//
//        System.setProperty("java.security.krb5.conf", "/Users/tangmoumou/bigdata_projects/learned-big-data/Java/yet-another-data-generator/conf/krb5.conf");
//
////        conf.set("dfs.namenode.kerberos.principal","hive@TDH");
////        conf.set("hadoop.security.authentication", "kerberos");
////        conf.set("hadoop.security.authorization", "true");
//        UserGroupInformation.setConfiguration(conf);
//        UserGroupInformation.loginUserFromKeytab("hdfs/tdh01@TDH",
//                "/Users/tangmoumou/bigdata_projects/learned-big-data/Java/yet-another-data-generator/conf/hdfs.keytab");
//
//        UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI("hdfs/tdh01@TDH",
//                "/Users/tangmoumou/bigdata_projects/learned-big-data/Java/yet-another-data-generator/conf/hdfs.keytab");
//
//        System.out.println(ugi.getAuthenticationMethod().toString());
//
//
//        FileSystem fs = FileSystem.get(conf);
//        FileStatus[] files = fs.listStatus(new Path("/"));
//        for (FileStatus file : files) {
//            System.out.println(file.getPath());
//        }
//
//
//        //通过上面的配置得到hdfs连接
////        FileSystem fs =  ugi.doAs(new PrivilegedAction<FileSystem>() {
////            @Override
////            public FileSystem run() {
////                try {
////                    return FileSystem.get(new URI("hdfs://tdh02"), conf);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////                return null;
////            }
////        });
//
//        System.out.println(fs.listStatus(new Path("/")));
//
//        // 华为推荐
////        UserGroupInformation.getLoginUser().reloginFromKeytab();
//
//        // 客户实际使用
////        UserGroupInformation.getCurrentUser().checkTGTAndReloginFromKeytab();
//
////        RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path("/"), true);
////        while(files.hasNext()) {
////            LocatedFileStatus file = files.next();
////
////            System.out.println((fs.open(file.getPath())));
////        }
//
//
////统计根目录下文件总大小的信息
//        Path rootPath = new Path("/");
//        FileStatus[] fileStatuses = fs.listStatus(rootPath);
//        for (FileStatus fileStatus : fileStatuses) {
//            Path filePath = fileStatus.getPath();
//            System.out.println(
//                    filePath
//                            + ", size : "
//                            + fs.getContentSummary(filePath).getLength()
//                            + ", size included backup:"
//                            + fs.getContentSummary(filePath).getSpaceConsumed());
//        }
//    }
}
}

