package org.example.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository implements IUserRepository{
    public List<User> users = new ArrayList<>();
    public FirstVehicleRepo vehicleRepo;

    public UserRepository(FirstVehicleRepo vehicleRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    public void load(){
        users.clear();
        try(BufferedReader br = new BufferedReader(new FileReader("data/user_list.csv"))){
            String line = br.readLine();
            while(line != null) {
                String[] data = line.split(";");
                if(!Objects.equals(data[4], "Null")){
                    Vehicle rentedVehicle = vehicleRepo.getVehicles().get(Integer.parseInt(data[4]));
                    User newUser = new User(data[0], data[1], Integer.parseInt(data[2]), Boolean.parseBoolean(data[3]), rentedVehicle);
                    users.add(newUser);
                } else {
                    User newUser = new User(data[0], data[1], Integer.parseInt(data[2]), Boolean.parseBoolean(data[3]), null);
                    users.add(newUser);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<User> getUsers() {
        List<User> usersCloned = new ArrayList<>();
        for(User u : users){
            usersCloned.add(u.cloneUser());
        }
        return usersCloned;
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }
}
