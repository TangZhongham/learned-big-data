package datagenerator.transaction;


import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.PhoneNumber;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.columngenerator.RandomString;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;

public class BCFMCPNI extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    private ColumnDataGen isidisid;

    private ColumnDataGen cinocsno;

    private ColumnDataGen pnsnptsn;

    private ColumnDataGen pnnoteln;

    private ColumnDataGen boidbrno;

    private ColumnDataGen bdidbrbm;

    private ColumnDataGen buopstaf;

    private ColumnDataGen budidate;

    private DateTime butsstam;

    public void init() {
        this.isidisid = (ColumnDataGen)new RandomString("isidisid", 1);
        this.cinocsno = (ColumnDataGen)new RandomNumber("cinocsno", 11);
        this.pnsnptsn = (ColumnDataGen)new RandomInt("pnsnptsn", 10, 10);
        this.pnnoteln = (ColumnDataGen)new PhoneNumber("pnnoteln");
        this.boidbrno = (ColumnDataGen)new RandomNumber("boidbrno", 6);
        this.buopstaf = (ColumnDataGen)new RandomNumber("buopstaf", 4);
        this.budidate = (ColumnDataGen)new DateYYYYMMDD("budidate");
        this.butsstam = new DateTime("butsstam");
    }

    public String nextRecord() {
        String date1 = this.budidate.getColumnData();
        int date2 = Integer.parseInt(date1) + (int)(Math.random() * 4.0D + 1.0D) * 10000;
        int date3 = date2 + (int)(Math.random() * 4.0D + 1.0D) * 10000;
        String no1 = this.boidbrno.getColumnData();
        String bm1 = no1.substring(0, 3);
        String no2 = this.boidbrno.getColumnData();
        String bm2 = no2.substring(0, 3);
        this.sb.setLength(0);
        this.sb.append(this.isidisid.getColumnData() + ",");
        this.sb.append(this.cinocsno.getColumnData() + ",");
        this.sb.append(this.pnsnptsn.getColumnData() + ",");
        this.sb.append(this.pnsnptsn.getColumnData() + ",");
        this.sb.append(this.pnnoteln.getColumnData() + ",");
        this.sb.append(" ,");
        this.sb.append("0,");
        this.sb.append(no1 + ",");
        this.sb.append(bm1 + ",");
        this.sb.append(bm1 + this.buopstaf.getColumnData() + ",");
        this.sb.append(date1 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date1) + ",");
        this.sb.append(no2 + ",");
        this.sb.append(bm2 + ",");
        this.sb.append(bm2 + this.buopstaf.getColumnData() + ",");
        this.sb.append(date2 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date2 + "") + ",");
        this.sb.append(date3 + ",");
        this.sb.append("0");
        return this.sb.toString();
    }

    public String getTableName() {
        return super.getTableName();
    }

    public BCFMCPNI(String tableName) {
        super(tableName);
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/ODSDB/BCFMCPNI/BCFMCPNI.txt"));
        BCFMCPNI report = new BCFMCPNI("transaction");
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
