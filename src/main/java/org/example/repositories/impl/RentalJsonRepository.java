package org.example.repositories.impl;

import com.google.gson.reflect.TypeToken;
import org.example.models.Rental;
import org.example.repositories.IRentalRepository;
import org.example.utils.JsonFileStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class RentalJsonRepository implements IRentalRepository {
    private final JsonFileStorage<Rental> storage =
            new JsonFileStorage<>("rentals.json", new TypeToken<List<Rental>>(){}.getType());
    private final List<Rental> rentals;

    public RentalJsonRepository() {
        this.rentals = new
                ArrayList<>(storage.load());
    }
    @Override
    public List<Rental> findAll() {
        return new ArrayList<>(rentals);
    }

    @Override
    public Optional<Rental> findById(String id) {
        return rentals.stream().filter(v ->
                v.getId().equals(id)).findFirst();
    }
    @Override
    public Optional<Rental> findByVehId(String vehId) {
        if (rentals == null || rentals.isEmpty()) {
            return Optional.empty();
        }
        return rentals.stream().filter(v ->
                v.getVehicleId().equals(vehId)).findFirst();
    }

    public List<String> getRentedVehicleIds() {
        return rentals.stream()
                .filter(Rental::isActive)
                .map(Rental::getVehicleId)
                .collect(Collectors.toList());
    }

    @Override
    public Rental save(Rental user) {
        if (user.getId() == null ||
                user.getId().isBlank()) {
            user.setId(UUID.randomUUID().toString());
        } else {
            deleteById(user.getId());
        }
        rentals.add(user);
        storage.save(rentals);
        return user;
    }
    @Override
    public void deleteById(String id) {
        rentals.removeIf(v -> v.getId().equals(id));
        storage.save(rentals);
    }
}
