package com.bichotitas.votaciones.server;

import static com.bichotitas.votaciones.server.config.Constants.BINNACLE_PATH_FILE;
import static com.bichotitas.votaciones.server.config.Constants.PRODUCTS_PATH_FILE;
import static com.bichotitas.votaciones.server.config.Constants.RESULTS_PATH_FILE;

import com.bichotitas.votaciones.server.client.ClientSocket;
import com.bichotitas.votaciones.server.controllers.GodController;
import com.bichotitas.votaciones.server.repositories.*;
import com.bichotitas.votaciones.server.services.BinnacleService;
import com.bichotitas.votaciones.server.services.ProductsService;
import com.bichotitas.votaciones.server.services.VotesService;
import com.bichotitas.votaciones.server.utils.NetworkUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {
    static String ip;
    static int serverPort;
    static String brokerHost;
    static int brokerPort;

    public static void main(String[] args) {
        GodController godController = getGodController();

        if (args[0].equalsIgnoreCase("PRODUCTION")) {
            ip = NetworkUtils.getMyIp();
            System.out.println("Using production variables " + ip);
        } else if (args[0].equalsIgnoreCase("DEVELOPMENT")) {
            ip = "127.0.0.1";
            System.out.println("Using development variables " + ip);
        } else {
            throw new RuntimeException("Enviroment not configured");
        }

        serverPort = Objects.nonNull(args[1]) ? Integer.parseInt(args[1]) : 80;
        brokerHost = Objects.nonNull(args[2]) ? args[2] : "127.0.0.1";
        brokerPort = Objects.nonNull(args[3]) ? Integer.parseInt(args[3]) : 90;

        try {
            Gson gson = new Gson();
            ClientSocket clientSocket = new ClientSocket();
            clientSocket.startConnection(brokerHost, brokerPort);
            clientSocket.sendMessage(gson.toJson(getRegistryRequestForContar()));
            clientSocket.sendMessage(gson.toJson(getRegistryRequestForVotar()));
            clientSocket.stopConnection();
        } catch (Exception e) {
            System.out.println("Unable to connect to broker");
        }

        godController.start(serverPort);
    }

    private static GodController getGodController() {
        BinnacleRepository binnacleRepository = new FileBinnacleRepository(BINNACLE_PATH_FILE);
        ProductsRepository productsRepository = new FileProductsRepository(PRODUCTS_PATH_FILE);
        ResultsRepository resultsRepository = new FileResultsRepository(RESULTS_PATH_FILE);

        BinnacleService binnacleService = new BinnacleService(binnacleRepository);
        ProductsService productsService = new ProductsService(productsRepository, resultsRepository);
        VotesService votesService = new VotesService(resultsRepository);

        return new GodController(binnacleService, productsService, votesService);
    }

    private static Map<String, Object> getRegistryRequestForContar() {
        Map<String, Object> registryRequest = new HashMap<>();

        registryRequest.put("servicio", "registrar");
        registryRequest.put("variables", 4);

        registryRequest.put("variable1", "servidor");
        registryRequest.put("valor1", ip);

        registryRequest.put("variable2", "puerto");
        registryRequest.put("valor2", String.valueOf(serverPort));

        registryRequest.put("variable3", "servicio");
        registryRequest.put("valor3", "contar");

        registryRequest.put("variable4", "parametros");
        registryRequest.put("valor4", "0");

        return registryRequest;
    }

    private static Map<String, Object> getRegistryRequestForVotar() {
        Map<String, Object> registryRequest = new HashMap<>();

        registryRequest.put("servicio", "registrar");
        registryRequest.put("variables", 4);

        registryRequest.put("variable1", "servidor");
        registryRequest.put("valor1", ip);

        registryRequest.put("variable2", "puerto");
        registryRequest.put("valor2", String.valueOf(serverPort));

        registryRequest.put("variable3", "servicio");
        registryRequest.put("valor3", "votar");

        registryRequest.put("variable4", "parametros");
        registryRequest.put("valor4", "2");

        return registryRequest;
    }
}