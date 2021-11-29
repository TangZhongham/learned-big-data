package datagenerator.transaction;


import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.EnumValue;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;

public class BCFMCDBI extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    private ColumnDataGen cinocsno;

    private ColumnDataGen ccbiwork;

    private ColumnDataGen ccbisetp;

    private ColumnDataGen boidbrno;

    private ColumnDataGen bdidbrbm;

    private ColumnDataGen buopstaf;

    private ColumnDataGen budidate;

    private DateTime butsstam;

    private ColumnDataGen boidbrno1;

    public void init() {
        super.init();
        String[] ccbisetp1 = { "1", "2" };
        this.cinocsno = (ColumnDataGen)new RandomNumber("cinocsno", 7);
        this.ccbiwork = (ColumnDataGen)new RandomInt("ccbiwork", 10, 100);
        this.ccbisetp = (ColumnDataGen)new EnumValue("ccbisetp", ccbisetp1);
        this.boidbrno = (ColumnDataGen)new RandomNumber("boidbrno", 3);
        this.boidbrno1 = (ColumnDataGen)new RandomInt("boidbrno1", 10, 199);
        this.buopstaf = (ColumnDataGen)new RandomNumber("buopstaf", 4);
        this.budidate = (ColumnDataGen)new DateYYYYMMDD("budidate");
        this.butsstam = new DateTime("butsstam");
    }

    public String nextRecord() {
        String date1 = this.budidate.getColumnData();
        int date2 = Integer.parseInt(date1) + (int)(Math.random() * 2.0D + 1.0D) * 10000;
        int bm1 = Integer.parseInt(this.boidbrno1.getColumnData()) + 800;
        String bm1_3 = bm1 + "" + this.boidbrno.getColumnData();
        String yg1 = bm1 + "" + this.buopstaf.getColumnData();
        int bm2 = Integer.parseInt(this.boidbrno1.getColumnData()) + 800;
        String bm2_3 = bm2 + "" + this.boidbrno.getColumnData();
        String yg2 = bm2 + "" + this.buopstaf.getColumnData();
        this.sb.setLength(0);
        this.sb.append("8100" + this.cinocsno.getColumnData() + ",");
        this.sb.append(this.ccbiwork.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(this.ccbisetp.getColumnData() + ",");
        this.sb.append(bm1_3 + ",");
        this.sb.append(yg1 + ",");
        this.sb.append(date1 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date1) + ",");
        this.sb.append(bm2_3 + ",");
        this.sb.append(yg2 + ",");
        this.sb.append(date2 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date2 + "") + ",");
        this.sb.append(date2 + ",");
        this.sb.append("0");
        return this.sb.toString();
    }

    public String getTableName() {
        return super.getTableName();
    }

    public BCFMCDBI(String tableName) {
        super(tableName);
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/ODSDB/BCFMCDBI/BCFMCDBI.txt"));
        BCFMCDBI report = new BCFMCDBI("transaction");
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
