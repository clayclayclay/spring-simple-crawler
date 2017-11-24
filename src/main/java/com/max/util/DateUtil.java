package com.max.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil
{

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    public final static String DATE_FORMAT_1 = "yyyy-MM-dd";
    public final static String DATE_FORMAT_2 = "yyyyMMdd";

    public static String getTodayDateString(String pattern)
    {
        simpleDateFormat.applyPattern(pattern);
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public static long getTodayTimestampString()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    public static String getTomorrowDateString(String pattern)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        simpleDateFormat.applyPattern(pattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static long getTomorrowTimestampString()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

}
