package datagenerator.transaction;


import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.EnumValue;
import datagenerator.common.columngenerator.IdentityCard;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;

public class BDFMHQAA extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    private ColumnDataGen aa01ac15;

    private ColumnDataGen aa09sn03;

    private ColumnDataGen aa11acfg;

    private ColumnDataGen aa12act2;

    private ColumnDataGen aa15zhzt;

    private ColumnDataGen aa66pefg;

    private ColumnDataGen aa30sn08;

    private ColumnDataGen aa31sn03;

    private ColumnDataGen aa49sn08;

    private ColumnDataGen aa71sn08;

    private ColumnDataGen aa34no16;

    private ColumnDataGen aa51date;

    private ColumnDataGen aa43date;

    private ColumnDataGen aa45brno;

    private ColumnDataGen aa62cfno;

    private ColumnDataGen aa28tlcd;

    private ColumnDataGen aa44date;

    private ColumnDataGen boidbrno;

    private ColumnDataGen bdidbrbm;

    private ColumnDataGen buopstaf;

    private ColumnDataGen budidate;

    private DateTime butsstam;

    private ColumnDataGen boidbrno1;

    public void init() {
        super.init();
        String[] aa15zhzt1 = { "1", "2" };
        String[] aa34no161 = { "110000000000", "1000110000000000" };
        this.aa01ac15 = (ColumnDataGen)new RandomNumber("aa01ac15", 7);
        this.aa09sn03 = (ColumnDataGen)new RandomInt("aa09sn03", 3, 2);
        this.aa15zhzt = (ColumnDataGen)new EnumValue("aa15zhzt", aa15zhzt1);
        this.aa12act2 = (ColumnDataGen)new RandomInt("aa12act2", 6, 6);
        this.aa31sn03 = (ColumnDataGen)new RandomInt("aa31sn03", 1, 16);
        this.aa49sn08 = (ColumnDataGen)new RandomInt("aa49sn08", 1, 17);
        this.aa71sn08 = (ColumnDataGen)new RandomInt("aa71sn08", 10, 400);
        this.aa34no16 = (ColumnDataGen)new EnumValue("aa34no16", aa34no161);
        this.aa62cfno = (ColumnDataGen)new IdentityCard("aa62cfno", "19800909", "19700909");
        this.aa28tlcd = (ColumnDataGen)new RandomInt("aa28tlcd", 10, 3);
        this.boidbrno = (ColumnDataGen)new RandomNumber("boidbrno", 3);
        this.boidbrno1 = (ColumnDataGen)new RandomInt("boidbrno1", 10, 199);
        this.buopstaf = (ColumnDataGen)new RandomNumber("buopstaf", 4);
        this.budidate = (ColumnDataGen)new DateYYYYMMDD("budidate");
        this.butsstam = new DateTime("butsstam");
    }

    public String nextRecord() {
        String date1 = this.budidate.getColumnData();
        int date2 = Integer.parseInt(date1) + (int)(Math.random() * 2.0D + 1.0D) * 10000;
        int date3 = date2 + (int)(Math.random() * 2.0D + 1.0D) * 10000;
        int bm1 = Integer.parseInt(this.boidbrno1.getColumnData()) + 800;
        String bm1_3 = bm1 + "" + this.boidbrno.getColumnData();
        String yg1 = bm1 + "" + this.buopstaf.getColumnData();
        int bm2 = Integer.parseInt(this.boidbrno1.getColumnData()) + 800;
        String bm2_3 = bm2 + "" + this.boidbrno.getColumnData();
        String yg2 = bm2 + "" + this.buopstaf.getColumnData();
        String aa03csno1 = "8100" + this.aa01ac15.getColumnData();
        this.sb.setLength(0);
        this.sb.append("10101100" + this.aa01ac15.getColumnData() + ",");
        this.sb.append(aa03csno1 + ",");
        this.sb.append("客户" + aa03csno1 + ",");
        this.sb.append(",");
        this.sb.append("101,");
        this.sb.append("11,");
        this.sb.append("102,");
        this.sb.append(this.aa09sn03.getColumnData() + ",");
        this.sb.append("1,");
        this.sb.append(this.aa15zhzt.getColumnData() + ",");
        this.sb.append(this.aa12act2.getColumnData() + ",");
        this.sb.append("11,");
        this.sb.append(",");
        this.sb.append("0,");
        this.sb.append(this.aa15zhzt.getColumnData() + ",");
        this.sb.append("1,");
        this.sb.append("0,");
        this.sb.append("1,");
        this.sb.append("2,");
        this.sb.append(this.aa09sn03.getColumnData() + ",");
        this.sb.append("51,");
        this.sb.append("0397B7383C68E3DB8AF5DA1FB1BC14BA,");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append("1,");
        this.sb.append("1,");
        this.sb.append(this.aa12act2.getColumnData() + ",");
        this.sb.append(this.aa31sn03.getColumnData() + ",");
        this.sb.append(this.aa49sn08.getColumnData() + ",");
        this.sb.append(this.aa71sn08.getColumnData() + ",");
        this.sb.append(this.aa34no16.getColumnData() + ",");
        this.sb.append(date2 + ",");
        this.sb.append(date1 + ",");
        this.sb.append(",");
        this.sb.append(bm1 + this.boidbrno.getColumnData() + ",");
        this.sb.append(bm1 + this.boidbrno.getColumnData() + ",");
        this.sb.append(bm1 + "000,");
        this.sb.append(date1 + ",");
        this.sb.append("18991231,");
        this.sb.append("0,");
        this.sb.append(",");
        this.sb.append("1,");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append(",");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append("101,");
        this.sb.append(this.aa62cfno.getColumnData() + ",");
        this.sb.append(this.aa15zhzt.getColumnData() + ",");
        this.sb.append(this.aa28tlcd.getColumnData() + ",");
        this.sb.append("0,");
        this.sb.append("9,");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append(",");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(date2 + ",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append("B,");
        this.sb.append(date1 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date1) + ",");
        this.sb.append(yg1 + ",");
        this.sb.append(date2 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date2 + "") + ",");
        this.sb.append(yg2 + ",");
        this.sb.append(date3 + ",");
        this.sb.append("0");
        return this.sb.toString();
    }

    public String getTableName() {
        return super.getTableName();
    }

    public BDFMHQAA(String tableName) {
        super(tableName);
    }

    public static void main(String[] args) {
        int recordNums = Integer.valueOf(20000000).intValue();
        Out o = new Out(String.valueOf("BDFMHQAA.txt"));
        BDFMHQAA report = new BDFMHQAA("transaction");
        report.init();
        int nBacthCount = 2000;
        StringBuffer records = new StringBuffer();
        records.append(report.nextRecord());
        for (int i = 1; i < recordNums; i++) {
            if (i % nBacthCount == 0) {
                System.out.println(records.toString());
                records.setLength(0);
                records.append(report.nextRecord());
            } else {
                records.append("\n" + report.nextRecord());
            }
        }
        System.out.println(records.toString());
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/ODSDB/BDFMHQAA/BDFMHQAA.txt"));
        BDFMHQAA report = new BDFMHQAA("transaction");
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