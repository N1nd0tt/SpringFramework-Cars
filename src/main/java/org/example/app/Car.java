package org.example.app;

import java.util.Objects;

public class Car extends Vehicle{
    public Car(String brand, String model, int year, int systemID, float price, boolean rented) {
        super(brand, model, year, systemID, price, rented);
    }

    public Car cloneVehicle(){
        return new Car(this.getBrand(), this.getModel(), this.getYear(), this.getSystemID(), this.getPrice(), this.isRented());
    }
    public boolean equals(Object o){
        if(this.getClass() == o.getClass()){
            return Objects.equals(this.getBrand(), ((Car) o).getBrand()) && Objects.equals(this.getModel(), ((Car) o).getModel()) &&
                    this.getYear() == ((Car) o).getYear() && this.getSystemID() == ((Car) o).getSystemID() &&
                    this.getPrice() == ((Car) o).getPrice() && this.isRented() == ((Car) o).isRented();
        }
        return false;
    }
    public int hashCode(){
        return getBrand().hashCode() * getSystemID() + getModel().hashCode() - getYear();
    }
}
