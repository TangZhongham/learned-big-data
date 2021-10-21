import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.net.URI;
import java.security.PrivilegedAction;

public class Demo {
    public static void main(String[] args) throws IOException {
        final Configuration conf = new Configuration();
        conf.set("dfs.namenode.kerberos.principal","hdfs/tw-node596@TDH");
        conf.set("hadoop.security.authentication", "kerberos");
        conf.set("hadoop.security.authorization", "true");
        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI("hdfs/tw-node596@TDH",
                "/home/xuguo/IdeaProjects/Aquila/hdfs.keytab");

        //通过上面的配置得到hdfs连接
        FileSystem fs =  ugi.doAs(new PrivilegedAction<FileSystem>() {
            @Override
            public FileSystem run() {
                try {
                    return FileSystem.get(new URI("hdfs://172.26.5.96"), conf);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        // 华为推荐
//        UserGroupInformation.getLoginUser().reloginFromKeytab();

        // 客户实际使用
//        UserGroupInformation.getCurrentUser().checkTGTAndReloginFromKeytab();

        RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path("/"), true);
        while(files.hasNext()) {
            LocatedFileStatus file = files.next();

            System.out.println((fs.open(file.getPath())));
        }


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
    }
}
