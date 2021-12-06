package datagenerator.columngenerator;

public class RandomInt extends AbstractColumnDataGen {
    private int columnLength;

    private int maxValue;

    public RandomInt(String columnName, int columnLength, int maxValue) {
        super(columnName);
        this.columnLength = columnLength;
        this.maxValue = maxValue;
    }

    public void init() {}

    public String getColumnData() {
        StringBuffer sb = new StringBuffer();
        int s = (int)(Math.random() * this.maxValue);
        sb.append(s);
        return sb.toString();
    }

    public static void main(String[] args) {
        RandomInt randomInt = new RandomInt("timeHHMMSS", 1, 2);
        randomInt.init();
        for (int i = 0; i < 10000; i++)
            System.out.println(randomInt.getColumnData());
    }
}

