package datagenerator.common.columngenerator;


import java.util.ArrayList;

public class PhoneNumber extends AbstractColumnDataGen {
    private ArrayList<String> prefixs = new ArrayList<>();

    private String nums = "0123456789";

    public PhoneNumber(String columnName) {
        super(columnName);
        init();
    }

    public void init() {
        this.prefixs.add("134");
        this.prefixs.add("135");
        this.prefixs.add("136");
        this.prefixs.add("137");
        this.prefixs.add("138");
        this.prefixs.add("139");
        this.prefixs.add("147");
        this.prefixs.add("150");
        this.prefixs.add("151");
        this.prefixs.add("152");
        this.prefixs.add("157");
        this.prefixs.add("158");
        this.prefixs.add("159");
        this.prefixs.add("182");
        this.prefixs.add("187");
        this.prefixs.add("188");
        this.prefixs.add("130");
        this.prefixs.add("131");
        this.prefixs.add("132");
        this.prefixs.add("155");
        this.prefixs.add("156");
        this.prefixs.add("185");
        this.prefixs.add("186");
        this.prefixs.add("133");
        this.prefixs.add("153");
        this.prefixs.add("180");
        this.prefixs.add("189");
    }

    public String getColumnData() {
        int index = (int)(Math.random() * this.prefixs.size());
        StringBuffer sb = new StringBuffer();
        sb.append(this.prefixs.get(index));
        for (int i = 0; i < 9; i++) {
            index = (int)(Math.random() * this.nums.length());
            sb.append(this.nums.charAt(index));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        PhoneNumber number = new PhoneNumber("pn");
        number.init();
        for (int i = 0; i < 10000; i++)
            System.out.println(number.getColumnData());
    }
}