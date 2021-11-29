package datagenerator.transaction;


import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.EnumValue;
import datagenerator.common.columngenerator.RandomDouble;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;

public class AFFMYRPT extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    private ColumnDataGen yrcabkno;

    private ColumnDataGen yrcadate;

    private ColumnDataGen yrcaccyc;

    private ColumnDataGen yrcaexrt;

    private DateTime yrcastam;

    private ColumnDataGen yrcastaf;

    public void init() {
        super.init();
        String[] yrcaccyc1 = { "GBP", "CAD", "AUD", "CHF", "HKD", "JPY", "EUR", "SGD", "USD", "EUR" };
        this.yrcadate = (ColumnDataGen)new DateYYYYMMDD("yrcadate");
        this.yrcaccyc = (ColumnDataGen)new EnumValue("yrcaccyc", yrcaccyc1);
        this.yrcaexrt = (ColumnDataGen)new RandomDouble("yrcaexrt", 500);
        this.yrcastaf = (ColumnDataGen)new RandomNumber("yrcastaf", 3);
        this.yrcastam = new DateTime("yrcastam");
    }

    public String nextRecord() {
        String date1 = this.yrcadate.getColumnData();
        int date2 = Integer.parseInt(date1) + (int)(Math.random() * 1.0D + 1.0D) * 10000;
        String year = (date2 + "").substring(0, 4);
        this.sb.setLength(0);
        this.sb.append("999,");
        this.sb.append(date2 + ",");
        this.sb.append(this.yrcaccyc.getColumnData() + ",");
        this.sb.append("100,");
        this.sb.append(year + ",");
        this.sb.append(this.yrcaexrt.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(date1 + ",");
        this.sb.append(this.yrcastam.getDateTimefromYYYYMMDD(date1) + ",");
        this.sb.append("999S" + this.yrcastaf.getColumnData() + ",");
        this.sb.append(date2 + ",");
        this.sb.append(this.yrcastam.getDateTimefromYYYYMMDD(date2 + "") + ",");
        this.sb.append("999S" + this.yrcastaf.getColumnData() + ",");
        this.sb.append(date2 + ",");
        this.sb.append("0");
        return this.sb.toString();
    }

    public String getTableName() {
        return super.getTableName();
    }

    public AFFMYRPT(String tableName) {
        super(tableName);
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/ODSDB/AFFMYRPT/AFFMYRPT.txt"));
        AFFMYRPT report = new AFFMYRPT("transaction");
        report.init();
        int nBacthCount = 200000;
        StringBuffer records = new StringBuffer();
        records.append(report.nextRecord());
        for (int i = 1; i < recordNums; i++) {
            if (i % nBacthCount == 0) {
                o.writeToHdfs(records);
                records.setLength(0);
                records.append(report.nextRecord());
            } else {
                records.append("\n" + report.nextRecord());
            }
        }
        o.writeToHdfs(records);
        o.close();
    }
}
