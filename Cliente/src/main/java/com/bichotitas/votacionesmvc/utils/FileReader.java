package com.bichotitas.votacionesmvc.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    private String filePath = "";

    public FileReader(String filePath) {
        this.filePath = filePath;
    }

    public List<String> getFileContent() {
        List<String> fileContent = new ArrayList<>();

        File file = new File(filePath);

        if (!(file.exists()) || !(file.canRead())) {
            throw new IllegalArgumentException("The file path entered is invalid.");
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();

                if (!(line.isBlank()) || !(line.isEmpty())) {
                    fileContent.add(line);
                }
            }
        } catch (Exception error) {
            System.out.println("Oh no! An error occurred while reading the file content" + error.getMessage());
        }

        return fileContent;
    }
}