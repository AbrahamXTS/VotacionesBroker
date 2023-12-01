package com.bichotitas.votacionesmvc.repositories;

import com.bichotitas.votacionesmvc.models.Vote;

import java.util.List;

public interface ResultsRepository {
    List<Vote> getResultsByProductName(String productName);

    Vote save(Vote entity);
}
