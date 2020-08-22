package com.hfad.taskmanager.utils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = new SecureRandom().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }


    public static Date getRandomDate() {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(2000, 2020);
        gc.set(gc.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);
        return gc.getTime();
    }

    public static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }
}