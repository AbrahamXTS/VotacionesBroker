package com.bichotitas.votaciones.server.repositories;

import com.bichotitas.votaciones.server.utils.FileReader;

import java.util.List;

public class FileProductsRepository implements ProductsRepository {
    final String productsFilePath;

    public FileProductsRepository(String productsFilePath) {
        this.productsFilePath = productsFilePath;
    }

    @Override
    public List<String> getAllProducts() {
        FileReader fileReader = new FileReader(productsFilePath);
        return fileReader.getFileContent();
    }
}
