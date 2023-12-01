package com.bichotitas.votaciones.server.repositories;

import com.bichotitas.votaciones.server.models.Vote;
import com.bichotitas.votaciones.server.utils.FileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileResultsRepository implements ResultsRepository {
    final String resultsFilePath;

    public FileResultsRepository(String resultsFilePath) {
        this.resultsFilePath = resultsFilePath;
    }

    private HashMap<String, List<Vote>> getAllResults() {
        FileReader fileReader = new FileReader(resultsFilePath);
        List<String> fileContent = fileReader.getFileContent();

        HashMap<String, List<Vote>> results = new HashMap<>();

        fileContent.forEach(line -> {
            String[] lineContent = line.split(" - ");

            Vote vote = Vote.builder()
                    .productName(lineContent[0])
                    .date(lineContent[1])
                    .time(lineContent[2])
                    .build();

            if (results.containsKey(vote.getProductName())) {
                results.get(vote.getProductName()).add(vote);
            } else {
                List<Vote> tempVotes = new ArrayList<>();
                tempVotes.add(vote);

                results.put(vote.getProductName(), tempVotes);
            }
        });

        return results;
    }

    @Override
    public List<Vote> getResultsByProductName(String productName) {
        return getAllResults()
                .getOrDefault(productName, new ArrayList<>());
    }

    @Override
    public Vote save(Vote vote) {
        try {
            Path path = Paths.get(resultsFilePath);

            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }

            System.out.println("Escribiendo a " + resultsFilePath);

            try (FileWriter fileWriter = new FileWriter(path.toString(), true);
                 BufferedWriter writer = new BufferedWriter(fileWriter)) {

                writer.write(vote.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Oh no! An error occurred while writing to a file");
            e.printStackTrace();
        }

        return vote;
    }
}
