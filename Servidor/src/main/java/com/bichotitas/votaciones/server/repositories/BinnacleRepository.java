package com.bichotitas.votaciones.server.repositories;

import java.util.List;

public interface BinnacleRepository {
    List<String> getAllEvents();

    void save(String eventDescription, String date);
}
