package datagenerator.common.columngenerator;


import java.util.ArrayList;

public class DateTime extends AbstractColumnDataGen {
    private String date;

    private ArrayList<String> years = new ArrayList<>();

    public DateTime(String columnName) {
        super(columnName);
        init();
    }

    public DateTime(String columnName, String date) {
        super(columnName);
        this.date = date;
        init();
    }

    public void init() {
        this.years.add("2018");
    }

    public String getColumnData() {
        StringBuffer date = new StringBuffer();
        date.append(this.years.get((int)(Math.random() * this.years.size())));
        int month = (int)(Math.random() * 12.0D + 1.0D);
        int day = (int)(Math.random() * 28.0D + 1.0D);
        if (month < 10 && day < 10) {
            date.append("-0" + month + "-0" + day);
        } else if (month < 10 && day >= 10) {
            date.append("-0" + month + "-" + day);
        } else if (month >= 10 && day < 10) {
            date.append("-" + month + "-0" + day);
        } else {
            date.append("-" + month + "-" + day);
        }
        int hour = (int)(Math.random() * 24.0D);
        int min = (int)(Math.random() * 60.0D);
        int sec = (int)(Math.random() * 60.0D);
        int ms = 1000000 - (int)(Math.random() * 990000.0D);
        date.append((hour < 10) ? ("-0" + hour) : ("-" + hour));
        date.append((min < 10) ? (".0" + min) : ("." + min));
        date.append((sec < 10) ? (".0" + sec) : ("." + sec));
        date.append((ms < 100000) ? (".0" + ms) : ("." + ms));
        return date.toString();
    }

    public String getDateTimefromYYYYMMDD(String date) {
        StringBuffer sb = new StringBuffer();
        String year = date.substring(0, 4);
        String moth = date.substring(4, 6);
        String day = date.substring(6, date.length());
        int hour = (int)(Math.random() * 24.0D);
        int min = (int)(Math.random() * 60.0D);
        int sec = (int)(Math.random() * 60.0D);
        int ms = 1000000 - (int)(Math.random() * 990000.0D);
        sb.append(year);
        sb.append("-" + moth);
        sb.append("-" + day);
        sb.append((hour < 10) ? ("-0" + hour) : ("-" + hour));
        sb.append((min < 10) ? (".0" + min) : ("." + min));
        sb.append((sec < 10) ? (".0" + sec) : ("." + sec));
        sb.append((ms < 100000) ? (".0" + ms) : ("." + ms));
        return sb.toString();
    }

    public static void main(String[] args) {
        DateTime datetime = new DateTime("datetime");
        datetime.init();
        for (int i = 0; i < 1000; i++)
            System.out.println(datetime.getDateTimefromYYYYMMDD("20140928"));
    }
}