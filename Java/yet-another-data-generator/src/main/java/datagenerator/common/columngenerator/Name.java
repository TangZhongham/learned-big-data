package datagenerator.common.columngenerator;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

public class Name extends AbstractColumnDataGen {
    private ArrayList<String> firstNames = new ArrayList<>();

    private static String base;

    public Name(String columnName) {
        super(columnName);
        init();
    }

    public void init() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("firstname");
        URL url2 = Thread.currentThread().getContextClassLoader().getResource("chineseWords");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")));
            String line = null;
            while (null != (line = reader.readLine())) {
                String[] a = line.split(" ");
                for (int i = 0; i < a.length; i++) {
                    String[] b = a[i].split(":");
                    if (b.length == 2) {
                        int num = Integer.parseInt(b[1]);
                        for (int j = 0; j < num; j++)
                            this.firstNames.add(b[0]);
                    } else {
                        this.firstNames.add(b[0]);
                    }
                }
            }
            reader = new BufferedReader(new InputStreamReader(url2.openStream(), Charset.forName("UTF-8")));
            line = null;
            StringBuffer temp = new StringBuffer();
            while (null != (line = reader.readLine()))
                temp.append(line);
            base = temp.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public String getColumnData() {
        StringBuffer name = new StringBuffer();
        int index = (int)(Math.random() * this.firstNames.size());
        name.append(this.firstNames.get(index));
        name.append(getRandomString(2));
        return name.toString();
    }

    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Name name = new Name("name");
        name.init();
        for (int i = 0; i < 1000; i++)
            System.out.println(name.getColumnData());
    }
}

