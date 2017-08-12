package cn.gov.baiyin.court.core.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by WK on 2017/3/27.
 */
public class DateUtil {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String current() {

        return LocalDateTime.now().format(TIME_FORMATTER);
    }

    public static String str2long(String timeStr) {

        return String.valueOf(LocalDateTime.parse(timeStr, TIME_FORMATTER2).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
}
