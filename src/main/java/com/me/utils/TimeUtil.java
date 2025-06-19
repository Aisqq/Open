package com.me.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeUtil {
    public static LocalDateTime stringToLocalDateTime(String dateTime){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX", Locale.ROOT);
        return LocalDateTime.parse(dateTime, inputFormatter);
    }
}
