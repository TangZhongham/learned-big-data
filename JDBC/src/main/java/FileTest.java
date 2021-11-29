import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileTest {

    // around 2401 ms, so more lie 0.24 ms one file.
    public static int num = 10000;

    public static long start_time;
    public static long end_time;

    public static String random_string = "sdfojnvoadnsreonvaenvfklmakdlc";


    public static void main(String[] args) {
        if (args.length == 1) {
            num = Integer.parseInt(args[0]);
        } else {
            System.out.println("没有参数，默认循环10000次");
        }

        start_time = System.currentTimeMillis();

        for (int i = 0; i < num; i++) {

            try {
                File file = new File(String.format("%s.txt", i));
                FileWriter myWriter = new FileWriter(file);
                myWriter.write(random_string+file.getName());
                myWriter.close();
            } catch (IOException f) {
                f.printStackTrace();
            }
        }


        end_time = System.currentTimeMillis();
        System.out.println(String.format("总文件写入次数：%s, 总耗时：%s, 平均单次耗时：%s", num, end_time-start_time,
                (end_time-start_time)/num));
    }
}
