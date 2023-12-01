package com.bichotitas.votaciones.server.repositories;

import com.bichotitas.votaciones.server.utils.FileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBinnacleRepository implements BinnacleRepository {
    final String binnacleFilePath;

    public FileBinnacleRepository(String binnacleFilePath) {
        this.binnacleFilePath = binnacleFilePath;
    }

    @Override
    public List<String> getAllEvents() {
        FileReader fileReader = new FileReader(binnacleFilePath);
        return fileReader.getFileContent();
    }

    @Override
    public void save(String eventDescription, String date) {
        try {
            Path path = Paths.get(binnacleFilePath);

            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }

            try (FileWriter fileWriter = new FileWriter(path.toString(), true);
                 BufferedWriter writer = new BufferedWriter(fileWriter)) {

                String messageToLog = eventDescription + " - " + date;
                writer.write(messageToLog + "\n");
            }
        } catch (IOException e) {
            System.out.println("Oh no! An error occurred while writing to a file");
            e.printStackTrace();
        }
    }
}
