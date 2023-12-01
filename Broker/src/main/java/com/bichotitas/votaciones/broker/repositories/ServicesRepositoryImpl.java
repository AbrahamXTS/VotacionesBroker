package com.bichotitas.votaciones.broker.repositories;

import com.bichotitas.votaciones.broker.models.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServicesRepositoryImpl implements ServicesRepository {
    private static ServicesRepositoryImpl instance;
    private final List<Service> services = new ArrayList<>();

    private ServicesRepositoryImpl() {
    }

    public static ServicesRepositoryImpl getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ServicesRepositoryImpl();
        }

        return instance;
    }

    public boolean saveService(Service newService) {
        return services.add(newService);
    }

    public List<Service> getAllRegisteredServices() {
        return services;
    }
}
