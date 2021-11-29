package datagenerator.transaction;


import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.DateYYYYMMDD1;
import datagenerator.common.columngenerator.EnumValue;
import datagenerator.common.columngenerator.IdentityCard;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;

public class IND_CUST_EXPD_ECIF extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    private ColumnDataGen cust_isn;

    private ColumnDataGen bel_org;

    private ColumnDataGen cust_id;

    private ColumnDataGen nty;

    private ColumnDataGen mrg;

    private ColumnDataGen btd;

    private ColumnDataGen study_exp;

    private ColumnDataGen degree;

    private ColumnDataGen wk_unit;

    private ColumnDataGen duty;

    private ColumnDataGen year_income;

    private ColumnDataGen email;

    private ColumnDataGen cust_qly;

    private ColumnDataGen invest;

    private ColumnDataGen time;

    private DateTime crt_tm;

    private ColumnDataGen crt_organ;

    private ColumnDataGen last_organ;

    private ColumnDataGen ind_buss;

    private ColumnDataGen unit_kind;

    private ColumnDataGen cust_lev;

    private ColumnDataGen bank_emp_ind;

    public void init() {
        String[] nty1 = { "99", "1" };
        String[] mrg1 = { "20", "90" };
        String[] study_exp1 = { "999", "90", "30" };
        String[] unit_kind1 = { "AA", "" };
        String[] bank_emp_ind1 = { "0", "" };
        this.cust_isn = (ColumnDataGen)new RandomNumber("cust_isn", 11);
        this.bel_org = (ColumnDataGen)new RandomNumber("bel_org", 3);
        this.cust_id = (ColumnDataGen)new IdentityCard("cust_id", "19800909", "19900908");
        this.nty = (ColumnDataGen)new EnumValue("nty", nty1);
        this.mrg = (ColumnDataGen)new EnumValue("mrg", mrg1);
        this.btd = (ColumnDataGen)new DateYYYYMMDD1("btd");
        this.study_exp = (ColumnDataGen)new EnumValue("study_exp", study_exp1);
        this.degree = (ColumnDataGen)new RandomInt("degree", 10, 6);
        this.duty = (ColumnDataGen)new RandomInt("duty", 10, 10);
        this.year_income = (ColumnDataGen)new RandomInt("year_income", 10, 20);
        this.cust_qly = (ColumnDataGen)new RandomInt("cust_qly", 10, 100);
        this.invest = (ColumnDataGen)new RandomNumber("invest", 4);
        this.time = (ColumnDataGen)new DateYYYYMMDD("time");
        this.crt_tm = new DateTime("crt_tm");
        this.unit_kind = (ColumnDataGen)new EnumValue("unit_kind", unit_kind1);
        this.cust_lev = (ColumnDataGen)new RandomInt("cust_lev", 4, 4);
        this.bank_emp_ind = (ColumnDataGen)new EnumValue("bank_emp_ind", bank_emp_ind1);
        this.ind_buss = (ColumnDataGen)new RandomInt("ind_buss", 1, 1);
    }

    public String nextRecord() {
        String date1 = this.time.getColumnData();
        int date2 = Integer.parseInt(date1) + (int)(Math.random() * 4.0D + 1.0D) * 10000;
        int date3 = date2 + (int)(Math.random() * 2.0D + 1.0D) * 10000;
        String bel_org1 = this.bel_org.getColumnData();
        this.sb.setLength(0);
        this.sb.append(this.cust_isn.getColumnData() + ",");
        this.sb.append(bel_org1 + "000,");
        this.sb.append(this.cust_id.getColumnData() + "000,");
        this.sb.append(this.nty.getColumnData() + ",");
        this.sb.append(this.mrg.getColumnData() + ",");
        this.sb.append(this.btd.getColumnData() + ",");
        this.sb.append(this.study_exp.getColumnData() + ",");
        this.sb.append(this.degree.getColumnData() + ",");
        this.sb.append(" ,");
        this.sb.append(" ,");
        this.sb.append(this.unit_kind.getColumnData() + ",");
        this.sb.append(" ,");
        this.sb.append(this.duty.getColumnData() + ",");
        this.sb.append(this.duty.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(this.year_income.getColumnData() + "0000,");
        this.sb.append(this.degree.getColumnData() + ",");
        this.sb.append("1,");
        this.sb.append(this.cust_lev.getColumnData() + ",");
        this.sb.append(" ,");
        this.sb.append(this.bank_emp_ind.getColumnData() + ",");
        this.sb.append(this.cust_lev.getColumnData() + ",");
        this.sb.append(this.bank_emp_ind.getColumnData() + ",");
        this.sb.append(this.bank_emp_ind.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append("11,");
        this.sb.append(this.cust_qly.getColumnData() + ",");
        this.sb.append(this.cust_qly.getColumnData() + ",");
        this.sb.append(this.bank_emp_ind.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(bel_org1 + this.invest.getColumnData() + ",");
        this.sb.append(this.crt_tm.getDateTimefromYYYYMMDD(date1) + ",");
        this.sb.append(bel_org1 + this.invest.getColumnData() + ",");
        this.sb.append(bel_org1 + this.bel_org.getColumnData() + ",");
        this.sb.append(bel_org1 + this.bel_org.getColumnData() + ",");
        this.sb.append(bel_org1 + this.invest.getColumnData() + ",");
        this.sb.append(this.crt_tm.getDateTimefromYYYYMMDD(date2 + "") + ",");
        this.sb.append(this.degree.getColumnData() + ",");
        this.sb.append(this.year_income.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(this.bank_emp_ind.getColumnData() + ",");
        this.sb.append(this.ind_buss.getColumnData() + ",");
        this.sb.append(date3 + ",");
        this.sb.append("0");
        return this.sb.toString();
    }

    public String getTableName() {
        return super.getTableName();
    }

    public IND_CUST_EXPD_ECIF(String tableName) {
        super(tableName);
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/XDZX/IND_CUST_EXPD_ECIF/IND_CUST_EXPD_ECIF.txt"));
        IND_CUST_EXPD_ECIF report = new IND_CUST_EXPD_ECIF("transaction");
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