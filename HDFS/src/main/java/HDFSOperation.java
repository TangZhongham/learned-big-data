import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;
import java.io.*;
import java.net.URI;

public class HDFSOperation {
    private static final String hdfsSite = "/Users/tangmoumou/java_projects/kerberoslogin/hdfs-site.xml";
    private static final String coreSite = "/Users/tangmoumou/java_projects/kerberoslogin/core-site.xml";
    private static final String krb5conf = "/Users/tangmoumou/java_projects/kerberoslogin/krb5.conf";
    private static final String fshdfs = "org.apache.hadoop.hdfs.DistributedFileSystem";
    //    private static String user = "hdfs/linux-4-35@TDH";
    private static String keytab = "/Users/tangmoumou/java_projects/kerberoslogin/hdfs.keytab";

    public static Configuration getConf(String user) throws IOException {
        Configuration conf = new Configuration();
        conf.addResource(new Path(hdfsSite));
        conf.addResource(new Path(coreSite));
        conf.set("fs.hdfs.impl",fshdfs);
        conf.setBoolean("dfs.support.append", true);
        System.setProperty("java.security.krb5.conf", krb5conf);
        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab(user, keytab);
        return conf;
    }

    /**
     * list directory
     * @param args
     * @throws IOException
     */
    public static void listDir(String[] args) throws IOException {
        if(args.length < 2) {
            System.out.println("Listdir directory method requires dirname!");
            System.exit(1);
        }
        String dst = args[1];
        String user = args[2];
        FileSystem fs = FileSystem.get(getConf(user));
        FileStatus[] files = fs.listStatus(new Path(dst));
        for (FileStatus file : files) {
            System.out.println(file.getPath());
        }
    }

    public static void listDir(String dst, String user) throws IOException {
        FileSystem fs = FileSystem.get(getConf(user));
        FileStatus[] files = fs.listStatus(new Path(dst));
        for (FileStatus file : files) {
            System.out.println(file.getPath());
        }
    }

    public static void main(String[] args) throws IOException {
        String user = "hdfs/linux-4-35@TDH";
        HDFSOperation.listDir("/", user);
    }

    public static void readFile(Configuration conf, String file) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(file),conf);
        FSDataInputStream hdfsIS = fs.open(new Path(file));

        byte[] ioBuffer = new byte[1024];
        int readLen = hdfsIS.read(ioBuffer);
        while (readLen != -1) {
            System.out.write(ioBuffer, 0, readLen);
            readLen = hdfsIS.read(ioBuffer);
        }
        hdfsIS.close();
        fs.close();
    }


    public static void writeFile(Configuration conf, String file, String str) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(file), conf);
        FSDataOutputStream hdfsOS = null;
//        System.out.println("路径是否存在:" + fs.exists(new Path(file)));
        if (!fs.exists(new Path(file))){
            hdfsOS = fs.create(new Path(file));
        }else {
            hdfsOS = fs.append(new Path(file));
        }
//        hdfsOS.writeChars(URLDecoder.decode(str, "UTF-8"));
        hdfsOS.write(str.getBytes(), 0, str.getBytes().length);
        hdfsOS.close();
        fs.close();
    }

    /**
     * mkdir directory
     * @param args
     * @throws IOException
     */
    public static void mkdirDir(String[] args) throws IOException {
        if(args.length<2){
            System.out.println("Mkdir method requires dirname!");
            System.exit(1);
        }
        String dst = args[1];
        String user = args[2];
        FileSystem fs = FileSystem.get(URI.create(dst), getConf(user));
        Path path = new Path(dst);
        if(fs.exists(path)) {
            throw new IOException("The directory has existed in cureent environment!");
        }
        fs.mkdirs(path);
    }

    /**
     * Delete file
     * @param args
     * @throws Exception
     */
    public static void deleteFile(String[] args) throws Exception {
        if(args.length<2){
            System.out.println("Delete file method requires file name!");
            System.exit(1);
        }
        String fileName = args[1];
        String user = args[2];
        FileSystem fs = FileSystem.get(URI.create(fileName), getConf(user));
        Path path = new Path(fileName);
        if(!fs.exists(path)) {
            throw new IOException("The directory doesn't exist in cureent environment!");
        }
        boolean isDeleted = fs.delete(path, false);
        if(isDeleted) {
            System.out.println("delete file success");
        }else{
            System.out.println("delete file fail");
            System.exit(1);
        }
    }

    /**
     * upload local file to hdfs directory
     * @param args
     * @throws IOException
     */
    public static void upload(String args[]) throws IOException {
        String srcFilePath = args[1];
        String dstFilePath = args[2];
        String user = args[3];
        if(args.length < 4) {
            System.out.println("Upload file methon requires two params!");
            System.exit(1);
        }
        FileSystem hdfs = FileSystem.get(getConf(user));
        Path src = new Path(srcFilePath);
        Path dst = new Path(dstFilePath);
        if(!hdfs.exists(dst)) {
            throw new IOException("The file doesn't exist in cureent environment!");
        }
        hdfs.copyFromLocalFile(src, dst);
        System.out.println("Upload file ["+ srcFilePath + "] file to [" + dstFilePath +"] successfully!");
    }

}
