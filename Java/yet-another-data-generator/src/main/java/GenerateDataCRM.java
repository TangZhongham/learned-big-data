
import datagenerator.transaction.AFFMYRPT;
import datagenerator.transaction.BBFMDJZL;
import datagenerator.transaction.BBFMORGA;
import datagenerator.transaction.BCFMCADI;
import datagenerator.transaction.BCFMCDBI;
import datagenerator.transaction.BCFMCIDI;
import datagenerator.transaction.BCFMCMBI;
import datagenerator.transaction.BCFMCPNI;
import datagenerator.transaction.BCFMPCAI;
import datagenerator.transaction.BDFMHQAA;
import datagenerator.transaction.EN_CUST_EXPD_ECIF;
import datagenerator.transaction.EN_FNC_CUST_ECIF;
import datagenerator.transaction.EN_RL_LEGAL_REP_INFO;
import datagenerator.transaction.EN_RL_PER_INFO;
import datagenerator.transaction.IND_CUST_EXPD_ECIF;
import datagenerator.transaction.PUB_CUST_BASE_INFO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class GenerateDataCRM {
    private static Map<String, String> map = new HashMap<>();

    public GenerateDataCRM(String fileName) {
        BufferedReader br = null;
        try {
            File file = new File(fileName);
            InputStream in = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(in, Charset.forName("UTF-8"));
            br = new BufferedReader(reader);
            String line = null;
            while (null != (line = br.readLine())) {
                String[] data = line.split(":");
                String tableName = data[0];
                String dataNums = data[1];
                map.put(tableName, dataNums);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void main(String[] args) throws IOException {
        String filePath = System.getProperty("user.dir") +File.separator+"conf"+File.separator+"CRM_DataGenerate.properties";

        GenerateDataCRM g = new GenerateDataCRM(filePath);
//        GenerateDataCRM g = new GenerateDataCRM("./conf/CRM_DataGenerate.properties");
        int BBFMDJZL_NUMS = Integer.valueOf(map.get("BBFMDJZL")).intValue();
        System.out.println("造数总共：" + BBFMDJZL_NUMS + "条");
        int BBFMORGA_NUMS = Integer.valueOf(map.get("BBFMORGA")).intValue();
        int BCFMCADI_NUMS = Integer.valueOf(map.get("BCFMCADI")).intValue();
        int BCFMCIDI_NUMS = Integer.valueOf(map.get("BCFMCIDI")).intValue();
        int BCFMCMBI_NUMS = Integer.valueOf(map.get("BCFMCMBI")).intValue();
        int BCFMCPNI_NUMS = Integer.valueOf(map.get("BCFMCPNI")).intValue();
        int EN_CUST_EXPD_ECIF_NUMS = Integer.valueOf(map.get("EN_CUST_EXPD_ECIF")).intValue();
        int EN_RL_LEGAL_REP_INFO_NUMS = Integer.valueOf(map.get("EN_RL_LEGAL_REP_INFO")).intValue();
        int IND_CUST_EXPD_ECIF_NUMS = Integer.valueOf(map.get("IND_CUST_EXPD_ECIF")).intValue();
        int PUB_CUST_BASE_INFO_NUMS = Integer.valueOf(map.get("PUB_CUST_BASE_INFO")).intValue();
        int AFFMYRPT_NUMS = Integer.valueOf(map.get("AFFMYRPT")).intValue();
        int BCFMCDBI_NUMS = Integer.valueOf(map.get("BCFMCDBI")).intValue();
        int BDFMHQAA_NUMS = Integer.valueOf(map.get("BDFMHQAA")).intValue();
        int BCFMPCAI_NUMS = Integer.valueOf(map.get("BCFMPCAI")).intValue();
        int EN_RL_PER_INFO_NUMS = Integer.valueOf(map.get("EN_RL_PER_INFO")).intValue();
        int EN_FNC_CUST_ECIF_NUMS = Integer.valueOf(map.get("EN_FNC_CUST_ECIF")).intValue();
        String OUT = map.get("OUT");
        if (BBFMDJZL_NUMS > 0) {
            BBFMDJZL BBFMDJZL = new BBFMDJZL("aa");
            BBFMDJZL.generateData(BBFMDJZL_NUMS);
        }
        System.out.println("BBFMDJZL造数已完成");
                System.out.println("1/16");
        if (BBFMORGA_NUMS > 0) {
            BBFMORGA BBFMORGA = new BBFMORGA("AA");
            BBFMORGA.generateData(OUT, BBFMORGA_NUMS);
        }
        System.out.println("BBFMORGA造数已完成");
                System.out.println("2/16");
        if (BCFMCADI_NUMS > 0) {
            BCFMCADI BCFMCADI = new BCFMCADI("aa");
            BCFMCADI.generateData(OUT, BCFMCADI_NUMS);
        }
        System.out.println("BCFMCADI造数已完成");
                System.out.println("3/16");
        if (BCFMCIDI_NUMS > 0) {
            BCFMCIDI BCFMCIDI = new BCFMCIDI("aa");
            BCFMCIDI.generateData(OUT, BCFMCIDI_NUMS);
        }
        System.out.println("BCFMCIDI造数已完成");
                System.out.println("4/16");
        if (BCFMCMBI_NUMS > 0) {
            BCFMCMBI BCFMCMBI = new BCFMCMBI("aa");
            BCFMCMBI.generateData(OUT, BCFMCMBI_NUMS);
        }
        System.out.println("BCFMCMBI造数已完成");
                System.out.println("5/16");
        if (BCFMCPNI_NUMS > 0) {
            BCFMCPNI BCFMCPNI = new BCFMCPNI("aa");
            BCFMCPNI.generateData(OUT, BCFMCPNI_NUMS);
        }
        System.out.println("BCFMCPNI造数已完成");
                System.out.println("6/16");
        if (EN_CUST_EXPD_ECIF_NUMS > 0) {
            EN_CUST_EXPD_ECIF EN_CUST_EXPD_ECIF = new EN_CUST_EXPD_ECIF("aa");
            EN_CUST_EXPD_ECIF.generateData(OUT, EN_CUST_EXPD_ECIF_NUMS);
        }
        System.out.println("EN_CUST_EXPD_ECIF造数已完成");
                System.out.println("7/16");
        if (EN_RL_LEGAL_REP_INFO_NUMS > 0) {
            EN_RL_LEGAL_REP_INFO EN_RL_LEGAL_REP_INFO = new EN_RL_LEGAL_REP_INFO("aa");
            EN_RL_LEGAL_REP_INFO.generateData(OUT, EN_RL_LEGAL_REP_INFO_NUMS);
        }
        System.out.println("EN_RL_LEGAL_REP_INFO造数已完成");
                System.out.println("8/16");
        if (IND_CUST_EXPD_ECIF_NUMS > 0) {
            IND_CUST_EXPD_ECIF IND_CUST_EXPD_ECIF = new IND_CUST_EXPD_ECIF("aa");
            IND_CUST_EXPD_ECIF.generateData(OUT, IND_CUST_EXPD_ECIF_NUMS);
        }
        System.out.println("IND_CUST_EXPD_ECIF造数已完成");
                System.out.println("9/16");
        if (PUB_CUST_BASE_INFO_NUMS > 0) {
            PUB_CUST_BASE_INFO PUB_CUST_BASE_INFO = new PUB_CUST_BASE_INFO("aa");
            PUB_CUST_BASE_INFO.generateData(OUT, PUB_CUST_BASE_INFO_NUMS);
        }
        System.out.println("PUB_CUST_BASE_INFO造数已完成");
                System.out.println("10/16");
        if (AFFMYRPT_NUMS > 0) {
            AFFMYRPT AFFMYRPT = new AFFMYRPT("AFFMYRPT");
            AFFMYRPT.generateData(OUT, AFFMYRPT_NUMS);
        }
        System.out.println("AFFMYRPT造数已完成");
                System.out.println("11/16");
        if (BCFMCDBI_NUMS > 0) {
            BCFMCDBI BCFMCDBI = new BCFMCDBI("BCFMCDBI");
            BCFMCDBI.generateData(OUT, BCFMCDBI_NUMS);
        }
        System.out.println("BCFMCDBI造数已完成");
                System.out.println("12/16");
        if (BDFMHQAA_NUMS > 0) {
            BDFMHQAA BDFMHQAA = new BDFMHQAA("BDFMHQAA");
            BDFMHQAA.generateData(OUT, BDFMHQAA_NUMS);
        }
        System.out.println("BDFMHQAA造数已完成");
                System.out.println("13/16");
        if (BCFMPCAI_NUMS > 0) {
            BCFMPCAI BCFMPCAI = new BCFMPCAI("BCFMPCAI");
            BCFMPCAI.generateData(OUT, BCFMPCAI_NUMS);
        }
        System.out.println("BCFMPCAI造数已完成");
                System.out.println("14/16");
        if (EN_RL_PER_INFO_NUMS > 0) {
            EN_RL_PER_INFO EN_RL_PER_INFO = new EN_RL_PER_INFO("EN_RL_PER_INFO");
            EN_RL_PER_INFO.generateData(OUT, EN_RL_PER_INFO_NUMS);
        }
        System.out.println("EN_RL_PER_INFO造数已完成");
                System.out.println("15/16");
        if (EN_FNC_CUST_ECIF_NUMS > 0) {
            EN_FNC_CUST_ECIF EN_FNC_CUST_ECIF = new EN_FNC_CUST_ECIF("EN_FNC_CUST_ECIF");
            EN_FNC_CUST_ECIF.generateData(OUT, EN_FNC_CUST_ECIF_NUMS);
        }
        System.out.println("EN_FNC_CUST_ECIF造数已完成");
                System.out.println("16/16");
        System.out.println("全部造数已完成！");
    }
}
