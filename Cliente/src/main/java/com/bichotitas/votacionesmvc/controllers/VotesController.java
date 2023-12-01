package com.bichotitas.votacionesmvc.controllers;

import com.bichotitas.votacionesmvc.models.Vote;
import com.bichotitas.votacionesmvc.repositories.ResultsRepository;
import com.bichotitas.votacionesmvc.utils.Logger;
import com.bichotitas.votacionesmvc.utils.TimeUtils;

public class VotesController {
    private final ResultsRepository resultsRepository;

    public VotesController(ResultsRepository fileResultsRepository) {
        this.resultsRepository = fileResultsRepository;
    }

    public void vote(String productName) {
        this.resultsRepository.save(
                Vote.builder()
                        .productName(productName)
                        .date(TimeUtils.getDate())
                        .time(TimeUtils.getTime())
                        .build()
        );

        Logger.log(this.getClass().getSimpleName(), "Vote saved");
    }
}
