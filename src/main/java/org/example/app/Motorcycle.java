package org.example.app;

import java.util.Locale;
import java.util.Objects;

public class Motorcycle extends Vehicle{
    private final String category;

    public Motorcycle(String brand, String model, String category, int year, int systemID, float price, boolean rented) {
        super(brand, model, year, systemID, price, rented);
        this.category = category;
    }

    public Motorcycle cloneVehicle(){
        return new Motorcycle(this.getBrand(), this.getModel(), this.category, this.getYear(), this.getSystemID(), this.getPrice(), this.isRented());
    }
    public boolean equals(Object o){
        if(this.getClass() == o.getClass()){
            return Objects.equals(this.getBrand(), ((Motorcycle) o).getBrand()) && Objects.equals(this.getModel(), ((Motorcycle) o).getModel()) &&
                    this.getYear() == ((Motorcycle) o).getYear() && this.getSystemID() == ((Motorcycle) o).getSystemID() &&
                    this.getPrice() == ((Motorcycle) o).getPrice() && this.isRented() == ((Motorcycle) o).isRented();
        }
        return false;
    }

    public int hashCode(){
        return getBrand().hashCode() * getSystemID() + getModel().hashCode() - getYear();
    }

    @Override
    public String toCSV() {
        return String.format(Locale.US, "%s;%s;%s;%d;%d;%.2f;%b", this.getBrand(), this.getModel(), category, this.getYear(), this.getSystemID(), this.getPrice(), this.isRented());
    }

    @Override
    public String toString(){
        return String.format("Brand: %s,\nModel: %s,\nCategory: %s,\nYear: %d,\nSystem ID: %d,\nPrice: %.2f,\nRented: %b\n", this.getBrand(), this.getModel(), category, this.getYear(), this.getSystemID(), this.getPrice(), this.isRented());
    }
}
