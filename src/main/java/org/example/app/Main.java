package org.example.app;

import org.example.db.HibernateConfig;
import org.example.repositories.IRentalRepository;
import org.example.repositories.IUserRepository;
import org.example.repositories.IVehicleRepository;
import org.example.repositories.impl.hibernate.RentalHibernateRepository;
import org.example.repositories.impl.hibernate.UserHibernateRepository;
import org.example.repositories.impl.hibernate.VehicleHibernateRepository;
import org.example.repositories.impl.jdbc.RentalJdbcRepository;
import org.example.repositories.impl.jdbc.UserJdbcRepository;
import org.example.repositories.impl.jdbc.VehicleJdbcRepository;
import org.example.repositories.impl.json.RentalJsonRepository;
import org.example.repositories.impl.json.UserJsonRepository;
import org.example.repositories.impl.json.VehicleJsonRepository;
import org.example.services.IAuthService;
import org.example.services.IRentalService;
import org.example.services.IVehicleService;
import org.example.services.impl.hibernate.AuthHibernateService;
import org.example.services.impl.hibernate.RentalHibernateService;
import org.example.services.impl.hibernate.VehicleHibernateService;
import org.example.services.impl.simple.AuthService;
import org.example.services.impl.simple.RentalService;
import org.example.services.impl.simple.VehicleService;
import org.hibernate.Session;


public class Main {
    public static void main(String[] args) {
        String storageType = "jdbc";

        IAuthService authService;
        IVehicleService vehicleService;
        IRentalService rentalService;

        switch (storageType) {
            case "hibernate" -> {
                Session session = HibernateConfig.getSessionFactory().openSession();

                UserHibernateRepository userHibernateRepo = new UserHibernateRepository();
                userHibernateRepo.setSession(session);

                VehicleHibernateRepository vehicleHibernateRepo = new VehicleHibernateRepository();
                vehicleHibernateRepo.setSession(session);

                RentalHibernateRepository rentalHibernateRepo = new RentalHibernateRepository();
                rentalHibernateRepo.setSession(session);

                authService = new AuthHibernateService(userHibernateRepo);
                vehicleService = new VehicleHibernateService(vehicleHibernateRepo, rentalHibernateRepo);
                rentalService = new RentalHibernateService(rentalHibernateRepo, userHibernateRepo, vehicleHibernateRepo);
            }
            case "jdbc" -> {
                IUserRepository userRepo = new UserJdbcRepository();
                IVehicleRepository vehicleRepo = new VehicleJdbcRepository();
                IRentalRepository rentalRepo = new RentalJdbcRepository();

                authService = new AuthService(userRepo);
                vehicleService = new VehicleService(vehicleRepo, rentalRepo);
                rentalService = new RentalService(rentalRepo,  userRepo, vehicleRepo);
            }
            case "json" -> {
                IUserRepository userRepo = new UserJsonRepository();
                IVehicleRepository vehicleRepo = new VehicleJsonRepository();
                IRentalRepository rentalRepo = new RentalJsonRepository();

                authService = new AuthService(userRepo);
                vehicleService = new VehicleService(vehicleRepo, rentalRepo);
                rentalService = new RentalService(rentalRepo,  userRepo, vehicleRepo);
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }


        App app = new App(authService, vehicleService, rentalService);
        app.run();

    }
}
