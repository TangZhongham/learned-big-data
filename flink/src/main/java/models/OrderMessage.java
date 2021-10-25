package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class OrderMessage {

    // 订单ID
    private String id;

    // 商家名称 UUID
    private String sName;

    // 类型Type, 用于filter/group by
    private String type;

    // 创建时间
    private String createTime;

    public OrderMessage(Integer id, String name, Integer type, Integer time) {
        this.id = String.valueOf(id);
        this.sName = name;
        this.type = String.valueOf(type);
        this.createTime = String.valueOf(time);
    }

    public OrderMessage(Integer id, String name, Integer type, Long time) {
        this.id = String.valueOf(id);
        this.sName = name;
        this.type = String.valueOf(type);
        this.createTime = String.valueOf(time);
    }

    public OrderMessage() {
        double d = Math.random();
        int id = (int) (d * 10000000);
        this.id = String.valueOf(id);

        String sName = getRandomString(4);
        this.sName = sName;

        String createTime = String.valueOf(System.currentTimeMillis());
        this.createTime = createTime;
    }

    public OrderMessage(int id, String time_type) {
        this.id = String.valueOf(id);
        this.sName = UUID.randomUUID().toString();

        // 四位随机数
        double d = Math.random();
        int type = (int) (d * 1000);
        this.type = String.valueOf(type);

        // create-time
        if (time_type.equals("unix-time")) {
            String createTime = String.valueOf(System.currentTimeMillis());
            this.createTime = createTime;
        } else {
            SimpleDateFormat df = new SimpleDateFormat(time_type);//设置日期格式
            this.createTime = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        }

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    // 可以改成delimiter 的方式， 方便Slipstream  读取
    @Override
    public String toString() {
        return id + ',' + sName + ',' + type + ',' + createTime;
//        return "OrderMessage{" +
//                "id='" + id + '\'' +
//                ", sName='" + sName + '\'' +
//                ", createTime=" + createTime +
//                ", remake='" + remake + '\'' +
//                '}';
    }

    public static void main(String[] args) {
        OrderMessage orderMessage = new OrderMessage();
        // 可以当 String 字段使用（id）
        orderMessage.setId(UUID.randomUUID().toString());
        // id
        final double d = Math.random();
        final int id = (int) (d * 1000);
        // 生产3位随机数字， group by 可以用
//        orderMessage.setId(String.valueOf(id));

        long timestamp = System.nanoTime();
//        orderMessage.setCreateTime(timestamp);
//        orderMessage.setRemake("remind");
        orderMessage.setsName("test");

        // 如果 kakfa 需要的话， 就使用
        String kafkaString = orderMessage.toString();
        System.out.println(kafkaString);
        System.out.println(orderMessage.toString());

        for (int i=0;i<10;i++) {
            OrderMessage x = new OrderMessage(i, "x");
            String Mesg = x.toString();
            System.out.println(Mesg);
            String msg = new OrderMessage().toString();
            System.out.println(msg);
            System.out.println(new Date().toGMTString());

        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        ArrayList<SimpleDateFormat> x = new ArrayList<>();
        x.add(df);
        x.add(df2);
        for (SimpleDateFormat i: x ) {
            String u = i.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
            System.out.println(u);
        }
//        System.out.println(date);

        System.out.println(orderMessage.toString().getBytes().length);
        System.out.println(orderMessage.toString());

    }

    public String getRandomString(int length){
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(3);
            long result=0;
            switch(number){
                case 0:
                    result=Math.round(Math.random()*25+65);
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:
                    result=Math.round(Math.random()*25+97);
                    sb.append(String.valueOf((char)result));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }
}