package datagenerator.transaction;


import datagenerator.common.columngenerator.Address;
import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.DateYYYYMMDD1;
import datagenerator.common.columngenerator.EnumValue;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;

public class BCFMPCAI extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    private ColumnDataGen cinocsno;

    private ColumnDataGen bidtdate;

    private ColumnDataGen sexysexx;

    private ColumnDataGen marimrst;

    private ColumnDataGen anhonm5b;

    private ColumnDataGen pyizpy1b;

    private ColumnDataGen pyimamt;

    private ColumnDataGen hyimamt;

    private ColumnDataGen phsaedbg;

    private ColumnDataGen hdgrhd1b;

    private ColumnDataGen headduty;

    private ColumnDataGen supoqty5;

    private ColumnDataGen rcstrs1b;

    private ColumnDataGen boidbrno;

    private ColumnDataGen bdidbrbm;

    private ColumnDataGen buopstaf;

    private ColumnDataGen budidate;

    private DateTime butsstam;

    private ColumnDataGen fathfa1b;

    private ColumnDataGen boidbrno1;

    public void init() {
        super.init();
        String[] sexysexx1 = { "1", "2" };
        String[] marimrst1 = { "20", "90", "10" };
        String[] phsaedbg1 = { "99", "60" };
        String[] hdgrhd1b1 = { "", "1" };
        String[] rcstrs1b1 = { "", "9" };
        this.cinocsno = (ColumnDataGen)new RandomNumber("cinocsno", 7);
        this.bidtdate = (ColumnDataGen)new DateYYYYMMDD1("bidtdate");
        this.sexysexx = (ColumnDataGen)new EnumValue("sexysexx", sexysexx1);
        this.marimrst = (ColumnDataGen)new EnumValue("marimrst", marimrst1);
        this.anhonm5b = (ColumnDataGen)new Address("anhonm5b");
        this.pyizpy1b = (ColumnDataGen)new RandomInt("pyizpy1b", 10, 10);
        this.pyimamt = (ColumnDataGen)new RandomInt("pyimamt", 10, 40);
        this.phsaedbg = (ColumnDataGen)new EnumValue("phsaedbg", phsaedbg1);
        this.hdgrhd1b = (ColumnDataGen)new EnumValue("hdgrhd1b", hdgrhd1b1);
        this.fathfa1b = (ColumnDataGen)new RandomInt("fathfa1b", 10, 2);
        this.rcstrs1b = (ColumnDataGen)new EnumValue("rcstrs1b", rcstrs1b1);
        this.boidbrno = (ColumnDataGen)new RandomNumber("boidbrno", 3);
        this.boidbrno1 = (ColumnDataGen)new RandomInt("boidbrno1", 10, 199);
        this.buopstaf = (ColumnDataGen)new RandomNumber("buopstaf", 4);
        this.budidate = (ColumnDataGen)new DateYYYYMMDD("budidate");
        this.butsstam = new DateTime("butsstam");
        this.supoqty5 = (ColumnDataGen)new RandomInt("supoqty5", 5, 5);
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
        this.sb.setLength(0);
        this.sb.append("B,");
        this.sb.append("8100" + this.cinocsno.getColumnData() + ",");
        this.sb.append(this.bidtdate.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(this.sexysexx.getColumnData() + ",");
        this.sb.append(this.marimrst.getColumnData() + ",");
        this.sb.append("156,");
        this.sb.append("C,");
        this.sb.append("C,");
        this.sb.append(",");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append(this.anhonm5b.getColumnData() + ",");
        this.sb.append("156,");
        this.sb.append(",");
        this.sb.append("1,");
        this.sb.append(this.pyizpy1b.getColumnData() + ",");
        this.sb.append((Integer.parseInt(this.pyimamt.getColumnData()) * 10000) + ",");
        this.sb.append(this.pyizpy1b.getColumnData() + ",");
        this.sb.append("0,");
        this.sb.append("99,");
        this.sb.append("9,");
        this.sb.append(this.hdgrhd1b.getColumnData() + ",");
        this.sb.append("900,");
        this.sb.append("9,");
        this.sb.append("0,");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(this.supoqty5.getColumnData() + ",");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append("0,");
        this.sb.append(this.rcstrs1b.getColumnData() + ",");
        this.sb.append("0,");
        this.sb.append(bm1_3 + ",");
        this.sb.append(bm1 + ",");
        this.sb.append(yg1 + ",");
        this.sb.append(date1 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date1) + ",");
        this.sb.append(bm2_3 + ",");
        this.sb.append(bm2 + ",");
        this.sb.append(yg2 + ",");
        this.sb.append(date2 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date2 + "") + ",");
        this.sb.append(date3 + ",");
        this.sb.append("0");
        return this.sb.toString();
    }

    public String getTableName() {
        return super.getTableName();
    }

    public BCFMPCAI(String tableName) {
        super(tableName);
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out("/tmp/data/ODSDB/BCFMPCAI/BCFMPCAI.txt");
        BCFMPCAI report = new BCFMPCAI("transaction");
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