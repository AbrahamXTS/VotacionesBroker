package com.bichotitas.votaciones.server.services;

import com.bichotitas.votaciones.server.repositories.BinnacleRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinnacleService {
    private final BinnacleRepository binnacleRepository;

    public BinnacleService(BinnacleRepository binnacleRepository) {
        this.binnacleRepository = binnacleRepository;
    }

    // Mapped by "registrar"
    public Map<String, Object> logEvent(String eventDescription, String date) {
        this.binnacleRepository.save(eventDescription, date);

        Map<String, Object> response = new HashMap<>();

        response.put("servicio", "registrar");
        response.put("respuestas", 1);
        response.put("respuesta1", "eventos");
        response.put("valor1", this.binnacleRepository.getAllEvents().size());

        return response;
    }

    // Mapped by "listar
    public Map<String, Object> getAllEvents() {
        List<String> events = this.binnacleRepository.getAllEvents();

        Map<String, Object> response = new HashMap<>();

        response.put("servicio", "listar");
        response.put("respuestas", events.size());

        for (int i = 0; i < events.size(); i++) {
            response.put("respuesta" + (i + 1), events.get(i));
        }

        return response;
    }
}
