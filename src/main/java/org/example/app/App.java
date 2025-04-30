package org.example.app;

import org.example.models.Rental;
import org.example.services.IAuthService;
import org.example.services.IRentalService;
import org.example.services.IVehicleService;
import org.example.models.User;
import org.example.models.Vehicle;
import java.util.*;

public class App {
    private final IAuthService authService;
    private final IVehicleService vehicleService;
    private final IRentalService rentalService;
    private final Scanner scanner = new Scanner(System.in);
    private User currentUser;

    public App(IAuthService authService, IVehicleService vehicleService, IRentalService rentalService) {
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
                    List<Vehicle> freeVehicles = vehicleService.findAvailableVehicles();
                    for (Vehicle v : freeVehicles) {
                        System.out.println(v);
                    }
                    break;
                case 2:
                    System.out.println("Provide ID of vehicle you want to rent:");
                    String rentID = scanner.next();
                    if (vehicleService.isAvailable(rentID)) {
                        Rental newRental = rentalService.rent(rentID, currentUser.getId());
                        System.out.println("Successfully rented a vehicle: " + newRental);
                    } else {
                        System.out.println("Vehicle is rented or does not exist.");
                    }
                    break;
                case 3:
                    System.out.println("Type in return vehicle ID:");
                    String returnID = scanner.next();
                    if (rentalService.returnRental(returnID, currentUser.getId())) {
                        System.out.println("Vehicle is successfully returned!");
                    } else {
                        System.out.println("Vehicle is not rented by you or does not exist.");
                    }
                    break;
                case 4:
                    System.out.println(currentUser);
                    break;
                case 5:
                    System.out.println("Saving data...");
                    System.out.println("Data saved. Exiting...");
                    return;
                case 6:
                    if (currentUser.getRole().equals("ADMIN")) {
                        List<Vehicle> allVehicles = vehicleService.findAll();
                        allVehicles.forEach(System.out::println);
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;
                case 7:
                    if (currentUser.getRole().equals("ADMIN")) {
                        List<User> users = authService.findAllUsers();
                        users.forEach(System.out::println);
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;
                case 8:
                    if (currentUser.getRole().equals("ADMIN")) {
                        System.out.println("Enter vehicle details:");
                        System.out.println("Type: ");
                        String type = scanner.next();
                        System.out.println("Brand: ");
                        String brand = scanner.next();
                        System.out.println("Model: ");
                        String model = scanner.next();
                        System.out.println("Year: ");
                        int year = scanner.nextInt();
                        System.out.println("Price: ");
                        double price = scanner.nextDouble();
                        System.out.println("Do you want add attributes? (yes/no)");
                        String addAttributes = scanner.next();
                        if (addAttributes.equalsIgnoreCase("yes")) {
                            System.out.println("Enter attributes (key=value, separated by commas): ");
                            String attributesInput = scanner.next();
                            Vehicle newVehicle = Vehicle.builder()
                                    .id(UUID.randomUUID().toString())
                                    .category(type)
                                    .brand(brand)
                                    .model(model)
                                    .year(year)
                                    .price(price)
                                    .build();
                            parseAndAddAttributes(newVehicle, attributesInput);
                            vehicleService.save(newVehicle);
                            System.out.println("Vehicle added successfully.");
                        } else {
                            Vehicle newVehicle = Vehicle.builder()
                                    .id(UUID.randomUUID().toString())
                                    .category(type)
                                    .brand(brand)
                                    .model(model)
                                    .year(year)
                                    .price(price)
                                    .build();
                            vehicleService.save(newVehicle);
                            System.out.println("Vehicle added successfully.");
                        }
                        System.out.println("Vehicle added successfully.");
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;
                case 9:
                    if (currentUser.getRole().equals("ADMIN")) {
                        System.out.println("Enter ID of vehicle to remove:");
                        String removeID = scanner.next();
                        vehicleService.deleteById(removeID);
                        System.out.println("Vehicle removed successfully.");
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