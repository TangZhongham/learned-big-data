package datagenerator.transaction;


import datagenerator.columngenerator.Address;
import datagenerator.columngenerator.ColumnDataGen;
import datagenerator.columngenerator.DateTime;
import datagenerator.columngenerator.DateYYYYMMDD1;
import datagenerator.columngenerator.RandomInt;
import datagenerator.columngenerator.RandomNumber;
import datagenerator.columngenerator.RandomString;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;
import java.text.DecimalFormat;

public class BCFMCADI extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    DecimalFormat df = new DecimalFormat("#.00");

    private ColumnDataGen isidisid;

    private ColumnDataGen cinocsno;

    private ColumnDataGen adsnptsn;

    private ColumnDataGen adtyadtp;

    private ColumnDataGen adarrgnm;

    private ColumnDataGen adi1addr;

    private ColumnDataGen custst16;

    private ColumnDataGen boidbrno;

    private ColumnDataGen buopstaf;

    private ColumnDataGen budidate;

    private DateTime butsstam;

    public void init() {
        this.isidisid = (ColumnDataGen)new RandomString("isidisid", 1);
        this.cinocsno = (ColumnDataGen)new RandomNumber("cinocsno", 11);
        this.adsnptsn = (ColumnDataGen)new RandomInt("adsnptsn", 10, 10);
        this.adtyadtp = (ColumnDataGen)new RandomInt("adtyadtp", 10, 10);
        this.adarrgnm = (ColumnDataGen)new RandomNumber("adarrgnm", 6);
        this.adi1addr = (ColumnDataGen)new Address("adi1addr");
        this.custst16 = (ColumnDataGen)new RandomNumber("custst16", 16);
        this.boidbrno = (ColumnDataGen)new RandomNumber("boidbrno", 6);
        this.buopstaf = (ColumnDataGen)new RandomNumber("buopstaf", 4);
        this.budidate = (ColumnDataGen)new DateYYYYMMDD1("budidate");
        this.butsstam = new DateTime("butsstam");
    }

    public String nextRecord() {
        String date1 = this.budidate.getColumnData();
        int date2 = Integer.parseInt(date1) + (int)(Math.random() * 4.0D + 1.0D) * 10000;
        int date3 = date2 + (int)(Math.random() * 2.0D + 1.0D) * 10000;
        int aa = Integer.parseInt(this.adsnptsn.getColumnData());
        String pzipzpcd = null;
        String custst161 = null;
        if (aa > 5) {
            pzipzpcd = "";
            custst161 = "0";
        } else {
            pzipzpcd = this.adarrgnm.getColumnData();
            custst161 = this.custst16.getColumnData();
        }
        String no1 = this.boidbrno.getColumnData();
        String bm1 = no1.substring(0, 3);
        String no2 = this.boidbrno.getColumnData();
        String bm2 = no2.substring(0, 3);
        this.sb.setLength(0);
        this.sb.append(this.isidisid.getColumnData() + ",");
        this.sb.append(this.cinocsno.getColumnData() + ",");
        this.sb.append(this.adsnptsn.getColumnData() + ",");
        this.sb.append(this.adsnptsn.getColumnData() + ",");
        this.sb.append(this.adarrgnm.getColumnData() + ",");
        this.sb.append(this.adi1addr.getColumnData() + ",");
        this.sb.append(" ,");
        this.sb.append(" ,");
        this.sb.append(pzipzpcd + ",");
        this.sb.append(" ,");
        this.sb.append(custst161 + ",");
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

    public BCFMCADI(String tableName) {
        super(tableName);
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/ODSDB/BCFMCADI/BCFMCADI.txt"));
        BCFMCADI report = new BCFMCADI("transaction");
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
