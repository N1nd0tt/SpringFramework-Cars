package org.example.app;

public class User {
    private String login;
    private String password;

    private int id;
    private boolean isAdmin;
    private Vehicle rentedVehicle;

    public User(String login, String password, int id, boolean isAdmin, Vehicle rentedVehicle) {
        this.login = login;
        this.password = password;
        this.id = id;
        this.isAdmin = isAdmin;
        this.rentedVehicle = rentedVehicle;
    }

    public User cloneUser(){
        return new User(this.login, this.password, this.id, this.isAdmin, this.rentedVehicle);
    }

    public String toString(){
        if(rentedVehicle == null){
            return "User: " + login  + "\nID " + id + "\nAdmin: " + isAdmin + "\nRentedCar:\n" + "No rented car";
        }
        return "User: " + login  + "\nID: " + id + "\nAdmin: " + isAdmin + "\nRentedCar:\n" + rentedVehicle.toString();
    }
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Vehicle getRentedVehicle() {
        return rentedVehicle;
    }

    public void setRentedVehicle(Vehicle rentedVehicle) {
        this.rentedVehicle = rentedVehicle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

}
