package com.bichotitas.votaciones.server.config;

import java.io.File;

public class Constants {
    public static final String USER_DIRECTORY_PREFIX = System.getProperty("user.dir") + File.separator;
    public static final String BINNACLE_PATH_FILE = USER_DIRECTORY_PREFIX + "binnacle.txt";
    public static final String PRODUCTS_PATH_FILE = USER_DIRECTORY_PREFIX + "products.txt";
    public static final String RESULTS_PATH_FILE = USER_DIRECTORY_PREFIX + "results.txt";

    private Constants() {
    }
}
