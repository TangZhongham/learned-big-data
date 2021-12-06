package datagenerator.transaction;


import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.EnumValue;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;

public class EN_FNC_CUST_ECIF extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    private ColumnDataGen cust_isn;

    private ColumnDataGen bel_org;

    private ColumnDataGen cust_id;

    private ColumnDataGen cust_id1;

    private ColumnDataGen idpd_cpt_flg;

    private ColumnDataGen fnc_unt_ppt;

    private ColumnDataGen bank_typ;

    private ColumnDataGen boidbrno;

    private ColumnDataGen bdidbrbm;

    private ColumnDataGen buopstaf;

    private ColumnDataGen budidate;

    private DateTime butsstam;

    private ColumnDataGen boidbrno1;

    public void init() {
        super.init();
        String[] fnc_unt_ppt1 = { "Xr", "ib", "ig" };
        String[] bank_typ1 = {
                "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
                "61", "62", "63", "64", "65", "9" };
        this.cust_isn = (ColumnDataGen)new RandomNumber("cust_isn", 7);
        this.bel_org = (ColumnDataGen)new RandomInt("bel_org", 10, 200);
        this.cust_id = (ColumnDataGen)new RandomNumber("cust_id", 9);
        this.cust_id1 = (ColumnDataGen)new RandomInt("cust_id1", 10, 10);
        this.idpd_cpt_flg = (ColumnDataGen)new RandomInt("idpd_cpt_flg", 10, 2);
        this.fnc_unt_ppt = (ColumnDataGen)new EnumValue("fnc_unt_ppt", fnc_unt_ppt1);
        this.bank_typ = (ColumnDataGen)new EnumValue("bank_typ", bank_typ1);
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
        this.sb.setLength(0);
        this.sb.append("8100" + this.cust_isn.getColumnData() + ",");
        this.sb.append(bm1 + "000,");
        this.sb.append("20" + this.cust_id.getColumnData() + "-" + this.cust_id1.getColumnData() + ",");
        this.sb.append(this.idpd_cpt_flg.getColumnData() + ",");
        this.sb.append(this.fnc_unt_ppt.getColumnData() + ",");
        this.sb.append(this.bank_typ.getColumnData() + ",");
        this.sb.append(this.cust_id1.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date1) + ",");
        this.sb.append(yg1 + ",");
        this.sb.append(bm1_3 + ",");
        this.sb.append(yg2 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date2 + "") + ",");
        this.sb.append(bm2_3 + ",");
        this.sb.append(date3 + ",");
        this.sb.append("0");
        return this.sb.toString();
    }

    public String getTableName() {
        return super.getTableName();
    }

    public EN_FNC_CUST_ECIF(String tableName) {
        super(tableName);
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/XDZX/EN_FNC_CUST_ECIF/EN_FNC_CUST_ECIF.txt"));
        EN_FNC_CUST_ECIF report = new EN_FNC_CUST_ECIF("transaction");
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