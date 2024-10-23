package com.example.calculate.utils;

import java.util.Objects;
import java.util.Random;

public class TestUtils {
    private static final Random RAND = new Random();
    private static final int MIN_INT = 1;
    private static final int MAX_INT = 100_000_000;


    public static String getStrForDisplayByNumber(final float num) {
        return (canConvertToInteger(num)) ? (int)num + "" : num + "";
    }


    public static float convertStrToFloat(final String strNum) {
        float number;
        try {
            number = Float.parseFloat(strNum);
        } catch (NumberFormatException e) {
            number = 0;
        }
        return number;
    }

    public static boolean canConvertToInteger(final float num) {
        return num % 1 == 0;
    }


    public static boolean isOperation(final String str, String[] operations) {
        for (String oper : operations) {
            if (Objects.equals(oper, str)) {
                return true;
            }
        }
        return false;
    }


    public static int getRandomInteger() {
        return getRandomInteger(MIN_INT, MAX_INT);
    }

    public static int getRandomInteger(final int min) {
        return getRandomInteger(min, MAX_INT);
    }

    public static int getRandomInteger(final int min, final int max) {
        return RAND.nextInt((max - min) + 1) + min;
    }
}
