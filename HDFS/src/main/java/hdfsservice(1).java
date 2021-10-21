//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.orientsec.quant.quantbulkio.hdfs.FileSystemConnection;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.pool2.impl.GenericObjectPool;
//import org.apache.hadoop.fs.FileStatus;
//import org.apache.hadoop.fs.FileSystem;
//import org.apache.hadoop.fs.Path;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//public class HDFSService {
//
//    @Autowired
//    FileSystemConnection fileSystemConnection;
//
//    @GetMapping(path = "/hdfs2local")
//    public void hdfs2Local(String srcPathString, String destPath) throws Exception {
//        FileSystem fileSystem = null;
//        try {
//            fileSystem = fileSystemConnection.borrowObject();
//            Path srcPath = new Path(srcPathString);
//            if (!fileSystem.exists(srcPath)) {
//                throw new RuntimeException("src path is invalid, "+ srcPathString);
//            }
//            long l = System.currentTimeMillis();
//            fileSystem.copyToLocalFile(srcPath, new Path(destPath));
//            log.info("  hdfs2local Spent:" + (System.currentTimeMillis() - l));
//        } catch (Exception e) {
//            log.error("borrow failed", e);
//            throw e;
//        } finally {
//            if (fileSystem != null)
//                fileSystemConnection.returnObject(fileSystem);
//            GenericObjectPool<FileSystem> pool = fileSystemConnection.getPool();
//            log.info("Idle count:{}, in use count:{}, borrowed count:{}, returned count{}", pool.getNumIdle(), pool.getNumActive(),
//                    pool.getBorrowedCount(), pool.getReturnedCount());
//        }
//    }
//
//    @GetMapping(path = "/local2hdfs")
//    public void local2hdfs(String srcPathString, String destPath) throws Exception {
//        FileSystem fileSystem = null;
//        try {
//            fileSystem = fileSystemConnection.borrowObject();
//            long l = System.currentTimeMillis();
//            fileSystem.copyFromLocalFile(new Path(srcPathString), new Path(destPath));
//            log.info("  hdfs2local Spent:" + (System.currentTimeMillis() - l));
//        } catch (Exception e) {
//            log.error("borrow failed", e);
//            throw e;
//        } finally {
//            if (fileSystem != null)
//                fileSystemConnection.returnObject(fileSystem);
//            GenericObjectPool<FileSystem> pool = fileSystemConnection.getPool();
//            log.info("Idle count:{}, in use count:{}, borrowed count:{}, returned count{}", pool.getNumIdle(), pool.getNumActive(),
//                    pool.getBorrowedCount(), pool.getReturnedCount());
//        }
//    }
//
//    @GetMapping(path = "/deleteHdfsFile")
//    public void deleteHdfsFile(String pathString) throws Exception {
//        FileSystem fileSystem = null;
//        try {
//            fileSystem = fileSystemConnection.borrowObject();
//            long l = System.currentTimeMillis();
//            fileSystem.delete(new Path(pathString));
//            log.info("  hdfs2local Spent:" + (System.currentTimeMillis() - l));
//        } catch (Exception e) {
//            log.error("borrow failed", e);
//            throw e;
//        } finally {
//            if (fileSystem != null)
//                fileSystemConnection.returnObject(fileSystem);
//            GenericObjectPool<FileSystem> pool = fileSystemConnection.getPool();
//            log.info("Idle count:{}, in use count:{}, borrowed count:{}, returned count{}", pool.getNumIdle(), pool.getNumActive(),
//                    pool.getBorrowedCount(), pool.getReturnedCount());
//        }
//    }
//
//    @GetMapping(path = "/listHdfsFiles")
//    public String listHdfsFiles(String pathString) throws Exception {
//        FileSystem fileSystem = null;
//        try {
//            fileSystem = fileSystemConnection.borrowObject();
//            long l = System.currentTimeMillis();
//            FileStatus[] fileStatuses = fileSystem.listStatus(new Path(pathString));
//            log.info("  hdfs2local Spent:" + (System.currentTimeMillis() - l));
//            JsonArray value = new JsonArray();
//            for (FileStatus fileStatus : fileStatuses) {
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("path", fileStatus.getPath().toString());
//                jsonObject.addProperty("isDir", fileStatus.isDirectory());
//                jsonObject.addProperty("owner", fileStatus.getOwner());
//                jsonObject.addProperty("group", fileStatus.getGroup());
//                value.add(jsonObject);
//            }
//            String json = value.toString();
//            log.info(json);
//            return value.toString();
//        } catch (Exception e) {
//            log.error("borrow failed", e);
//            throw e;
//        } finally {
//            if (fileSystem != null)
//                fileSystemConnection.returnObject(fileSystem);
//            GenericObjectPool<FileSystem> pool = fileSystemConnection.getPool();
//            log.info("Idle count:{}, in use count:{}, borrowed count:{}, returned count{}", pool.getNumIdle(), pool.getNumActive(),
//                    pool.getBorrowedCount(), pool.getReturnedCount());
//        }
//    }
//
//}