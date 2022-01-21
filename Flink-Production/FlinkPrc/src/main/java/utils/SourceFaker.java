package utils;

import com.github.javafaker.Faker;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;

public class SourceFaker {

    public static String locale;
    private static final Faker faker =  new Faker(new Locale("zh-TW"));
    private static final Instant beginTime = Instant.parse("2020-01-01T12:00:00.00Z");


//    public SourceFaker(String locale) {
//        locale =
//    }


    public String name(){
        return faker.name().name();
    }

    public String address(){
        return faker.address().streetName();
    }

    public String beer_name(){
        return faker.beer().name();
    }

    public String beer_style(){
        return faker.beer().style();
    }

    public Date birthday(){
        return faker.date().birthday();
    }

    public Instant time() {
        return Instant.now();
    }


    public static void main(String[] args) {
        Faker faker = new Faker(new Locale("zh-TW"));

        String name = faker.name().fullName(); // Miss Samanta Schmidt
        String firstName = faker.name().firstName(); // Emory
        String lastName = faker.name().lastName(); // Barton

        String streetAddress = faker.address().streetAddress(); // 60018 Sawayn Brooks Suite 449

        System.out.println(faker.beer().name());


        for (int i = 0; i < 10; i++) {
            SourceFaker faker1 = new SourceFaker();

            System.out.println(faker1.name() + faker1.address() + faker1.beer_name() + faker1.beer_style());
        }
    }


}
