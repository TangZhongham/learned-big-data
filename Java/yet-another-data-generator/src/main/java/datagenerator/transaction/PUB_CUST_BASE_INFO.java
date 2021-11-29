package datagenerator.transaction;


import datagenerator.common.columngenerator.Address;
import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.EnumValue;
import datagenerator.common.columngenerator.IdentityCard;
import datagenerator.common.columngenerator.Name;
import datagenerator.common.columngenerator.PhoneNumber;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;

public class PUB_CUST_BASE_INFO extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    private ColumnDataGen cust_isn;

    private ColumnDataGen cust_id;

    private ColumnDataGen cust_files_id;

    private ColumnDataGen cust_nam;

    private ColumnDataGen cust_cate;

    private ColumnDataGen ctc_tel;

    private ColumnDataGen address;

    private ColumnDataGen zip;

    private ColumnDataGen crd_grd;

    private ColumnDataGen cust_sts;

    private ColumnDataGen cust_flg;

    private ColumnDataGen ltc_cust_update_flg;

    private ColumnDataGen buopstaf;

    private ColumnDataGen budidate;

    private DateTime butsstam;

    private ColumnDataGen boidbrno;

    private ColumnDataGen addr_adm_district;

    private ColumnDataGen work;

    private ColumnDataGen bel_to_idy;

    private ColumnDataGen reg_cap;

    public void init() {
        super.init();
        String[] reg_cap1 = { "", "0" };
        String[] crd_grd1 = { "1", "2", "3", "4" };
        String[] cust_sts1 = { "", "1" };
        String[] cust_flg1 = { "1", "2" };
        String[] ltc_cust_update_flg1 = { "0", "1" };
        this.cust_isn = (ColumnDataGen)new RandomNumber("cust_isn", 8);
        this.cust_id = (ColumnDataGen)new IdentityCard("cust_id", "19700101", "19800101");
        this.cust_nam = (ColumnDataGen)new Name("cust_nam");
        this.ctc_tel = (ColumnDataGen)new PhoneNumber("ctc_tel");
        this.address = (ColumnDataGen)new Address("address");
        this.zip = (ColumnDataGen)new RandomNumber("zip", 6);
        this.crd_grd = (ColumnDataGen)new EnumValue("crd_grd", crd_grd1);
        this.cust_sts = (ColumnDataGen)new EnumValue("cust_sts", cust_sts1);
        this.cust_flg = (ColumnDataGen)new EnumValue("cust_flg", cust_flg1);
        this.ltc_cust_update_flg = (ColumnDataGen)new EnumValue("ltc_cust_update_flg", ltc_cust_update_flg1);
        this.buopstaf = (ColumnDataGen)new RandomNumber("buopstaf", 4);
        this.budidate = (ColumnDataGen)new DateYYYYMMDD("budidate");
        this.butsstam = new DateTime("butsstam");
        this.boidbrno = (ColumnDataGen)new RandomNumber("boidbrno", 6);
        this.addr_adm_district = (ColumnDataGen)new RandomNumber("addr_adm_district", 9);
        this.work = (ColumnDataGen)new RandomInt("work", 10, 1000);
        this.bel_to_idy = (ColumnDataGen)new RandomInt("bel_to_idy", 10, 100);
        this.reg_cap = (ColumnDataGen)new EnumValue("reg_cap", reg_cap1);
    }

    public String nextRecord() {
        String date1 = this.budidate.getColumnData();
        int date2 = Integer.parseInt(date1) + (int)(Math.random() * 2.0D + 1.0D) * 10000;
        int date3 = date2 + (int)(Math.random() * 2.0D + 1.0D) * 10000;
        String no1 = this.boidbrno.getColumnData();
        String bm1 = no1.substring(0, 3);
        String no2 = this.boidbrno.getColumnData();
        String bm2 = no2.substring(0, 3);
        String id = this.cust_id.getColumnData();
        String phone = this.ctc_tel.getColumnData();
        this.sb.setLength(0);
        this.sb.append("810" + this.cust_isn.getColumnData() + ",");
        this.sb.append(id + ",");
        this.sb.append(id + ",");
        this.sb.append(this.cust_nam.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append("1,");
        this.sb.append("156,");
        this.sb.append(this.cust_nam.getColumnData() + ",");
        this.sb.append(phone + ",");
        this.sb.append("1,");
        this.sb.append(phone + ",");
        this.sb.append("2,");
        this.sb.append(this.address.getColumnData() + ",");
        this.sb.append("1,");
        this.sb.append(this.zip.getColumnData() + ",");
        this.sb.append(this.crd_grd.getColumnData() + ",");
        this.sb.append(this.cust_sts.getColumnData() + ",");
        this.sb.append(this.cust_flg.getColumnData() + ",");
        this.sb.append(this.ltc_cust_update_flg.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date1) + ",");
        this.sb.append(bm1 + this.buopstaf.getColumnData() + ",");
        this.sb.append(bm1 + this.buopstaf.getColumnData() + ",");
        this.sb.append(no1 + ",");
        this.sb.append(this.butsstam.getDateTimefromYYYYMMDD(date2 + "") + ",");
        this.sb.append(bm2 + this.buopstaf.getColumnData() + ",");
        this.sb.append(no2 + ",");
        this.sb.append("0,");
        this.sb.append(",");
        this.sb.append(",");
        this.sb.append(this.cust_flg.getColumnData() + ",");
        this.sb.append(this.work.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(this.reg_cap.getColumnData() + ",");
        this.sb.append(this.bel_to_idy.getColumnData() + ",");
        this.sb.append(",");
        this.sb.append(date3 + ",");
        this.sb.append("0");
        return this.sb.toString();
    }

    public String getTableName() {
        return super.getTableName();
    }

    public PUB_CUST_BASE_INFO(String tableName) {
        super(tableName);
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/XDZX/PUB_CUST_BASE_INFO/PUB_CUST_BASE_INFO.txt"));
        PUB_CUST_BASE_INFO report = new PUB_CUST_BASE_INFO("transaction");
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
