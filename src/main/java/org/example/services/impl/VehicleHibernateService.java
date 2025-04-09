package org.example.services.impl;

import org.example.models.Vehicle;
import org.example.services.IVehicleService;

import java.util.List;
import java.util.Optional;

public class VehicleHibernateService implements IVehicleService {
    @Override
    public List<Vehicle> findAll() {
        return List.of();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return null;
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return List.of();
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        return false;
    }
}
