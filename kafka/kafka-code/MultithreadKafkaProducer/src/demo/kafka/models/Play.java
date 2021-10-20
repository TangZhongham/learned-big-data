package demo.kafka.models;

import java.util.HashSet;

public class Play {
    public static void main(String[] args) {
        int a = 1;
        int b = 1;
        Integer c = 23;
        Integer d = 23;
        String string1 = "abcd";
        String string2 = "abcd";
        System.out.println(a == b);
        System.out.println(c == d);
        System.out.println(string1 == string2);

        float f = 1.1f;
        Long l = 1111111111111111L;

        WrapNum wrap1 = new WrapNum(string1, c);
        WrapNum wrap2 = new WrapNum(string2, d);
        WrapNum wrap3 = wrap1;

        System.out.println(wrap1 == wrap2);
        System.out.println(wrap1.equals(wrap2));
        System.out.println(wrap1.equals(wrap3));
        System.out.println(wrap1 == wrap3);
        System.out.println(wrap1.hashCode());
        System.out.println(wrap2.hashCode());


//        f();
    }

    private static class WrapNum {
        String x;
        Integer y;

        public WrapNum(String x, Integer y) {
            this.x = x;
            this.y = y;
        }
    }

//    public static void f() {
//        String[] a = new String[2];
//        a[0] = "hi";
//        ((Object[]) a)[1] = Integer.valueOf(42);
//    }
}
