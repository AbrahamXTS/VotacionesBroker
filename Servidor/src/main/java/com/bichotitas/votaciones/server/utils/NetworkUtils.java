package com.bichotitas.votaciones.server.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {
    private NetworkUtils() {
    }

    public static String getMyIp() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();

            return localhost.getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("Oh no! An error was ocurred while getting IP " + e.getLocalizedMessage());
        }

        return null;
    }
}
