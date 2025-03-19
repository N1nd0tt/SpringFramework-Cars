package org.example.app;

import java.util.List;
import java.util.Scanner;

public class UserInterface {
    public static FirstVehicleRepo repo = new FirstVehicleRepo();
    public static UserRepository userRepository = new UserRepository(repo);
    public static Authentication authentication = new Authentication(userRepository);
    public static Scanner scanner = new Scanner(System.in);
    public static User currentUser;

    public static void main(String[] args) {
        repo.load();
        userRepository.load();

        while (true) {
            System.out.println("Enter login: ");
            String login = scanner.next();
            System.out.println("Enter password: ");
            String password = scanner.next();

            if (authentication.login(login, password)) {
                List<User> users = userRepository.getUsers();
                for (User user : users) {
                    if (user.getLogin().equals(login)) {
                        currentUser = user;
                        break;
                    }
                }
                break;
            } else {
                System.out.println("Invalid login or password. Try again.");
            }
        }

        while (true) {
            System.out.println("Choose an option: 1 - get vehicles, 2 - rent a vehicle, 3 - return a vehicle, 4 - test equal and hash, 5 - get user info, 6 - exit");
            if (currentUser.isAdmin()) {
                System.out.println("Admin options: 7 - get users, 8 - add vehicle, 9 - remove vehicle");
            }
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    List<Vehicle> vehicleList = repo.getVehicles();
                    System.out.println("Vehicles in repo:");
                    for (Vehicle v : vehicleList) {
                        System.out.println(v);
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
                    List<Vehicle> vehicleList2 = repo.getVehicles();
                    System.out.println("Hashcode:");
                    System.out.println("Hashcode of 0 elem: " + vehicleList2.get(0).hashCode());
                    Vehicle vehicle_copy = vehicleList2.get(0).cloneVehicle();
                    System.out.println("Hashcode of copy: " + vehicle_copy.hashCode());
                    System.out.println("Hashcode of 1 elem: " + vehicleList2.get(1).hashCode());
                    System.out.println("Equals:");
                    System.out.println("Original == copy : " + vehicleList2.get(0).equals(vehicle_copy));
                    System.out.println("0 elem (Car) == 1 elem (car) : " + vehicleList2.get(0).equals(vehicleList2.get(1)));
                    System.out.println("0 elem (Car) == 1 elem (moto) : " + vehicleList2.get(0).equals(vehicleList2.get(4)));
                    break;
                case 5:
                    System.out.println(userRepository.getUser(currentUser.getId()));
                    break;
                case 6:
                    System.out.println("Saving data...");
                    repo.save();
                    System.out.println("Data saved. Exiting...");
                    return;
                case 7:
                    if (currentUser.isAdmin()) {
                        List<User> userList = userRepository.getUsers();
                        System.out.println("Users in repo:");
                        for (User u : userList) {
                            System.out.println(u);
                        }
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;
                case 8:
                    if (currentUser.isAdmin()) {
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
                    if (currentUser.isAdmin()) {
                        System.out.println("Enter ID of vehicle to remove: ");
                        int removeID = scanner.nextInt();
                        repo.removeVehicle(removeID);
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