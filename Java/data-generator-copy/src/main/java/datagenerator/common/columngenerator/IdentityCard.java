package datagenerator.common.columngenerator;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IdentityCard extends AbstractColumnDataGen {
    private ArrayList<String> addressCode = new ArrayList<>();

    private Map<String, String> map = new HashMap<>();

    private Map<String, String> map2 = new HashMap<>();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private String initDate;

    private String endDate;

    private long need;

    private Calendar calendar = Calendar.getInstance();

    public IdentityCard(String columnName, String initDate, String endDate) {
        super(columnName);
        this.initDate = initDate;
        this.endDate = endDate;
        init();
    }

    public void init() {
        BufferedReader br = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.sdf.parse(this.initDate));
            URL url = Thread.currentThread().getContextClassLoader().getResource("addresscode");
            InputStreamReader reader = new InputStreamReader(url.openStream(), Charset.forName("UTF-8"));
            br = new BufferedReader(reader);
            String line = null;
            while (null != (line = br.readLine())) {
                String code = line.split(" ")[0];
                this.addressCode.add(code);
                this.map2.put(code, this.initDate + "000");
            }
            this.map.put("0", "1");
            this.map.put("1", "0");
            this.map.put("2", "X");
            this.map.put("3", "9");
            this.map.put("4", "8");
            this.map.put("5", "7");
            this.map.put("6", "6");
            this.map.put("7", "5");
            this.map.put("8", "4");
            this.map.put("9", "3");
            this.map.put("10", "2");
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

    public String getColumnData() {
        StringBuffer id = new StringBuffer();
        int index = (int)(Math.random() * this.addressCode.size());
        String currentAddressCode = ((String)this.addressCode.get(index)).toString();
        id.append(currentAddressCode);
        String lastDate = ((String)this.map2.get(currentAddressCode)).substring(0, 8);
        int lastSequence = Integer.parseInt(((String)this.map2.get(currentAddressCode))
                .substring(8));
        String currentDate = null;
        int currnetSequence = 0;
        try {
            if (lastSequence < 999) {
                currentDate = lastDate;
                currnetSequence = lastSequence + 1;
            } else {
                currnetSequence = 0;
                this.calendar.setTime(this.sdf.parse(lastDate));
                this.calendar.add(5, 1);
                currentDate = this.sdf.format(this.calendar.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currnetSequence < 10) {
            id.append(currentDate + "00" + currnetSequence);
            this.map2.put(currentAddressCode, currentDate + "00" + currnetSequence);
        } else if (currnetSequence < 100) {
            id.append(currentDate + "0" + currnetSequence);
            this.map2.put(currentAddressCode, currentDate + "0" + currnetSequence);
        } else {
            id.append(currentDate + currnetSequence);
            this.map2.put(currentAddressCode, currentDate + currnetSequence);
        }
        String[] nums = id.toString().split("");
        int sum = Integer.parseInt(nums[1]) * 9 + Integer.parseInt(nums[2]) * 10 + Integer.parseInt(nums[3]) * 5 + Integer.parseInt(nums[4]) * 8 + Integer.parseInt(nums[5]) * 4 + Integer.parseInt(nums[6]) * 2 + Integer.parseInt(nums[7]) * 1 + Integer.parseInt(nums[8]) * 6 + Integer.parseInt(nums[9]) * 3 + Integer.parseInt(nums[10]) * 7 + Integer.parseInt(nums[11]) * 9 + Integer.parseInt(nums[12]) * 10 + Integer.parseInt(nums[13]) * 5 + Integer.parseInt(nums[14]) * 8 + Integer.parseInt(nums[15]) * 4 + Integer.parseInt(nums[16]) * 2;
        int mod = sum % 11;
        id.append(this.map.get(mod + ""));
        return id.toString();
    }

    public static void main(String[] args) {
        ArrayList<String> id_card = new ArrayList<>();
        Set<String> h = new HashSet();
        IdentityCard card = new IdentityCard("id", "19800101", "19900102");
        card.init();
        for (int i = 0; i < 5001; i++) {
            id_card.add(card.getColumnData());
            h.add(card.getColumnData());
            System.out.println(card.getColumnData());
        }
        System.out.println(id_card.size());
        System.out.println(h.size());
    }
}