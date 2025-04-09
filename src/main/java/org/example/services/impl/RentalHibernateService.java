package org.example.services.impl;

import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.repositories.impl.hibernate.RentalHibernateRepository;
import org.example.repositories.impl.hibernate.UserHibernateRepository;
import org.example.repositories.impl.hibernate.VehicleHibernateRepository;
import org.example.services.IRentalService;
import org.example.utils.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class RentalHibernateService implements IRentalService {
    private final RentalHibernateRepository rentalRepo;
    private final VehicleHibernateRepository vehicleRepo;
    private final UserHibernateRepository userRepo;

    public RentalHibernateService(RentalHibernateRepository rentalRepo, VehicleHibernateRepository vehicleRepo,
                                  UserHibernateRepository userRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
        }
    }

    @Override
    public Rental rent(String vehicleId, String userId) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            rentalRepo.setSession(session);
            //vehicleRepo.setSession(session);
            //userRepo.setSession(session);
            if (rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent()) {
                throw new IllegalStateException("Vehicle is rented");
            }
            Vehicle vehicle = vehicleRepo.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Rental rental = Rental.builder()
                    .id(UUID.randomUUID().toString())
                    .vehicle(vehicle)
                    .user(user)
                    .rentDate(LocalDateTime.now().toString())
                    .build();
            rentalRepo.save(rental);
            tx.commit();
            return rental;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
    @Override
    public boolean returnRental(String vehicleId, String userId){
        return findAll().stream().anyMatch(rental -> rental.getVehicle().getId().equals(vehicleId) && rental.getUser().getId().equals(userId));
    }

    @Override
    public List<Rental> findAll() {
        return rentalRepo.findAll();
    }
}