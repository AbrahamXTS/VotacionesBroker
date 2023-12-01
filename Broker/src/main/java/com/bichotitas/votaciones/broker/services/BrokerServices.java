package com.bichotitas.votaciones.broker.services;

import com.bichotitas.votaciones.broker.client.ClientSocket;
import com.bichotitas.votaciones.broker.models.Service;
import com.bichotitas.votaciones.broker.repositories.ServicesRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BrokerServices {
    private final ServicesRepository servicesRepository;

    public BrokerServices(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    // Mapped by "registrar"
    public Map<String, Object> registerANewServer(Map<String, Object> request) {
        String serviceURL = request.get("valor1") + ":" + request.get("valor2");

        this.servicesRepository
                .saveService(new Service((String) request.get("valor3"), serviceURL));

        List<Service> servicesRegistered = this.servicesRepository
                .getAllRegisteredServices();

        Map<String, Object> response = new HashMap<>();

        response.put("servicio", "registrar");
        response.put("respuestas", 1);
        response.put("respuesta1", "identificador");
        response.put("valor1", servicesRegistered.size() - 1);

        return response;
    }

    // Mapped by "listar"
    public Map<String, Object> getAllRegisteredServers(Map<String, Object> request) {
        List<Service> services = this.servicesRepository.getAllRegisteredServices();

        Map<String, Object> response = new HashMap<>();

        response.put("servicio", "listar");

        if (request.containsKey("variable1") && request.get("variable1").equals("palabra")) {
            List<Service> filteredServices = services.stream()
                    .filter(service -> service.serviceName().equalsIgnoreCase((String) request.get("valor1")))
                    .toList();

            response.put("respuestas", filteredServices.size());

            for (int i = 0; i < filteredServices.size() - 1; i++) {
                response.put("respuesta " + (i + 1), filteredServices.get(i).serviceName());
                response.put("valor" + (i + 1), filteredServices.get(i).ip());
            }
        } else {
            response.put("respuestas", services.size());

            for (int i = 0; i < services.size() - 1; i++) {
                response.put("respuesta " + (i + 1), services.get(i).serviceName());
                response.put("valor" + (i + 1), services.get(i).ip());
            }
        }

        return response;
    }

    // Mapped by "ejecutar"
    public Map<String, Object> executeService(Map<String, Object> requestFromClient) {
        Gson gson = new Gson();

        Map<String, Object> response = new HashMap<>();

        String serviceName = (String) requestFromClient.get("valor1");

        List<Service> services = this.servicesRepository.getAllRegisteredServices();

        services.stream()
                .filter(service -> Objects.equals(service.serviceName(), serviceName))
                .findAny()
                .ifPresent(service -> {
                    ClientSocket clientSocket = new ClientSocket();

                    String serviceIp = service.ip().split(":")[0];
                    int servicePort = Integer.parseInt(service.ip().split(":")[1]);

                    System.out.println("Conectando a " + serviceIp + ":" + servicePort);

                    try {
                        clientSocket.startConnection(serviceIp, servicePort);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Map<String, Object> requestToServer = new HashMap<>();
                    requestToServer.put("servicio", serviceName);
                    requestToServer.put("variables", requestFromClient.get("variables"));

                    Double varsCount = (Double) requestFromClient.get("variables");

                    // Siempre estará cuando menos la variable que indique el nombre del servicio
                    if (varsCount > 1) {
                        for (int i = 1; i < varsCount; i++) {
                            requestToServer.put("variable" + i, requestFromClient.get("variable" + (i + 1)));
                            requestToServer.put("valor" + i, requestFromClient.get("valor" + (i + 1)));
                        }
                    }

                    String responseFromServer = null;

                    try {
                        responseFromServer = clientSocket.sendMessage(gson.toJson(requestToServer));
                        System.out.println("Request to server: " + gson.toJson(requestToServer));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Map<String, Object> responseMap = gson.fromJson(responseFromServer, new TypeToken<Map<String, Object>>() {
                    }.getType());

                    response.put("servicio", "ejecutar");
                    Double responsesCount = (Double) responseMap.get("respuestas");

                    response.put("respuestas", responsesCount);

                    for (int i = 0; i < responsesCount; i++) {
                        response.put("respuesta" + (i + 1), responseMap.get("respuesta" + (i + 1)));
                        response.put("valor" + (i + 1), responseMap.get("valor" + (i + 1)));
                    }
                });

        return response;
    }
}
