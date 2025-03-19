package org.example.app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FirstVehicleRepo implements IVehicleRepository {
    public List<Vehicle> vehicleList = new ArrayList<>();

    @Override
    public void load(){
        vehicleList.clear();
        try(BufferedReader br = new BufferedReader(new FileReader("data/vehicle_list.csv"))){
            String line = br.readLine();
            while(line != null) {
                String[] data = line.split(";");
                if (data.length == 6) {
                    Car newCar = new Car(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]), Float.parseFloat(data[4]), Boolean.parseBoolean(data[5]));
                    vehicleList.add(newCar);
                } else if (data.length == 7) {
                    Motorcycle newMoto = new Motorcycle(data[0], data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), Float.parseFloat(data[5]), Boolean.parseBoolean(data[6]));
                    vehicleList.add(newMoto);
                } else {
                    throw new InvalidObjectException("Invalid data in file");
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/vehicle_list.csv", false))){
            for(Vehicle v: vehicleList){
                bw.write(v.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rentVehicle(int id) {
        for (Vehicle v : vehicleList) {
            if (v.getSystemID() == id && !v.isRented()) {
                v.setRented(true);
                save();
                System.out.println("Vehicle rented: " + v.getBrand() + " " + v.getModel());
                return;
            }
        }
        System.out.println("Vehicle not found or already rented.");
    }

    @Override
    public void returnVehicle(int id) {
        for (Vehicle v : vehicleList) {
            if (v.getSystemID() == id && v.isRented()) {
                v.setRented(false);
                save();
                System.out.println("Vehicle returned: " + v.getBrand() + " " + v.getModel());
                return;
            }
        }
        System.out.println("Vehicle not found or not rented.");
    }

    @Override
    public List<Vehicle> getVehicles(){
        List<Vehicle> vehicleListCloned = new ArrayList<>();
        for(Vehicle v : vehicleList){
            vehicleListCloned.add(v.cloneVehicle());
        }
        return vehicleListCloned;
    }

    @Override
    public void addVehicle(Vehicle v){
        vehicleList.add(v);
        save();
    }

    @Override
    public void removeVehicle(int id){
        for (Vehicle v : vehicleList) {
            if (v.getSystemID() == id) {
                vehicleList.remove(v);
                save();
                return;
            }
        }
    }
}
