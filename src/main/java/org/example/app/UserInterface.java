package org.example.app;

import org.example.repositories.impl.RentalJsonRepository;
import org.example.services.AuthService;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.repositories.impl.VehicleJsonRepository;
import org.example.repositories.impl.UserJsonRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    public static VehicleJsonRepository repo = new VehicleJsonRepository();
    public static UserJsonRepository userJsonRepository = new UserJsonRepository();
    public static RentalJsonRepository rentalJsonRepository = new RentalJsonRepository();
    public static AuthService authService = new AuthService(userJsonRepository);
    public static Scanner scanner = new Scanner(System.in);
    public static User currentUser;

    public static void main(String[] args) {

        while (true) {
            System.out.println("1. Login \n2. Register");
            int choice = scanner.nextInt();
            if(choice == 1){
                System.out.println("Enter login: ");
                String login = scanner.next();
                System.out.println("Enter password: ");
                String password = scanner.next();
                if (authService.login(login, password).isPresent()) {
                    currentUser = authService.login(login, password).get();
                    break;
                } else {
                    System.exit(24);
                }
            } else if(choice == 2){
                System.out.println("Enter login: ");
                String login = scanner.next();
                System.out.println("Enter password: ");
                String password = scanner.next();
                if (authService.register(login, password)) {
                    System.out.println("Successfully registered! try to login now.");
                } else {
                    System.out.println("Failed to register.");
                }
            }
        }

        while (true) {
            System.out.println("Choose an option: 1 - get free vehicles, 2 - rent a vehicle, 3 - return a vehicle, 4 - get user info, 5 - exit");
            if (currentUser.getRole().equals("ADMIN")) {
                System.out.println("Admin options: 6 - get all vehicles, 7 - get users, 8 - add vehicle, 9 - remove vehicle");
            }
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    List<Vehicle> freeVehicleList = repo.findAll();
                    System.out.println("Free vehicles in repo:");
                    for (Vehicle v : freeVehicleList) {
                        if(v.getId()){
                            System.out.println(v);
                        }
                    }
                    break;
                case 2:
                    System.out.println("Type ID of selected vehicle: ");
                    int rentID = scanner.nextInt();
                    repo.rentVehicle(rentID);
                    break;
                case 3:
                    System.out.println("Type ID of selected vehicle: ");
                    int returnID = scanner.nextInt();
                    repo.returnVehicle(returnID);
                    break;
                case 4:
                    System.out.println(userJsonRepository.findById(currentUser.getId()));
                    break;
                case 5:
                    System.out.println("Saving data...");
                    System.out.println("Data saved. Exiting...");
                    return;
                case 6:
                    List<Vehicle> vehicleList = repo.findAll();
                    System.out.println("Vehicles in repo:");
                    for (Vehicle v : vehicleList) {
                        System.out.println(v);
                    }
                    break;
                    break;
                case 7:
                    if (currentUser.getRole().equals("ADMIN")) {
                        List<User> userList = userJsonRepository.findAll();
                        System.out.println("Users in repo:");
                        for (User u : userList) {
                            System.out.println(u);
                        }
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;
                case 8:
                    if (currentUser.getRole().equals("ADMIN")) {
                        System.out.println("Enter vehicle type (car/moto): ");
                        String type = scanner.next().toLowerCase();
                        System.out.println("Enter brand: ");
                        String brand = scanner.next();
                        System.out.println("Enter model: ");
                        String model = scanner.next();
                        System.out.println("Enter production year: ");
                        int year = scanner.nextInt();
                        System.out.println("Enter systemID: ");
                        int systemID = scanner.nextInt();
                        System.out.println("Enter rental price per day: ");
                        float price = scanner.nextFloat();
                        if (type.equals("car")) {
                            Car newCar = new Car(brand, model, year, systemID, price, false);
                            repo.addVehicle(newCar);
                            System.out.println("Car added successfully.");
                        } else if (type.equals("moto")) {
                            System.out.println("Enter motorcycle type: ");
                            String motoType = scanner.next();
                            Motorcycle newMoto = new Motorcycle(brand, model, motoType, year, systemID, price, false);
                            repo.addVehicle(newMoto);
                            System.out.println("Motorcycle added successfully.");
                        } else {
                            System.out.println("Invalid vehicle type!");
                        }
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;
                case 9:
                    if (currentUser.getRole().equals("ADMIN")) {
                        System.out.println("Enter ID of vehicle to remove: ");
                        int removeID = scanner.nextInt();
                        repo.deleteById(String.valueOf(removeID));
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}