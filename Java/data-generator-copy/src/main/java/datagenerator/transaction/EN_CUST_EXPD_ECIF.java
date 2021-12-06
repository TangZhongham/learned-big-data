package datagenerator.transaction;


import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.EnumValue;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.columngenerator.RandomString;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;

public class EN_CUST_EXPD_ECIF extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    private ColumnDataGen cust_isn;

    private ColumnDataGen bel_org;

    private ColumnDataGen cust_id;

    private ColumnDataGen random01;

    private ColumnDataGen reg_dt;

    private ColumnDataGen reg_tl_dt;

    private ColumnDataGen cie_dt;

    private ColumnDataGen reg_district_typ;

    private ColumnDataGen en_qly;

    private ColumnDataGen fa_rpt_typ;

    private ColumnDataGen cust_typ;

    private ColumnDataGen grp_lev;

    private ColumnDataGen one_null_two;

    private ColumnDataGen zero_one;

    private ColumnDataGen cust_lev;

    private ColumnDataGen zero_null;

    private ColumnDataGen invest;

    private ColumnDataGen crt_tm;

    private ColumnDataGen crt_loan_off;

    private ColumnDataGen boidbrno;

    private ColumnDataGen bdidbrbm;

    private ColumnDataGen buopstaf;

    private ColumnDataGen budidate;

    private DateTime butsstam;

    private ColumnDataGen crd_grd;

    private ColumnDataGen ten;

    private ColumnDataGen one_null;

    public void init() {
        String[] reg_district_typ1 = { " ", "2" };
        String[] one_null_two1 = { "1", "2", "" };
        String[] one_null1 = { "", "1" };
        String[] zero_null1 = { "0", "" };
        this.cust_isn = (ColumnDataGen)new RandomNumber("cust_isn", 8);
        this.bel_org = (ColumnDataGen)new RandomNumber("bel_org", 3);
        this.cust_id = (ColumnDataGen)new RandomNumber("cust_id", 11);
        this.ten = (ColumnDataGen)new RandomInt("ten", 10, 9);
        this.zero_one = (ColumnDataGen)new RandomInt("zero_one", 10, 2);
        this.reg_dt = (ColumnDataGen)new DateYYYYMMDD("reg_dt");
        this.reg_district_typ = (ColumnDataGen)new EnumValue("reg_district_typ", reg_district_typ1);
        this.en_qly = (ColumnDataGen)new RandomInt("en_qly", 10, 5);
        this.one_null_two = (ColumnDataGen)new EnumValue("one_null_two", one_null_two1);
        this.cust_typ = (ColumnDataGen)new RandomString("cust_typ", 2);
        this.one_null = (ColumnDataGen)new EnumValue("one_null", one_null1);
        this.zero_null = (ColumnDataGen)new EnumValue("zero_null", zero_null1);
        this.invest = (ColumnDataGen)new RandomNumber("invest", 4);
        this.boidbrno = (ColumnDataGen)new RandomNumber("boidbrno", 6);
        this.buopstaf = (ColumnDataGen)new RandomNumber("buopstaf", 4);
        this.budidate = (ColumnDataGen)new DateYYYYMMDD("budidate");
        this.butsstam = new DateTime("butsstam");
        this.crd_grd = (ColumnDataGen)new RandomInt("crd_grd", 7, 7);
    }

    public String nextRecord() {
        String date1 = this.budidate.getColumnData();
        int date2 = Integer.parseInt(date1) + (int)(Math.random() * 1.0D + 1.0D) * 10000;
        int date3 = date2 + (int)(Math.random() * 1.0D + 1.0D) * 10000;
        int date4 = date3 + (int)(Math.random() * 1.0D + 1.0D) * 10000;
        int date5 = date4 + (int)(Math.random() * 1.0D + 1.0D) * 10000;
        int date6 = date5 + (int)(Math.random() * 1.0D + 1.0D) * 10000;
        String no1 = this.boidbrno.getColumnData();
        String bm1 = no1.substring(0, 3);
        String no2 = this.boidbrno.getColumnData();
        String bm2 = no2.substring(0, 3);
        Boolean a = Boolean.valueOf((Integer.parseInt(this.zero_one.getColumnData()) == 0));
        this.sb.setLength(0);
        this.sb.append("820" + this.cust_isn.getColumnData() + ",");
        this.sb.append(this.bel_org.getColumnData() + "000,");
        this.sb.append(this.cust_id.getColumnData() + "-" + this.ten.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(",");
        if (!a.booleanValue()) {
            this.sb.append(date1 + ",");
            this.sb.append("CNY,");
        } else {
            this.sb.append(",,");
        }
        this.sb.append(date2 + ",");
        this.sb.append(this.one_null_two.getColumnData() + ",");
        this.sb.append(date3 + ",");
        this.sb.append(this.en_qly.getColumnData() + ",");
        this.sb.append("0,");
        this.sb.append(this.one_null_two.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append("0,");
        this.sb.append(this.one_null.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(this.zero_null.getColumnData() + ",");
        this.sb.append(this.cust_typ.getColumnData() + ",");
        this.sb.append(this.one_null.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append("0,");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(this.one_null_two.getColumnData() + ",");
        this.sb.append(this.zero_null.getColumnData() + ",");
        this.sb.append(this.zero_null.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(this.zero_null.getColumnData() + ",");
        this.sb.append(this.zero_null.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(this.zero_null.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append("876" + this.invest.getColumnData() + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date4 + "") + ",");
        this.sb.append(bm1 + this.buopstaf.getColumnData() + ",");
        this.sb.append(no1 + ",");
        this.sb.append(bm2 + this.buopstaf.getColumnData() + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date5 + "") + ",");
        this.sb.append(no2 + ",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(this.crd_grd.getColumnData() + ",");
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
        this.sb.append(",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date6 + "") + ",");
        this.sb.append("0");
        return this.sb.toString();
    }

    public String getTableName() {
        return super.getTableName();
    }

    public EN_CUST_EXPD_ECIF(String tableName) {
        super(tableName);
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/XDZX/EN_CUST_EXPD_ECIF/EN_CUST_EXPD_ECIF.txt"));
        EN_CUST_EXPD_ECIF report = new EN_CUST_EXPD_ECIF("transaction");
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