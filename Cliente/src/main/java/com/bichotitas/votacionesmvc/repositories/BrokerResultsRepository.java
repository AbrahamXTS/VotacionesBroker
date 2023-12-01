package com.bichotitas.votacionesmvc.repositories;

import com.bichotitas.votacionesmvc.client.ClientToBroker;
import com.bichotitas.votacionesmvc.models.Vote;
import com.bichotitas.votacionesmvc.utils.FileReader;
import com.bichotitas.votacionesmvc.utils.Logger;
import com.bichotitas.votacionesmvc.utils.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BrokerResultsRepository implements ResultsRepository {
    private final Gson gson = new Gson();
    private final ClientToBroker clientToBroker = ClientToBroker.getInstance();

    public BrokerResultsRepository() {
    }

    private HashMap<String, List<Vote>> getAllResults() {
        HashMap<String, List<Vote>> results = new HashMap<>();

        Map<String, Object> request = new HashMap<>();
        request.put("servicio", "ejecutar");
        request.put("variables", 1);
        request.put("variable1", "servicio");
        request.put("valor1", "contar");

        Map<String, Object> response = gson.fromJson(
                clientToBroker.sendMessage(gson.toJson(request)),
                new TypeToken<Map<String, Object>>() {
                }.getType()
        );

        results.put((String) response.get("respuesta1"), new ArrayList<>(Collections.nCopies(((Double) response.get("valor1")).intValue(), new Vote(TimeUtils.getDate(), (String) response.get("respuesta1"), TimeUtils.getTime()))));
        results.put((String) response.get("respuesta2"), new ArrayList<>(Collections.nCopies(((Double) response.get("valor2")).intValue(), new Vote(TimeUtils.getDate(), (String) response.get("respuesta2"), TimeUtils.getTime()))));
        results.put((String) response.get("respuesta3"), new ArrayList<>(Collections.nCopies(((Double) response.get("valor3")).intValue(), new Vote(TimeUtils.getDate(), (String) response.get("respuesta3"), TimeUtils.getTime()))));

        return results;
    }

    @Override
    public List<Vote> getResultsByProductName(String productName) {
        Logger.log(this.getClass().getSimpleName(), "Getting votes for " + productName);

        return getAllResults()
                .getOrDefault(productName, new ArrayList<>());
    }

    @Override
    public Vote save(Vote vote) {
        Logger.log(this.getClass().getSimpleName(), "Saving vote for " + vote.getProductName());

        Map<String, Object> request = new HashMap<>();
        request.put("servicio", "ejecutar");
        request.put("variables", 2);
        request.put("variable1", "servicio");
        request.put("valor1", "votar");
        request.put("variable2", vote.getProductName());
        request.put("valor2", 1);

        clientToBroker.sendMessage(gson.toJson(request));

        return vote;
    }
}
