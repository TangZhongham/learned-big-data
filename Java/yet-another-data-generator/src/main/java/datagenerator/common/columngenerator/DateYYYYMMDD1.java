package datagenerator.common.columngenerator;


import java.util.ArrayList;

public class DateYYYYMMDD1 extends AbstractColumnDataGen {
    private ArrayList<String> years = new ArrayList<>();

    public DateYYYYMMDD1(String columnName) {
        super(columnName);
        init();
    }

    public void init() {
        this.years.add("1970");
        this.years.add("1971");
        this.years.add("1972");
        this.years.add("1973");
        this.years.add("1974");
        this.years.add("1975");
        this.years.add("1976");
        this.years.add("1977");
        this.years.add("1978");
        this.years.add("1981");
        this.years.add("1979");
        this.years.add("1980");
    }

    public String getColumnData() {
        StringBuffer date = new StringBuffer();
        date.append(this.years.get((int)(Math.random() * this.years.size())));
        int month = (int)(Math.random() * 12.0D + 1.0D);
        int day = (int)(Math.random() * 28.0D + 1.0D);
        if (month < 10 && day < 10) {
            date.append("0" + month + "0" + day);
        } else if (month < 10 && day >= 10) {
            date.append("0" + month + day);
        } else if (month >= 10 && day < 10) {
            date.append(month + "0" + day);
        } else {
            date.append(month + "" + day);
        }
        return date.toString();
    }

    public String getYearData() {
        return this.years.get((int)(Math.random() * this.years.size()));
    }

    public int getMonthData() {
        return (int)(Math.random() * 12.0D + 1.0D);
    }

    public int getDayData() {
        return (int)(Math.random() * 28.0D + 1.0D);
    }

    public String getColumnData(int nTimeType) {
        int nMonth;
        switch (nTimeType) {
            case 1:
                return getYearData();
            case 2:
                nMonth = getMonthData();
                if (nMonth < 10)
                    return getYearData() + "0" + nMonth;
                return getYearData() + nMonth;
            case 3:
                return getColumnData();
        }
        return "";
    }

    public static void main(String[] args) {
        DateYYYYMMDD1 dateYYYYMMDD = new DateYYYYMMDD1("dateYYYYMMDD");
        dateYYYYMMDD.init();
        for (int i = 0; i < 1000; i++)
            System.out.println(dateYYYYMMDD.getColumnData());
    }
}