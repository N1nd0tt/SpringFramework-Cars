package org.example.services;

import lombok.Getter;
import org.example.models.Rental;
import org.example.models.Vehicle;
import org.example.repositories.IRentalRepository;
import org.example.repositories.IVehicleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VehicleService {
    private final IVehicleRepository vehicleRepo;
    private final IRentalRepository rentalRepo;

    public VehicleService(IVehicleRepository vehicleRepo, IRentalRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    public List<String> getRentedVehicleIds() {
        return rentalRepo.findAll().stream()
                .filter(rental -> rental.getRentDateTime() != null && rental.getReturnDateTime() == null)
                .map(Rental::getVehicleId)
                .collect(Collectors.toList());
    }

    public List<Vehicle> getAvailableVehicles() {
        List<String> rentedVehicleIds = getRentedVehicleIds();
        return vehicleRepo.findAll().stream()
                .filter(vehicle -> !rentedVehicleIds.contains(vehicle.getId()))
                .collect(Collectors.toList());
    }
    public List<Vehicle> getAllVehicle(){
        return vehicleRepo.findAll();
    }

    public Optional<Vehicle> getVehicleById(String id){
        return vehicleRepo.findById(id);
    }

    public Vehicle saveVehicle(Vehicle vehicle){
        return vehicleRepo.save(vehicle);
    }

    public void deleteVehicleByID(String id){
        vehicleRepo.deleteById(id);
    }
}
