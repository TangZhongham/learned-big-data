package datagenerator.transaction;


import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.IdentityCard;
import datagenerator.common.columngenerator.Name;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.columngenerator.RandomString;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;
import java.text.DecimalFormat;

public class BCFMCMBI extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    DecimalFormat df = new DecimalFormat("#.00");

    private ColumnDataGen isidisid;

    private ColumnDataGen cinocsno;

    private ColumnDataGen cuidcsid;

    private ColumnDataGen cunaflnm;

    private ColumnDataGen poadptsn;

    private ColumnDataGen phonptsn;

    private ColumnDataGen cusvcsst;

    private ColumnDataGen aoidbrno;

    private ColumnDataGen cmidstaf;

    private ColumnDataGen custst16;

    private ColumnDataGen budidate;

    private DateTime butsstam;

    public String nextRecord() {
        String date1 = this.budidate.getColumnData();
        int date2 = Integer.parseInt(date1) + (int)(Math.random() * 4.0D + 1.0D) * 10000;
        int date3 = date2 + (int)(Math.random() * 4.0D + 1.0D) * 10000;
        int aa = Integer.parseInt(this.cusvcsst.getColumnData());
        String vityvipt = null;
        String custst16 = null;
        if (aa > 0) {
            vityvipt = "";
            custst16 = "0";
        } else {
            vityvipt = "2";
            custst16 = "1000000000000";
        }
        String no1 = this.aoidbrno.getColumnData();
        String bm1 = no1.substring(0, 3);
        String no2 = this.aoidbrno.getColumnData();
        String bm2 = no2.substring(0, 3);
        this.sb.setLength(0);
        this.sb.append(this.isidisid.getColumnData() + ",");
        this.sb.append(this.cinocsno.getColumnData() + ",");
        this.sb.append("1,");
        this.sb.append(this.cuidcsid.getColumnData() + ",");
        this.sb.append(this.cunaflnm.getColumnData() + ",");
        this.sb.append(" ,");
        this.sb.append(" ,");
        this.sb.append("156,");
        this.sb.append("C,");
        this.sb.append(this.poadptsn.getColumnData() + ",");
        this.sb.append(this.poadptsn.getColumnData() + ",");
        this.sb.append("1,");
        this.sb.append(vityvipt + ",");
        this.sb.append(this.poadptsn.getColumnData() + ",");
        this.sb.append(this.poadptsn.getColumnData() + ",");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append("0,");
        this.sb.append(no1 + ",");
        this.sb.append(bm1 + ",");
        this.sb.append(bm1 + this.cmidstaf.getColumnData() + ",");
        this.sb.append(" ,");
        this.sb.append(" ,");
        this.sb.append(" ,");
        this.sb.append("1,");
        this.sb.append(" ,");
        this.sb.append(custst16 + ",");
        this.sb.append(no1 + ",");
        this.sb.append(bm1 + ",");
        this.sb.append(bm1 + this.cmidstaf.getColumnData() + ",");
        this.sb.append(date1 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date1) + ",");
        this.sb.append(no2 + ",");
        this.sb.append(bm2 + ",");
        this.sb.append(bm2 + this.cmidstaf.getColumnData() + ",");
        this.sb.append(date2 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date2 + "") + ",");
        this.sb.append(date3 + ",");
        this.sb.append("0");
        return this.sb.toString();
    }

    public void init() {
        this.isidisid = (ColumnDataGen)new RandomString("isidisid", 1);
        this.cinocsno = (ColumnDataGen)new RandomNumber("cinocsno", 11);
        this.cuidcsid = (ColumnDataGen)new IdentityCard("cuidcsid", "19700202", "19900909");
        this.cunaflnm = (ColumnDataGen)new Name("cunaflnm");
        this.poadptsn = (ColumnDataGen)new RandomInt("poadptsn", 10, 10);
        this.cusvcsst = (ColumnDataGen)new RandomInt("cusvcsst", 10, 2);
        this.aoidbrno = (ColumnDataGen)new RandomNumber("aoidbrno", 6);
        this.budidate = (ColumnDataGen)new DateYYYYMMDD("budidate");
        this.cmidstaf = (ColumnDataGen)new RandomNumber("cmidstaf", 4);
        this.butsstam = new DateTime("butsstam");
    }

    public String getTableName() {
        return super.getTableName();
    }

    public BCFMCMBI(String columnName) {
        super(columnName);
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/ODSDB/BCFMCMBI/BCFMCMBI.txt"));
        BCFMCMBI report = new BCFMCMBI("transaction");
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