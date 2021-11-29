package datagenerator.transaction;


import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;

public class EN_RL_LEGAL_REP_INFO extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    private ColumnDataGen cust_isn;

    private ColumnDataGen cust_id;

    private ColumnDataGen rlt_cust_isn;

    private ColumnDataGen buopstaf;

    private ColumnDataGen budidate;

    private DateTime butsstam;

    private ColumnDataGen boidbrno;

    public void init() {
        this.cust_isn = (ColumnDataGen)new RandomNumber("cust_isn", 8);
        this.cust_id = (ColumnDataGen)new RandomNumber("cust_id", 11);
        this.budidate = (ColumnDataGen)new DateYYYYMMDD("budidate");
        this.butsstam = new DateTime("butsstam");
        this.boidbrno = (ColumnDataGen)new RandomNumber("boidbrno", 6);
        this.buopstaf = (ColumnDataGen)new RandomNumber("buopstaf", 4);
    }

    public String nextRecord() {
        String date1 = this.budidate.getColumnData();
        int date2 = Integer.parseInt(date1) + (int)(Math.random() * 2.0D + 1.0D) * 10000;
        int date3 = date2 + (int)(Math.random() * 2.0D + 1.0D) * 10000;
        String no1 = this.boidbrno.getColumnData();
        String bm1 = no1.substring(0, 3);
        String no2 = this.boidbrno.getColumnData();
        String bm2 = no2.substring(0, 3);
        this.sb.setLength(0);
        this.sb.append("820" + this.cust_isn.getColumnData() + ",");
        this.sb.append(this.cust_id.getColumnData() + ",");
        this.sb.append("810" + this.cust_isn.getColumnData() + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date1) + ",");
        this.sb.append(bm1 + this.buopstaf.getColumnData() + ",");
        this.sb.append(no1 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date2 + "") + ",");
        this.sb.append(bm2 + this.buopstaf.getColumnData() + ",");
        this.sb.append(no2 + ",");
        this.sb.append(date3 + ",");
        this.sb.append("0");
        return this.sb.toString();
    }

    public String getTableName() {
        return super.getTableName();
    }

    public EN_RL_LEGAL_REP_INFO(String tableName) {
        super(tableName);
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/XDZX/EN_RL_LEGAL_REP_INFO/EN_RL_LEGAL_REP_INFO.txt"));
        EN_RL_LEGAL_REP_INFO report = new EN_RL_LEGAL_REP_INFO("transaction");
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
