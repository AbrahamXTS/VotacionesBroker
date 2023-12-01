package com.bichotitas.votacionesmvc.repositories;

import com.bichotitas.votacionesmvc.client.ClientToBroker;
import com.bichotitas.votacionesmvc.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrokerProductsRepository implements ProductsRepository {
    private final Gson gson = new Gson();
    private final ClientToBroker clientToBroker = ClientToBroker.getInstance();

    public BrokerProductsRepository() {
    }

    @Override
    public List<String> getAllProducts() {
        Logger.log(this.getClass().getSimpleName(), "Getting the name of all products");

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

        List<String> names = new ArrayList<>();

        names.add((String) response.get("respuesta1"));
        names.add((String) response.get("respuesta2"));
        names.add((String) response.get("respuesta3"));

        return names;
    }
}
