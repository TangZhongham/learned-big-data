package datagenerator.transaction;


import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.EnumValue;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomString;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;
import java.text.DecimalFormat;

public class BBFMDJZL extends AbstractTableDataGen {
    StringBuffer record = new StringBuffer();

    DecimalFormat df = new DecimalFormat("#.00");

    private ColumnDataGen djcano08;

    private ColumnDataGen djcano10;

    private ColumnDataGen djcbno10;

    private ColumnDataGen djcamkid;

    private ColumnDataGen djcabool;

    private ColumnDataGen djcbbool;

    private ColumnDataGen djcarmrk;

    private ColumnDataGen djcbrmrk;

    private ColumnDataGen djcars1b;

    private ColumnDataGen djcadate;

    private DateTime djcastam;

    private ColumnDataGen djcastaf;

    private ColumnDataGen djcbdate;

    private DateTime djcbstam;

    private ColumnDataGen djcbstaf;

    private ColumnDataGen last_etl_acg_dt;

    private ColumnDataGen del_f;

    public BBFMDJZL(String tableName) {
        super(tableName);
    }

    public void init() {
        String[] resDes = { ", ", ", ", ", ", ", ", ", "};
        this.djcano08 = (ColumnDataGen)new RandomString("djcano08", 4);
        this.djcano10 = (ColumnDataGen)new RandomInt("djcano10", 10, 1000);
        this.djcbno10 = (ColumnDataGen)new RandomInt("djcbno10", 10, 8000);
        this.djcarmrk = (ColumnDataGen)new EnumValue("djcarmrk", resDes);
        this.djcadate = (ColumnDataGen)new DateYYYYMMDD("djcadate");
        this.djcastam = new DateTime("djcastam");
        this.djcastaf = (ColumnDataGen)new RandomString("djcastaf", 7);
        this.djcbdate = (ColumnDataGen)new DateYYYYMMDD("djcbdate");
        this.djcbstam = new DateTime("djcbstam");
        this.djcbstaf = (ColumnDataGen)new RandomString("djcbstaf", 7);
        this.last_etl_acg_dt = (ColumnDataGen)new DateYYYYMMDD("last_etl_acg_dt");
    }

    public String nextRecord() {
        String data1 = this.djcadate.getColumnData();
        int data2 = Integer.parseInt(data1) + (int)(Math.random() * 4.0D + 1.0D) * 10000;
        int data3 = data2 + (int)(Math.random() * 4.0D + 1.0D) * 10000;
        int cano10 = Integer.parseInt(this.djcano10.getColumnData());
        String up = (cano10 < 200) ? (cano10 + "") : "";
        String own = this.djcbno10.getColumnData();
        while (own.equals(up))
            own = this.djcbno10.getColumnData();
        this.record.setLength(0);
        this.record.append(this.djcano08.getColumnData() + ",");
        this.record.append(up + ",");
        this.record.append(own + ",");
        this.record.append(" ,");
        if (cano10 < 200) {
            this.record.append("2,");
        } else {
            this.record.append("1,");
        }
        this.record.append("0,");
        this.record.append(this.djcarmrk.getColumnData() + ",");
        this.record.append(" , ,");
        this.record.append(data1 + ",");
        this.record.append(this.djcastam.getDateTimefromYYYYMMDD(data1) + ",");
        this.record.append(this.djcastaf.getColumnData() + ",");
        this.record.append(data2 + ",");
        this.record.append(this.djcbstam.getDateTimefromYYYYMMDD(data2 + "") + ",");
        this.record.append(this.djcastaf.getColumnData() + ",");
        this.record.append(data3 + ",");
        this.record.append(0);
        return this.record.toString();
    }

    public String getTableName() {
        return super.getTableName();
    }

    public void generateData(int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/ODSDB/BBFMDJZL/BBFMDJZL.txt"));
        BBFMDJZL report = new BBFMDJZL("transaction");
        report.init();
        int nBacthCount = 100000;
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
