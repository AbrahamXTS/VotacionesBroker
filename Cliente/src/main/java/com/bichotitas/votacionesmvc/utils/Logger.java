package com.bichotitas.votacionesmvc.utils;

public class Logger {
    private Logger() {
    }

    public static void log(String className, String message) {
        String messageToLog = className + " - " + message + " - " + TimeUtils.getDate() + " - " + TimeUtils.getTime();
        System.out.println("Logger: " + messageToLog);
    }
}
