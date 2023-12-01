package com.bichotitas.votaciones.server.utils;

import java.util.Calendar;

public class TimeUtils {
    private static final Calendar calendar = Calendar.getInstance();

    private TimeUtils() {
    }

    public static String getDate() {
        return calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
    }

    public static String getTime() {
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
    }
}
