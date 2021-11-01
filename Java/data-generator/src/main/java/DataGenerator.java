import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataGenerator {

    private static final int Random_num = new Random().nextInt();
    private static final Instant beginTime = Instant.parse("2020-01-01T12:00:00.00Z");
    private static final Faker faker = new Faker(new Locale("zh-CN"));

    public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String upperLetterChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String lowerLetterChar = "abcdefghijklmnopqrstuvwxyz";
    public static final String numberChar = "0123456789";
    public static final String numberLowerLetterChar = "0123456789abcdefghijklmnopqrstuvwxyz";
    public static final String numberUpperLetterChar = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final FakeValuesService fakeValuesService = new FakeValuesService(
            new Locale("en-GB"), new RandomService());


//    public DataGenerator2(ArrayList<String> column_list) {
//
//    }

    public static enum ColumnType {
        INT,
        VARCHAR,
        DATE
    }

    public DataGenerator(String args)
    {};

    public static StringJoiner get_all(ArrayList<String> column_list, String delimiter) throws Exception {
//        StringBuilder sb = new StringBuilder();
        StringJoiner sb = new StringJoiner(delimiter, "", "\n");

        // 出现多线程问题（between抛异常）尝试添加锁，
//        synchronized (DataGenerator.class) {
            for (String i: column_list) {
                List<String> result = Arrays.asList(i.split(","));
                String columnType = result.get(0);
//            ColumnType x = result.get(0);
                switch (columnType) {
                    default:
                        int num = Integer.parseInt(result.get(1));
                        sb.add(DataGenerator.getRandomALLChar(num));
                        break;
                    case "int" :
                        int num3 = Integer.parseInt(result.get(1));
                        sb.add(DataGenerator.get_Integer(num3));
                        break;
                    case "varchar" :
                        int num2 = Integer.parseInt(result.get(1));
                        sb.add(DataGenerator.getRandomALLChar(num2));
                        break;
//                    case "date" :
//                        String start = result.get(1);
//                        String end = result.get(2);
//                        sb.add(DataGenerator.between(start, end));
//                        break;
                    case "name" :
                        sb.add(DataGenerator.get_Name());
                        break;
                    case "quote" :
                        sb.add(DataGenerator.get_Quote());
                        break;
                    case "telephone" :
                        sb.add(DataGenerator.get_Tel());
                        break;
                    case "address" :
                        sb.add(DataGenerator.get_Addr());
                        break;
                }
            }
//    }
        // 获得 column list, 构造
        return sb;
    }

    public static String getRandomNumberChar(int n) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < n; i++) { sb.append( numberChar.charAt( random.nextInt( numberChar.length() ) ) );}
        return sb.toString();
    };

    public static String getRandomALLChar(int n) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < n; i++) { sb.append( allChar.charAt( random.nextInt( allChar.length() ) ));}
        return sb.toString();
    }

    public static String getRandomALLChar2(int n) {
        // 没有 原生的快，数量级的差距
//        FakeValuesService fakeValuesService = new FakeValuesService(
//                new Locale("en-GB"), new RandomService());
//        return fakeValuesService.regexify(String.format("[a-z1-9]{%s}", n));
        return fakeValuesService.letterify(String.format("[a-z]{%s}", n));
//        Matcher alphaNumericMatcher = Pattern.compile("[a-z1-9]{10}").matcher(alphaNumericString);
    }

    public static String get_Integer(int num) {
        return String.valueOf(faker.number().randomNumber(num, true));
    }

    public static String get_date(String start, String end) throws ParseException {
        Date from = fmt.parse(start);
        Date to = fmt.parse(end);

        return String.valueOf(faker.date().between(from, to));
    }


    public static String get_String(int num) {
        return faker.name().username();
    }

    public static String get_Quote() {
        return faker.rickAndMorty().quote();
    }

//    public static String between(String start, String end) throws IllegalArgumentException, ParseException {
//        synchronized (DataGenerator.class) {
//
//        Date from = fmt.parse(start);
//        Date to = fmt.parse(end);
//        if (to.before(from)) {
//            throw new IllegalArgumentException("Invalid date range, the upper bound date is before the lower bound.");
//        } else if (from.equals(to)) {
//            return end;
//        } else {
//            long offsetMillis = faker.random().nextLong(to.getTime() - from.getTime());
//            Date new_date = new Date(from.getTime() + offsetMillis);
//            return fmt.format(new_date);
//        }
//    }
//}

    public static String get_Name() {
        return faker.name().fullName();
    }

    public static String get_Addr() {
        return faker.address().city();
    }

    public static String get_Tel() {
        return faker.phoneNumber().cellPhone();
    }


    public static void main(String[] args) throws ParseException {
//        DataGenerator2 x = DataGenerator2;
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String xx = DataGenerator.between("2010-01-01 10:00:00","2020-12-12 10:00:00");
//        System.out.println(String.format("%s",
//                xx));

        // 方法性能对比
        //1 耗时：42
        //2 耗时：20
        long s = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
//            String num1 = DataGenerator.getRandomNumberChar(8);

            String num1 = DataGenerator.getRandomALLChar(8);
        }
        System.out.println("1 耗时：" + (System.currentTimeMillis()-s));
        long s2 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
//            String num2 = DataGenerator.get_Integer(8);
            String num1 = DataGenerator.getRandomALLChar2(8);
        }
        System.out.println("2 耗时：" + (System.currentTimeMillis()-s2));


//        Faker faker = new Faker(new Locale("zh-CN"));
//        faker.date().between()


//        StringBuilder sb = new StringBuilder();
//        sb.append(x.get_Integer(8));
//        sb.append(x.get_Quote());
//        sb.append(x.get_String(8));
//
//        StringJoiner sj = new StringJoiner(",", "", "\n");
//        sj.add(x.get_Integer(8));
//        sj.add(x.get_Integer(8));
//        sj.add(x.get_Quote());
//        sj.add(x.getRandomALLChar(8));
//
//        System.out.println(String.format("%s",
//                sj));

//        System.out.println(String.format("%s\n%s",
//                x.get_Integer(8), x.get_String(2)));

//        Faker faker = new Faker(new Locale("zh-CN"));
//        faker.date().between()
//
//        String name = faker.name().fullName(); // Miss Samanta Schmidt
//        String firstName = faker.name().firstName(); // Emory
//        String lastName = faker.name().lastName(); // Barton
//
//        String streetAddress = faker.address().streetAddress(); // 60018 Sawayn Brooks Suite 449
//
//        String streetName = faker.address().streetName();
//        String number = faker.address().buildingNumber();
//        String city = faker.address().city();
//        String country = faker.address().country();
//
//        System.out.println(String.format("%s\n%s\n%s\n%s",
//                number,
//                streetName,
//                city,
//                country));
    }
}
