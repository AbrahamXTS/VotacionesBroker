package com.bichotitas.votaciones.broker.repositories;

import com.bichotitas.votaciones.broker.models.Service;

import java.util.List;

public interface ServicesRepository {
    boolean saveService(Service newService);

    List<Service> getAllRegisteredServices();
}
