package org.example.services.impl.simple;

import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.repositories.IRentalRepository;
import org.example.repositories.IUserRepository;
import org.example.repositories.IVehicleRepository;
import org.example.services.IRentalService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalService implements IRentalService {
    private final IRentalRepository rentalRepo;
    private final IUserRepository userRepo;
    private final IVehicleRepository vehicleRepo;

    public RentalService(IRentalRepository rentalRepo, IUserRepository userRepo, IVehicleRepository vehicleRepo) {
        this.rentalRepo = rentalRepo;
        this.userRepo = userRepo;
        this.vehicleRepo = vehicleRepo;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    @Override
    public Rental rent(String vehicleId, String userId) {
        Optional<User> user = userRepo.findById(userId);
        Optional<Vehicle> vehicle = vehicleRepo.findById(vehicleId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }
        if (vehicle.isEmpty()) {
            throw new IllegalArgumentException("Invalid vehicle ID: " + vehicleId);
        }
        if (isVehicleRented(vehicleId)) {
            throw new IllegalStateException("Vehicle with ID " + vehicleId + " is already rented.");
        }

        Rental rental = Rental.builder()
                .vehicle(vehicle.get())
                .user(user.get())
                .rentDate(LocalDate.now().toString())
                .returnDate(null)
                .build();
        return rentalRepo.save(rental);
    }

    @Override
    public boolean returnRental(String vehicleId, String userId) {
        Optional<Rental> rental = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId);
        if (rental.isPresent()) {
            Rental existingRental = rental.get();
            if (!existingRental.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("User ID does not match the rental record.");
            }
            existingRental.setReturnDate(LocalDate.now().toString());
            rentalRepo.save(existingRental);
            return true;
        }
        throw new IllegalArgumentException("No active rental found for vehicle ID: " + vehicleId);
    }

    @Override
    public List<Rental> findAll() {
        return rentalRepo.findAll();
    }
}