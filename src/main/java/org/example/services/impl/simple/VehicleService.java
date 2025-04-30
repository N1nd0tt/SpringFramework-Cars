package org.example.services.impl.simple;

import org.example.models.Vehicle;
import org.example.repositories.IRentalRepository;
import org.example.repositories.IVehicleRepository;
import org.example.services.IVehicleService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VehicleService implements IVehicleService {
    private final IVehicleRepository vehicleRepo;
    private final IRentalRepository rentalRepo;

    public VehicleService(IVehicleRepository vehicleRepo, IRentalRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepo.findAll();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicleRepo.findById(id);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return vehicleRepo.save(vehicle);
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return vehicleRepo.findAll().stream()
                .filter(vehicle -> rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicle.getId()).isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isEmpty();
    }
    @Override
    public void deleteById(String id) {
        vehicleRepo.deleteById(id);
    }
}