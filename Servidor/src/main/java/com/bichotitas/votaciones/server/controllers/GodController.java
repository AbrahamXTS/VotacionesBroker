package com.bichotitas.votaciones.server.controllers;

import com.bichotitas.votaciones.server.services.BinnacleService;
import com.bichotitas.votaciones.server.services.ProductsService;
import com.bichotitas.votaciones.server.services.VotesService;
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
    BinnacleService binnacleService;
    ProductsService productsService;
    VotesService votesService;

    public GodController(
            BinnacleService binnacleService,
            ProductsService productsService,
            VotesService votesService
    ) {
        this.binnacleService = binnacleService;
        this.productsService = productsService;
        this.votesService = votesService;
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                new MultipleClientsHandler(serverSocket.accept()).start();
            }
        } catch (Exception e) {
            System.out.println("Oh no! Internal server error");
        }
    }

    private class MultipleClientsHandler extends Thread {
        private final Socket communicationChannelWithClient;

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
                    System.out.println("Request registrada en el servidor" + gson.toJson(inputLine));

                    if (inputLine.equalsIgnoreCase("EXIT")) {
                        out.println("Good Bye!");
                        break;
                    }

                    Map<String, Object> input = gson.fromJson(inputLine, new TypeToken<Map<String, Object>>() {
                    }.getType());

                    String serviceName = (String) input.get("servicio");
                    Map<String, Object> response = null;

                    switch (serviceName) {
                        case "registrar" ->
                                response = binnacleService.logEvent((String) input.get("valor1"), (String) input.get("valor2"));
                        case "listar" -> response = binnacleService.getAllEvents();
                        case "contar" -> response = productsService.getProductsAndResults();
                        case "votar" -> response = votesService.addVote((String) input.get("variable1"));
                        default -> throw new IllegalArgumentException("Ops! Service not available");
                    }

                    System.out.println("Respuesta a " + serviceName + ": " + gson.toJson(response));
                    out.println(gson.toJson(response));
                }

                in.close();
                out.close();
                communicationChannelWithClient.close();
            } catch (IOException exception) {
                System.out.println("Oh no! Internal Server Error " + exception.getMessage());
            }
        }
    }
}
