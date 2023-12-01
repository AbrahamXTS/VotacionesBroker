package com.bichotitas.votaciones.server.repositories;

import com.bichotitas.votaciones.server.models.Vote;

import java.util.List;

public interface ResultsRepository {
    List<Vote> getResultsByProductName(String productName);

    Vote save(Vote entity);
}
