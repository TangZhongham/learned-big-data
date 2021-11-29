package datagenerator.transaction;


import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.PhoneNumber;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;

public class EN_RL_PER_INFO extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    private ColumnDataGen id;

    private ColumnDataGen cust_isn;

    private ColumnDataGen cust_id;

    private ColumnDataGen cust_id_1;

    private ColumnDataGen mobile;

    private ColumnDataGen tel;

    private ColumnDataGen boidbrno;

    private ColumnDataGen bdidbrbm;

    private ColumnDataGen buopstaf;

    private ColumnDataGen budidate;

    private DateTime butsstam;

    private ColumnDataGen fathfa1b;

    private ColumnDataGen boidbrno1;

    public void init() {
        super.init();
        this.id = (ColumnDataGen)new RandomNumber("id", 6);
        this.cust_isn = (ColumnDataGen)new RandomNumber("cust_isn", 7);
        this.cust_id = (ColumnDataGen)new RandomNumber("cust_id", 9);
        this.cust_id_1 = (ColumnDataGen)new RandomInt("cust_id", 10, 10);
        this.mobile = (ColumnDataGen)new PhoneNumber("mobile");
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
        this.sb.append(this.id.getColumnData() + ",");
        this.sb.append("8100" + this.cust_isn.getColumnData() + ",");
        this.sb.append(bm1 + "000,");
        this.sb.append(this.cust_id.getColumnData() + "-" + this.cust_id_1.getColumnData() + ",");
        this.sb.append("1,");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append("8100" + this.cust_isn.getColumnData() + ",");
        this.sb.append(this.mobile.getColumnData() + ",");
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

    public EN_RL_PER_INFO(String tableName) {
        super(tableName);
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/XDZX/EN_RL_PER_INFO/EN_RL_PER_INFO.txt"));
        EN_RL_PER_INFO report = new EN_RL_PER_INFO("transaction");
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