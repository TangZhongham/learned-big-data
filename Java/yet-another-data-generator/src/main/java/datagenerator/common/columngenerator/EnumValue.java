package datagenerator.common.columngenerator;

public class EnumValue extends AbstractColumnDataGen {
    private String[] enumValue;

    public EnumValue(String columnName, String[] enumValue) {
        super(columnName);
        this.enumValue = enumValue;
    }

    public void init() {}

    public String getColumnData() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.enumValue[(int)(Math.random() * this.enumValue.length)]);
        return sb.toString();
    }

    public static void main(String[] args) {
        String[] enumValue = { "01", "02", "03", "04" };
        EnumValue rn = new EnumValue("enumValue", enumValue);
        rn.init();
        for (int i = 0; i < 1000; i++)
            System.out.println(rn.getColumnData());
    }
}

