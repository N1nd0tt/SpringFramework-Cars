package org.example.app;

import org.example.models.Rental;
import org.example.services.impl.AuthService;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.services.impl.RentalService;
import org.example.services.impl.VehicleService;

import java.util.*;

public class App {
    private final AuthService authService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final Scanner scanner = new Scanner(System.in);
    private User currentUser;

    public App(AuthService authService, VehicleService vehicleService, RentalService rentalService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void run(){
        authenticateUser();
        displayMenu();
    }

    private void authenticateUser() {
        while (true) {
            System.out.println("1. Login \n2. Register");
            int choice = scanner.nextInt();
            if (choice == 1) {
                System.out.println("Enter login: ");
                String login = scanner.next();
                System.out.println("Enter password: ");
                String password = scanner.next();
                Optional<User> user = authService.login(login, password);
                if (user.isPresent()) {
                    currentUser = user.get();
                    break;
                } else {
                    System.out.println("Invalid login credentials.");
                }
            } else if (choice == 2) {
                System.out.println("Enter login: ");
                String login = scanner.next();
                System.out.println("Enter password: ");
                String password = scanner.next();
                System.out.println("Enter role (ADMIN/USER): ");
                String role = scanner.next();
                if (authService.register(login, password, role)) {
                    System.out.println("Successfully registered! Try to login now.");
                } else {
                    System.out.println("Failed to register.");
                }
            }
        }
    }

    private void displayMenu() {
        while (true) {
            System.out.println("Choose an option: 1 - get free vehicles, 2 - rent a vehicle, 3 - return a vehicle, 4 - get user info, 5 - exit");
            if (currentUser.getRole().equals("ADMIN")) {
                System.out.println("Admin options: 6 - get all vehicles, 7 - get users, 8 - add vehicle, 9 - remove vehicle");
            }
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Free vehicles:");
                    List<Vehicle> freeVehicles = vehicleService.getAvailableVehicles();
                    for (Vehicle v : freeVehicles) {
                        System.out.println(v);
                    }
                    break;
                case 2:
                    System.out.println("Provide ID of vehicle you want to rent:");
                    String rentID = scanner.next();
                    Optional<Vehicle> vehicle = vehicleService.getVehicleById(rentID);

                    if (vehicle.isPresent() && vehicleService.getAvailableVehicles().contains(vehicle.get())) {
                        Rental newRental = Rental.builder()
                                .vehicleId(rentID)
                                .userId(currentUser.getId())
                                .rentDateTime(String.valueOf(System.currentTimeMillis()))
                                .build();

                        rentalService.saveRental(newRental);
                        System.out.println("Successfully rented a vehicle!");
                    } else {
                        System.out.println("Vehicle is rented or don`t exist");
                    }
                    break;
                case 3:
                    System.out.println("Type in return vehicle ID:");
                    String returnID = scanner.next();

                    Optional<Rental> rental = rentalService.findByVehicleId(returnID);

                    if (rental.isPresent()) {
                        Rental updatedRental = rental.get();
                        updatedRental.setReturnDateTime(String.valueOf(System.currentTimeMillis()));
                        rentalService.saveRental(updatedRental);
                        System.out.println("Vehicle is successfully returned!");
                    } else {
                        System.out.println("Vehicle is not rented by you or don`t exist.");
                    }
                    break;
                case 4:
                    System.out.println(authService.getUserRepository().findById(currentUser.getId()));
                    break;
                case 5:
                    System.out.println("Saving data...");
                    System.out.println("Data saved. Exiting...");
                    return;
                case 6:
                    if (currentUser.getRole().equals("ADMIN")) {
                        List<String> rentedIds2 = vehicleService.getRentedVehicleIds();
                        List<Vehicle> allVehicles = vehicleService.getAllVehicle();

                        System.out.println("All vehicles:");
                        for (Vehicle v : allVehicles) {
                            String status = rentedIds2.contains(v.getId()) ? " (Rented)" : " (Free)";
                            System.out.println(v + status);
                        }
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;
                case 7:
                    if (currentUser.getRole().equals("ADMIN")) {
                        List<User> userList = authService.getUserRepository().findAll();
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
                        System.out.println("Enter rental price per day: ");
                        float price = scanner.nextFloat();
                        scanner.nextLine();
                        Vehicle newVehicle = Vehicle.builder()
                                .id(null)
                                .category(type)
                                .brand(brand)
                                .model(model)
                                .year(year)
                                .price(price)
                                .build();
                        System.out.println("Enter attributes of vehicle (key=value,key=value), or press Enter to skip:");
                        String attributes = scanner.nextLine();
                        if (!attributes.isBlank()) {
                            parseAndAddAttributes(newVehicle, attributes);
                        }
                        vehicleService.saveVehicle(newVehicle);
                        System.out.println("Vehicle added successfully.");
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;
                case 9:
                    if (currentUser.getRole().equals("ADMIN")) {
                        System.out.println("Enter ID of vehicle to remove: ");
                        String removeID = scanner.next();
                        vehicleService.deleteVehicleByID(removeID);
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    private static void parseAndAddAttributes(Vehicle vehicle, String input) {
        String[] pairs = input.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                vehicle.addAttribute(keyValue[0].trim(), keyValue[1].trim());
            }
        }
    }
}