import java.io.*;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException, InterruptedException, IOException {

//        String f = "setting.properties";

//        URL url = new URL("./setting.properties");
//        InputStream inStream = url.openStream();
//        Properties props = new Properties();
////        props.load(new java.io.FileInputStream(f));
//        props.load(inStream);

//        File file = new File("/Users/tangmoumou/java_projects/fasty/src/main/resources/setting.properties");
//        File file = new File("./conf/setting.properties");
//        try {


        File file;
        if (args.length > 0) {
             file = new File(args[0]);
        } else {
             file = new File("./conf/setting.properties");
            System.out.println("未指定配置文件使用默认 ./conf/setting.properties");
        }
        Properties props = new OrderedProperties();

// 如果文件不存在会抛出异常:FileNotFoundException
        if (file.exists()) {
            props.load(new FileReader(file));
        } else {
            System.out.println("未找到配置文件 setting.properties");
            System.exit(1);
        }


        String table_name = props.getProperty("table_name");
        int total_num = Integer.parseInt(props.getProperty("total_num"));
        int thread_num = Integer.parseInt(props.getProperty("thread_num"));
        String delete_merge_file = props.getProperty("delete_merge_file");
        String delimiter = props.getProperty("delimiter");
        int buffer_io = Integer.parseInt(props.getProperty("buffer_io"));

//        String column_list = props.getProperty("column_list", "120");

        ArrayList<String> column_list = new ArrayList<>();

        Set<Object> keys = props.keySet();//返回属性key的集合
        for (Object key : keys) {
            if (key.toString().contains("col")) {
//            System.out.println(key.toString() + "=!" + props.get(key));
                column_list.add((String) props.get(key));
            }}

        System.out.println(String.format("正在生成表：%s 的数据", table_name));

//        int totals = 100000000;
        int totals = total_num;
        int segment = thread_num ;
        // 写入5亿条数据
        // 开启20个线程
        ExecutorService service = Executors.newFixedThreadPool(segment);
        AtomicInteger incr = new AtomicInteger(0);
        CountDownLatch downLatch = new CountDownLatch(segment);
        long s = System.currentTimeMillis();



        for(int tzh=0;tzh<segment;tzh++) {
            service.submit(() -> {
                RandomAccessFile acf;
                FileChannel fc = null ;
                try {
                    String fName = table_name + incr.getAndIncrement()+".txt";
                    acf = new RandomAccessFile(fName, "rw");
                    fc = acf.getChannel();
                    int offset = 0;

                    StringJoiner stringJoiner = new StringJoiner("" , "", "\n");
                    for (int i = 0; i < totals/segment/buffer_io; i++) { //25000000


                        try {

                        StringBuilder sb = new StringBuilder();

//                        StringJoiner stringJoiner = new StringJoiner("" , "", "\n");

                        ExecutorService executor = Executors.newFixedThreadPool(2);
                        Future submit = executor.submit(new AsyncDataGenerator(column_list,delimiter, buffer_io));

//                        for (int k=0;k<buffer_io;k++) {
//                            // 改成 异步执行？
//
//
//                            StringJoiner StringJoiner = (java.util.StringJoiner) submit.get();
////                            StringJoiner StringJoiner = AsyncDataGenerator.get_all(column_list,delimiter);
//                            sb.append(StringJoiner);
//                        }
                        StringJoiner StringJoiner = (java.util.StringJoiner) submit.get();

                        stringJoiner.merge(StringJoiner);}

                        catch (Exception e) {
                                e.printStackTrace();
                            }finally {
                                downLatch.countDown();
                                try {
                                    fc.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        byte[] bs = stringJoiner.toString().getBytes();

                        MappedByteBuffer mbuf = fc.map(FileChannel.MapMode.READ_WRITE, offset, bs.length);
                        mbuf.put(bs);

                        offset = offset + bs.length;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    downLatch.countDown();
                    try {
                        fc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            };


//
//        for(int j=0;j<segment;j++) {
//            service.execute(()->{
//                RandomAccessFile acf;
//                FileChannel fc = null ;
//                try {
//                    String fName = table_name + incr.getAndIncrement()+".txt";
//                    acf = new RandomAccessFile(fName, "rw");
//                    fc = acf.getChannel();
//                    int offset = 0;
////                    for (int i = 0; i < totals/segment/10000; i++) { //25000000
//                    // 1w 条太少了，尝试 10w 条
////                    for (int i = 0; i < totals/segment/50; i++) { //25000000
//                    for (int i = 0; i < totals/segment/buffer_io; i++) { //25000000
//                        //每次写1w个 数字
//
//                        StringBuilder sb = new StringBuilder();
////                        for (int k=0;k<10000;k++) {
////                            sb.append(new Random().nextInt(10000000) + "\n");
////                        }
////                        byte[] bs = sb.toString().getBytes();
////
////                        MappedByteBuffer mbuf = fc.map(FileChannel.MapMode.READ_WRITE, offset, bs.length);
////                        mbuf.put(bs);
//
////                        DataGenerator2 x = new DataGenerator2("gege");
//                        // 单条数据循环少点
////                        for (int k=0;k<50;k++) {
//                        for (int k=0;k<buffer_io;k++) {
////                            DataGenerator2 x = new DataGenerator2("gege");
////                            StringJoiner sj = new StringJoiner(",", "", "\n");
////                            sj.add(x.get_Integer(8));
////                            sj.add(x.get_Integer(8));
////                            sj.add(x.get_Quote());
////                            sj.add(x.getRandomALLChar(8));
////                            sb.append(DataGenerator2.get_Integer(8))
////                                    .append(',')
////                                    .append(DataGenerator2.get_Integer(8))
////                                    .append(',')
////                                    .append(DataGenerator2.get_Quote())
////                                    .append(',')
////                                    .append(DataGenerator2.getRandomALLChar(8))
////                                    .append("\n");
//
//                            // 改成 异步执行？
//                            StringJoiner StringJoiner = DataGenerator.get_all(column_list,delimiter);
//                            sb.append(StringJoiner);
////                            sb.append("\n");
////                            System.out.println("循环第多少次了：" + k);
//                        }
//                        byte[] bs = sb.toString().getBytes();
//
//                        MappedByteBuffer mbuf = fc.map(FileChannel.MapMode.READ_WRITE, offset, bs.length);
//                        mbuf.put(bs);
//
//                        offset = offset + bs.length;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }finally {
//                    downLatch.countDown();
//                    try {
//                        fc.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
        downLatch.await();
        System.out.println("await 唤醒， 小文件写入完毕! 耗时：" + (System.currentTimeMillis()-s));
        List<File> files = new ArrayList<File>();
        for(int i=0;i<segment;i++) {
            files.add(new File(table_name + i+".txt"));
        }
        s = System.currentTimeMillis();
        //合并文件
        merge(files, table_name+".txt");
        if (delete_merge_file.equals("true")) {
            deleteFile(files);
        }
        System.out.println("合并文件完毕! 耗时：" + (System.currentTimeMillis()-s));
        service.shutdown();
    }


    public static void merge(List<File> files , String to) {
        File t = new File(to);
        FileInputStream in = null;
        FileChannel inChannel = null;

        FileOutputStream out = null ;
        FileChannel outChannel = null ;
        try {
            out = new FileOutputStream(t, true);
            outChannel = out.getChannel();
            // 记录新文件最后一个数据的位置
            long start = 0;
            for (File file : files) {
                in = new FileInputStream(file);
                inChannel = in.getChannel();
                // 从inChannel中读取file.length()长度的数据，写入outChannel的start处
                outChannel.transferFrom(inChannel, start, file.length());
                start += file.length();
                in.close();
                inChannel.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                outChannel.close();
            } catch (Exception e2) {
            }
        }
    }

    public static void deleteFile(List<File> files) {
        try {
            for (File file: files) {
                if(file.delete()) {
                    System.out.println( file.getName() + " is deleted!");
                }else {
                    System.out.println("Delete operation is failed.");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
