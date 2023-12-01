package com.bichotitas.votacionesmvc;

import com.bichotitas.votacionesmvc.client.ClientToBroker;

public class Bootstrap {
    public static void main(String[] args) {
        ClientToBroker.configureInstance(args[0], Integer.parseInt(args[1]));
        Main.main(args);
    }
}
