package com.bichotitas.votaciones.broker.controllers;

import com.bichotitas.votaciones.broker.repositories.ServicesRepositoryImpl;
import com.bichotitas.votaciones.broker.services.BrokerServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class GodController {

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                new MultipleClientsHandler(serverSocket.accept()).start();
            }
        } catch (Exception e) {
            System.out.println("Oh no! Internal server error");
        }
    }

    private static class MultipleClientsHandler extends Thread {
        private final Socket communicationChannelWithClient;

        private final BrokerServices brokerServices = new BrokerServices(ServicesRepositoryImpl.getInstance());

        public MultipleClientsHandler(Socket socket) {
            this.communicationChannelWithClient = socket;
        }

        @Override
        public void run() {
            Gson gson = new Gson();

            try {
                PrintWriter out = new PrintWriter(communicationChannelWithClient.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(communicationChannelWithClient.getInputStream()));

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Request received" + gson.toJson(inputLine));

                    if (inputLine.equalsIgnoreCase("EXIT")) {
                        out.println("Good Bye!");
                        break;
                    }

                    Map<String, Object> input = gson.fromJson(inputLine, new TypeToken<Map<String, Object>>() {
                    }.getType());

                    Map<String, Object> response;
                    String serviceName = (String) input.get("servicio");

                    switch (serviceName) {
                        case "registrar" -> response = brokerServices.registerANewServer(input);
                        case "listar" -> response = brokerServices.getAllRegisteredServers(input);
                        case "ejecutar" -> response = brokerServices.executeService(input);
                        default -> throw new IllegalArgumentException("Ops! Service not available");
                    }

                    String responseJson = gson.toJson(response);

                    out.println(responseJson);
                    System.out.println("Response sent for " + serviceName + ": " + responseJson);
                }

                in.close();
                out.close();
                communicationChannelWithClient.close();
            } catch (IOException exception) {
                System.out.println("Oh no! Internal Broker Error " + exception.getMessage());
            }
        }
    }
}
