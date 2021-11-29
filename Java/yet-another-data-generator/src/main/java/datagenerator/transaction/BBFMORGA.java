package datagenerator.transaction;


import datagenerator.common.columngenerator.Address;
import datagenerator.common.columngenerator.ColumnDataGen;
import datagenerator.common.columngenerator.DateTime;
import datagenerator.common.columngenerator.DateYYYYMMDD;
import datagenerator.common.columngenerator.EnumValue;
import datagenerator.common.columngenerator.Name;
import datagenerator.common.columngenerator.PhoneNumber;
import datagenerator.common.columngenerator.RandomInt;
import datagenerator.common.columngenerator.RandomNumber;
import datagenerator.common.columngenerator.RandomString;
import datagenerator.common.tablegenerator.AbstractTableDataGen;
import java.io.IOException;

public class BBFMORGA extends AbstractTableDataGen {
    StringBuffer sb = new StringBuffer();

    private ColumnDataGen orcabkfg;

    private ColumnDataGen orcabrno;

    private ColumnDataGen orcaflnm;

    private ColumnDataGen orcanm30;

    private ColumnDataGen orcaaddr;

    private ColumnDataGen orcben80;

    private ColumnDataGen orcaad80;

    private ColumnDataGen orccbool;

    private ColumnDataGen orcabool;

    private ColumnDataGen orcbbool;

    private ColumnDataGen orcabrlv;

    private ColumnDataGen orcabrt1;

    private ColumnDataGen orcaebno;

    private ColumnDataGen orcaatdr;

    private ColumnDataGen orcaipad;

    private ColumnDataGen orccbrno;

    private ColumnDataGen orcdbrno;

    private ColumnDataGen orcebrno;

    private ColumnDataGen orczbrno;

    private ColumnDataGen orcfbrno;

    private ColumnDataGen orcgbrno;

    private ColumnDataGen orchbrno;

    private ColumnDataGen orcapltr;

    private ColumnDataGen orcibrno;

    private ColumnDataGen orcanial;

    private ColumnDataGen orcabskd;

    private ColumnDataGen orcasslv;

    private ColumnDataGen orcfbool;

    private ColumnDataGen orcabrft;

    private ColumnDataGen orcatime;

    private ColumnDataGen orcbtime;

    private ColumnDataGen orcaftxt;

    private ColumnDataGen orcafxbr;

    private ColumnDataGen orcafxbn;

    private ColumnDataGen orcacetp;

    private ColumnDataGen orcacstm;

    private ColumnDataGen orcadate;

    private ColumnDataGen orcbdate;

    private ColumnDataGen orchbool;

    private ColumnDataGen orcazpcd;

    private ColumnDataGen orcanm40;

    private ColumnDataGen orcateln;

    private ColumnDataGen orcabrno_end;

    private DateTime orcastam;

    private ColumnDataGen orcastaf;

    private ColumnDataGen orcedate;

    private ColumnDataGen orcbstam;

    private ColumnDataGen orcbstaf;

    private ColumnDataGen last_etl_acg_dt;

    private ColumnDataGen del_f;

    private ColumnDataGen orcabrno_1;

    private ColumnDataGen orcapoft;

    public BBFMORGA(String tableName) {
        super(tableName);
    }

    public void init() {
        String[] orcapoft1 = { "1  0", "1  1", "1 11", "1 2 1" };
        String[] orcabrno1 = { "8010", "9990", "8530", "8014", "8530", "8210", "8520", "8960", "9090" };
        String[] bankName = { ", ", ", ", ", ", ", "};
        this.orcabkfg = (ColumnDataGen)new RandomString("orcabkfg", 1);
        this.orcabrno = (ColumnDataGen)new RandomNumber("orcabrno", 6);
        this.orcaflnm = (ColumnDataGen)new EnumValue("orcaflnm", bankName);
        this.orcabrno_1 = (ColumnDataGen)new EnumValue("orcabrno_end", orcabrno1);
        this.orcabrno_end = (ColumnDataGen)new RandomNumber("orcabrno_end", 2);
        this.orcaaddr = (ColumnDataGen)new Address("orcaaddr");
        this.orccbool = (ColumnDataGen)new RandomInt("orccbool", 3, 2);
        this.orcapoft = (ColumnDataGen)new EnumValue("orcapoft", orcapoft1);
        this.orcacetp = (ColumnDataGen)new RandomInt("orcacetp", 3, 3);
        this.orcabrlv = (ColumnDataGen)new RandomInt("orcabrlv", 3, 10);
        this.orcabrt1 = (ColumnDataGen)new RandomInt("orcabrt1", 3, 10);
        this.orcaatdr = (ColumnDataGen)new RandomInt("orcaatdr", 3, 10);
        this.orcatime = (ColumnDataGen)new RandomNumber("orcatime", 5);
        this.orcbtime = (ColumnDataGen)new RandomNumber("orcbtime", 6);
        this.orcafxbr = (ColumnDataGen)new RandomNumber("orcafxbr", 6);
        this.orcafxbn = (ColumnDataGen)new RandomNumber("orcafxbn", 3);
        this.orcacstm = (ColumnDataGen)new RandomInt("orcacstm", 2, 2);
        this.orcadate = (ColumnDataGen)new DateYYYYMMDD("orcadate");
        this.orchbool = (ColumnDataGen)new RandomInt("orchbool", 10, 2);
        this.orcazpcd = (ColumnDataGen)new RandomNumber("orcazpcd", 6);
        this.orcanm40 = (ColumnDataGen)new Name("orcanm40");
        this.orcateln = (ColumnDataGen)new PhoneNumber("orcateln");
        this.orcastam = new DateTime("orcastam");
        this.orcastaf = (ColumnDataGen)new RandomString("orcastaf", 7);
    }

    public String nextRecord() {
        String bankName = this.orcaflnm.getColumnData();
        String bankNum = this.orcabrno_1.getColumnData();
        String orcabrnoEnd1 = null;
        int orcabrnoEnd = Integer.parseInt(this.orcabrno_end.getColumnData());
        if (orcabrnoEnd > 97) {
            orcabrnoEnd1 = "00";
        } else {
            orcabrnoEnd1 = orcabrnoEnd + "";
        }
        int orcabool1 = Integer.parseInt(this.orccbool.getColumnData());
        int orcacetp1 = orcabool1 + 1;
        int date1 = Integer.parseInt(this.orcadate.getColumnData());
        int date2 = date1 + (int)(Math.random() * 10.0D + 1.0D) * 10000;
        int date4 = date2 + (int)(Math.random() * 10.0D + 1.0D) * 10000;
        int isNewSys = Integer.parseInt(this.orchbool.getColumnData());
        String phone = this.orcateln.getColumnData();
        String date3 = null;
        if (isNewSys == 0) {
            date3 = "0";
        } else {
            date3 = date1 + "";
        }
        this.sb.setLength(0);
        this.sb.append(this.orcabkfg.getColumnData() + ",");
        this.sb.append(bankNum + orcabrnoEnd1 + ",");
        this.sb.append(bankName + ",");
        this.sb.append(bankName + ",");
        this.sb.append(this.orcaaddr.getColumnData() + ",");
        this.sb.append(" ,");
        this.sb.append(" ,");
        this.sb.append("0,");
        this.sb.append(orcabool1 + ",");
        this.sb.append(orcabool1 + ",");
        this.sb.append(orcacetp1 + ",");
        this.sb.append(this.orcabrlv.getColumnData() + ",");
        this.sb.append(this.orcabrt1.getColumnData() + ",");
        this.sb.append(bankNum.substring(0, 3) + ",");
        this.sb.append("0" + this.orcaatdr.getColumnData() + ",");
        this.sb.append(" ,");
        this.sb.append(bankNum.substring(0, 3) + "000,");
        this.sb.append(bankNum.substring(0, 3) + "000,");
        this.sb.append(this.orcabrno.getColumnData() + ",");
        this.sb.append(this.orcabrno.getColumnData() + ",");
        this.sb.append(bankNum.substring(0, 3) + this.orcafxbn.getColumnData() + ",");
        this.sb.append(" ,");
        this.sb.append(" ,");
        this.sb.append("1,");
        this.sb.append(" ,");
        this.sb.append("1,");
        this.sb.append(this.orccbool.getColumnData() + ",");
        this.sb.append(this.orcabrlv.getColumnData() + ",");
        this.sb.append("0,");
        this.sb.append(" ,");
        this.sb.append(this.orcapoft.getColumnData() + ",");
        this.sb.append(this.orcatime.getColumnData() + ",");
        this.sb.append(this.orcbtime.getColumnData() + ",");
        this.sb.append("1,");
        this.sb.append(this.orcafxbr.getColumnData() + ",");
        this.sb.append(this.orcafxbn.getColumnData() + ",");
        this.sb.append(" ,");
        this.sb.append(" ,");
        this.sb.append(" ,");
        this.sb.append(" ,");
        this.sb.append(" ,");
        this.sb.append(" ,");
        this.sb.append(this.orcacstm.getColumnData() + ",");
        this.sb.append(date1 + ",");
        this.sb.append(date2 + ",");
        this.sb.append(this.orchbool.getColumnData() + ",");
        this.sb.append(date3 + ",");
        this.sb.append(" ,");
        this.sb.append(this.orcazpcd.getColumnData() + ",");
        this.sb.append(this.orcanm40.getColumnData() + ",");
        this.sb.append(phone + ",");
        this.sb.append(phone + ",");
        this.sb.append(phone + ",");
        this.sb.append(phone + ",");
        this.sb.append("1,");
        this.sb.append("1,");
        this.sb.append(" ,");
        this.sb.append(date1 + ",");
        this.sb.append(this.orcastam.getDateTimefromYYYYMMDD(date1 + "") + ",");
        this.sb.append(this.orcastaf.getColumnData() + ",");
        this.sb.append(date2 + ",");
        this.sb.append(this.orcastam.getDateTimefromYYYYMMDD(date2 + "") + ",");
        this.sb.append(this.orcastaf.getColumnData() + ",");
        this.sb.append(date4 + ",");
        this.sb.append("0");
        return this.sb.toString();
    }

    public String getTableName() {
        return super.getTableName();
    }

    public void generateData(String path, int nums) throws IOException {
        int recordNums = Integer.valueOf(nums).intValue();
        HDFS_Out o = new HDFS_Out(String.valueOf("/tmp/data/ODSDB/BBFMORGA/BBFMORGA.txt"));
        BBFMORGA report = new BBFMORGA("transaction");
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

