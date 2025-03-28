package org.example.app;

import org.example.models.Rental;
import org.example.repositories.impl.RentalJsonRepository;
import org.example.services.AuthService;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.repositories.impl.VehicleJsonRepository;
import org.example.repositories.impl.UserJsonRepository;

import java.util.*;
import java.util.stream.Collectors;

public class UserInterface {
    private static final VehicleJsonRepository repo = new VehicleJsonRepository();
    private static final UserJsonRepository userJsonRepository = new UserJsonRepository();
    private static final RentalJsonRepository rentalJsonRepository = new RentalJsonRepository();
    private static final AuthService authService = new AuthService(userJsonRepository);
    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser;

    public static void main(String[] args) {
        try {
            authenticateUser();
            displayMenu();
        } finally {
            scanner.close();
        }
    }

    private static void authenticateUser() {
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
                if (authService.register(login, password)) {
                    System.out.println("Successfully registered! Try to login now.");
                } else {
                    System.out.println("Failed to register.");
                }
            }
        }
    }

    private static void displayMenu() {
        while (true) {
            System.out.println("Choose an option: 1 - get free vehicles, 2 - rent a vehicle, 3 - return a vehicle, 4 - get user info, 5 - exit");
            if (currentUser.getRole().equals("ADMIN")) {
                System.out.println("Admin options: 6 - get all vehicles, 7 - get users, 8 - add vehicle, 9 - remove vehicle");
            }
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    List<String> rentedIds = rentalJsonRepository.getRentedVehicleIds();
                    List<Vehicle> freeVehicles = repo.findAll().stream()
                            .filter(v -> !rentedIds.contains(v.getId()))
                            .collect(Collectors.toList());

                    System.out.println("Free vehicles:");
                    for (Vehicle v : freeVehicles) {
                        System.out.println(v);
                    }
                    break;
                case 2:
                    System.out.println("Provide ID of vehicle you want to rent:");
                    String rentID = scanner.next();
                    Optional<Vehicle> vehicle = repo.findById(rentID);

                    if (vehicle.isPresent() && rentalJsonRepository.findByVehId(rentID).isEmpty()) {
                        Rental newRental = Rental.builder()
                                .vehicleId(rentID)
                                .userId(currentUser.getId())
                                .rentDateTime(String.valueOf(System.currentTimeMillis()))
                                .build();

                        rentalJsonRepository.save(newRental);
                        System.out.println("Successfully rented a vehicle!");
                    } else {
                        System.out.println("Vehicle is rented or don`t exist");
                    }
                    break;
                case 3:
                    System.out.println("Type in return vehicle ID:");
                    String returnID = scanner.next();

                    Optional<Rental> rental = rentalJsonRepository.findByVehId(returnID);

                    if (rental.isPresent()) {
                        Rental updatedRental = rental.get();
                        updatedRental.setReturnDateTime(String.valueOf(System.currentTimeMillis()));
                        rentalJsonRepository.save(updatedRental);
                        System.out.println("Vehicle is successfully returned!");
                    } else {
                        System.out.println("Vehicle is not rented by you or don`t exist.");
                    }
                    break;
                case 4:
                    System.out.println(userJsonRepository.findById(currentUser.getId()));
                    break;
                case 5:
                    System.out.println("Saving data...");
                    System.out.println("Data saved. Exiting...");
                    return;
                case 6:
                    if (currentUser.getRole().equals("ADMIN")) {
                        List<String> rentedIds2 = rentalJsonRepository.getRentedVehicleIds();
                        List<Vehicle> allVehicles = repo.findAll();

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
                        repo.save(newVehicle);
                        System.out.println("Vehicle added successfully.");
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;
                case 9:
                    if (currentUser.getRole().equals("ADMIN")) {
                        System.out.println("Enter ID of vehicle to remove: ");
                        String removeID = scanner.next();
                        repo.deleteById(removeID);
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