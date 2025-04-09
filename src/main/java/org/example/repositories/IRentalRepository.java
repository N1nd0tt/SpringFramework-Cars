package org.example.repositories;

import org.example.models.Rental;

import java.util.List;
import java.util.Optional;

public interface IRentalRepository {
    List<Rental> findAll();
    Optional<Rental> findById(String id);
    Rental save(Rental rental);
    void deleteById(String id);
    Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId);
}
