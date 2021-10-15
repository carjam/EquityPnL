package com.companyx.equity.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    // https://stackoverflow.com/questions/17432735/convert-unix-timestamp-to-date-in-java
    public static Date dateFromEpoch(Long unixSeconds) {
        // convert seconds to milliseconds
        return new java.util.Date(unixSeconds * 1000L);
    }

    public static Long epochFromDate(Date date) {
        // convert milliseconds to seconds
        return date.getTime() / 1000L;
    }

    public static String stringFromEpochGMT(Long unixSeconds) {
        Date date = dateFromEpoch(unixSeconds);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        return sdf.format(date);
    }

    public static String stringFromEpochPT(Long unixSeconds) {
        Date date = dateFromEpoch(unixSeconds);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

        TimeZone PT = java.util.TimeZone.getTimeZone("GMT-7");
        sdf.setTimeZone(PT);
        return sdf.format(date);
    }

}
