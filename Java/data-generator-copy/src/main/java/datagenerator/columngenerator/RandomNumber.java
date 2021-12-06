package datagenerator.columngenerator;

import java.text.DecimalFormat;
import java.util.Random;

public class RandomNumber extends AbstractColumnDataGen {
    private static String base1 = "123456789";

    private static String base2 = "0123456789";

    private int columnLength;

    public RandomNumber(String columnName, int columnLength) {
        super(columnName);
        this.columnLength = columnLength;
    }

    public void init() {}

    public String getColumnData() {
        StringBuffer sb = new StringBuffer();
        sb.append(getRandomString(this.columnLength));
        return sb.toString();
    }

    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int number = random.nextInt(base1.length());
        sb.append(base1.charAt(number));
        for (int i = 0; i < length - 1; i++) {
            number = random.nextInt(base2.length());
            sb.append(base2.charAt(number));
        }
        return sb.toString();
    }

    public static double getDouble(int maxNum) {
        DecimalFormat df = new DecimalFormat("#.00");
        int a = (int)(Math.random() * 2.0D + 1.0D);
        int aa = (int)Math.pow(-1.0D, a);
        Random random = new Random();
        return Double.valueOf(df.format(random.nextDouble() * maxNum * aa)).doubleValue();
    }

    public static void main(String[] args) {
        RandomNumber rn = new RandomNumber("randomString", 5);
        rn.init();
        for (int i = 0; i < 10; i++)
            System.out.println(getDouble(1000));
    }
}