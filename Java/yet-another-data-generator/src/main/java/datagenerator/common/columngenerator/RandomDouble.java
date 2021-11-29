package datagenerator.common.columngenerator;

import java.text.DecimalFormat;
import java.util.Random;

public class RandomDouble extends AbstractColumnDataGen {
    private int maxNum;

    public RandomDouble(String columnName, int maxNum) {
        super(columnName);
        this.maxNum = maxNum;
    }

    public void init() {}

    public String getColumnData() {
        StringBuffer sb = new StringBuffer();
        sb.append(getRandomDouble(this.maxNum));
        return sb.toString();
    }

    public static double getRandomDouble(int maxNum) {
        DecimalFormat df = new DecimalFormat("#.00");
        int a = (int)(Math.random() * 2.0D + 1.0D);
        Random random = new Random();
        return Double.valueOf(df.format(random.nextDouble() * maxNum * a)).doubleValue();
    }
}
