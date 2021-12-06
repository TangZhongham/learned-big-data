package datagenerator.transaction;


import datagenerator.columngenerator.DateTime;
import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.EnumValue;
import datagenerator.common.columngenerator.IdentityCard;
import datagenerator.common.columngenerator.Name;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.columngenerator.RandomString;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;
import java.text.DecimalFormat;

public class BCFMCIDI extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    DecimalFormat df = new DecimalFormat("#.00");

    private ColumnDataGen isidisid;

    private ColumnDataGen cinocsno;

    private ColumnDataGen idtycftp;

    private ColumnDataGen idnocfno;

    private ColumnDataGen miflbool;

    private ColumnDataGen idnanm5b;

    private ColumnDataGen isdtdate;

    private ColumnDataGen isouisbu;

    private ColumnDataGen yechye1b;

    private ColumnDataGen rcstrs1b;

    private ColumnDataGen boidbrno;

    private ColumnDataGen buopstaf;

    private ColumnDataGen budidate;

    private DateTime butsstam;

    public BCFMCIDI(String tableName) {
        super(tableName);
    }

    public void init() {
        String[] res = { ", "};
        this.isidisid = (ColumnDataGen)new RandomString("isidisid", 1);
        this.cinocsno = (ColumnDataGen)new RandomNumber("cinocsno", 11);
        this.idtycftp = (ColumnDataGen)new RandomNumber("idtycftp", 3);
        this.idnocfno = (ColumnDataGen)new IdentityCard("idnocfno", "19700101", "19901212");
        this.idnanm5b = (ColumnDataGen)new Name("idnanm5b");
        this.isdtdate = (ColumnDataGen)new DateYYYYMMDD("isdtdate");
        this.isouisbu = (ColumnDataGen)new EnumValue("isouisbu", res);
        this.yechye1b = (ColumnDataGen)new RandomInt("yechye1b", 10, 10);
        this.boidbrno = (ColumnDataGen)new RandomNumber("boidbrno", 7);
        this.buopstaf = (ColumnDataGen)new RandomNumber("buopstaf", 4);
        this.butsstam = new DateTime("butsstam");
    }

    public String nextRecord() {
        String date1 = this.isdtdate.getColumnData();
        int isdtdate = Integer.parseInt(date1) - 100000;
        int exdtdate = Integer.parseInt(date1) + 100000;
        int date2 = Integer.parseInt(date1) + (int)(Math.random() * 4.0D + 1.0D) * 10000;
        int date3 = date2 + (int)(Math.random() * 4.0D + 1.0D) * 10000;
        int aa = Integer.parseInt(this.yechye1b.getColumnData());
        String yechye1b = null;
        String rcstrs1b = null;
        if (aa > 7) {
            yechye1b = "";
            rcstrs1b = "";
        } else {
            yechye1b = aa + "";
            rcstrs1b = aa + "";
        }
        String no1 = this.boidbrno.getColumnData();
        String bm1 = no1.substring(0, 3);
        String no2 = this.boidbrno.getColumnData();
        String bm2 = no2.substring(0, 3);
        this.sb.setLength(0);
        this.sb.append(this.isidisid.getColumnData() + ",");
        this.sb.append(this.cinocsno.getColumnData() + ",");
        this.sb.append(this.idtycftp.getColumnData() + ",");
        this.sb.append(this.idnocfno.getColumnData() + ",");
        this.sb.append("1,");
        this.sb.append(this.idnanm5b.getColumnData() + ",");
        this.sb.append(isdtdate + ",");
        this.sb.append(exdtdate + ",");
        this.sb.append("156,");
        this.sb.append(this.isouisbu.getColumnData() + ",");
        this.sb.append(yechye1b + ",");
        this.sb.append(rcstrs1b + ",");
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

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/ODSDB/BCFMCIDI/BCFMCIDI.txt"));
        BCFMCIDI report = new BCFMCIDI("transaction");
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