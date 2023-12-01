package com.bichotitas.votaciones.server.services;

import com.bichotitas.votaciones.server.repositories.ProductsRepository;
import com.bichotitas.votaciones.server.repositories.ResultsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsService {
    private final ProductsRepository productsRepository;
    private final ResultsRepository resultsRepository;

    public ProductsService(ProductsRepository productsRepository, ResultsRepository resultsRepository) {
        this.productsRepository = productsRepository;
        this.resultsRepository = resultsRepository;
    }

    // Mapped by "contar"
    public Map<String, Object> getProductsAndResults() {
        List<String> productsCatalog = this.productsRepository.getAllProducts();

        Map<String, Object> response = new HashMap<>();

        response.put("servicio", "contar");
        response.put("respuestas", productsCatalog.size());

        for (int i = 0; i < productsCatalog.size(); i++) {
            response.put("respuesta" + (i + 1), productsCatalog.get(i));
            response.put("valor" + (i + 1), resultsRepository.getResultsByProductName(productsCatalog.get(i)).size());
        }

        return response;
    }
}
