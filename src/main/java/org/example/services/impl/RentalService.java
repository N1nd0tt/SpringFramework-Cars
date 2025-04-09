package org.example.services.impl;

import org.example.models.Rental;
import org.example.repositories.IRentalRepository;

import java.util.Optional;

public class RentalService {
    private final IRentalRepository rentalRepo;

    public RentalService(IRentalRepository rentalRepo) {
        this.rentalRepo = rentalRepo;
    }

    public Optional<Rental> findByVehicleId(String id){
        return rentalRepo.findByVehicleIdAndReturnDateIsNull(id);
    }
    public Rental saveRental(Rental rental){
        return rentalRepo.save(rental);
    }
}
