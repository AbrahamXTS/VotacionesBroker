package com.bichotitas.votaciones.server.services;

import com.bichotitas.votaciones.server.models.Vote;
import com.bichotitas.votaciones.server.utils.TimeUtils;
import com.bichotitas.votaciones.server.repositories.ResultsRepository;

import java.util.HashMap;
import java.util.Map;

public class VotesService {
    private final ResultsRepository resultsRepository;

    public VotesService(ResultsRepository fileResultsRepository) {
        this.resultsRepository = fileResultsRepository;
    }

    // Mapped by "votar"
    public Map<String, Object> addVote(String productName) {
        this.resultsRepository.save(
                Vote.builder()
                        .productName(productName)
                        .date(TimeUtils.getDate())
                        .time(TimeUtils.getTime())
                        .build()
        );

        Map<String, Object> response = new HashMap<>();

        response.put("servicio", "votar");
        response.put("respuestas", 1);
        response.put("respuesta1", productName);
        response.put("valor1", this.resultsRepository.getResultsByProductName(productName).size());

        return response;
    }
}
