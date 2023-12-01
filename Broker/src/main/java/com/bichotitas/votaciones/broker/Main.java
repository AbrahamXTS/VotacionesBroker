package com.bichotitas.votaciones.broker;

import com.bichotitas.votaciones.broker.controllers.GodController;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        GodController godController = new GodController();

        int port = Objects.nonNull(args[0]) ? Integer.parseInt(args[0]) : 90;

        System.out.println("Broker escuchando en el puerto: " + port);

        godController.start(port);
    }
}