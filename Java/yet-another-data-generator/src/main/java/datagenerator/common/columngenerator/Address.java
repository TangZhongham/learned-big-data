package datagenerator.common.columngenerator;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

public class Address extends AbstractColumnDataGen {
    private ArrayList<String> addresses = new ArrayList<>();

    private static String roads = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public Address(String columnName) {
        super(columnName);
        init();
    }

    public void init() {
        BufferedReader br = null;
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("addresscode");
            InputStreamReader reader = new InputStreamReader(url.openStream(), Charset.forName("UTF-8"));
            br = new BufferedReader(reader);
            String line = null;
            while (null != (line = br.readLine())) {
                String code = line.split(" ")[1];
                this.addresses.add(code);
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

    public String getColumnData() {
        int index = (int)(Math.random() * this.addresses.size());
        StringBuffer sb = new StringBuffer();
        sb.append(this.addresses.get(index));
        sb.append(getRandomString(3) + "路");
                sb.append((int)(Math.random() * 1000.0D) + "号");
        return sb.toString();
    }

    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(roads.length());
            sb.append(roads.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Address address = new Address("address");
        address.init();
        for (int i = 0; i < 1000; i++)
            System.out.println(address.getColumnData());
    }
}