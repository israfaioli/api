package utils;

import com.github.javafaker.Faker;

public class Helpers {

    public static String obterTextoRandomico() {
        return new Faker().letterify("apiRest" + "??????");
    }

    public static Integer obterNumeroRandomico() {
        return new Faker().number().numberBetween(1, 1000);
    }
}
