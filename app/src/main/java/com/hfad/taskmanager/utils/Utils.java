package com.hfad.taskmanager.utils;

import java.security.SecureRandom;

public class Utils {
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = new SecureRandom().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}
