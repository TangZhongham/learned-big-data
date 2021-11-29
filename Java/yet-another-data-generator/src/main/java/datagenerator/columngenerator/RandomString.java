package datagenerator.columngenerator;

import java.util.Random;

public class RandomString extends AbstractColumnDataGen {
    private static String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private int columnLength;

    public RandomString(String columnName, int columnLength) {
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
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int num = Integer.valueOf(5).intValue();
        RandomString rs = new RandomString("randomString", num);
        rs.init();
        for (int i = 0; i < 10; i++)
            System.out.println("," + rs.getColumnData() + "," + rs.getColumnData() + "," + rs.getColumnData() + "," + rs.getColumnData() + "," + rs.getColumnData() + "," + rs.getColumnData() + "," + rs.getColumnData() + "," + rs.getColumnData() + "," + rs.getColumnData() + "," + rs.getColumnData() + "," + rs.getColumnData() + "," + rs.getColumnData() + "," + rs.getColumnData());
    }
}
