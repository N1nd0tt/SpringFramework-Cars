package org.example.app;

import java.util.Locale;

public abstract class Vehicle {
    private final String brand;
    private final String model;
    private final int year;
    private final int systemID;
    private final float price;
    private boolean rented;

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public int getSystemID() {
        return systemID;
    }

    public float getPrice() {
        return price;
    }


    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public Vehicle(String brand, String model, int year, int systemID, float price, boolean rented) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.systemID = systemID;
        this.price = price;
        this.rented = rented;
    }

    public abstract Vehicle cloneVehicle();
    public abstract boolean equals(Object o);
    public abstract int hashCode();

    public String toCSV(){
        return String.format(Locale.US, "%s;%s;%d;%d;%.2f;%b", brand, model, year, systemID, price, rented);
    }

    public String toString(){
        return String.format("Brand: %s,\nModel: %s,\nYear: %d,\nSystem ID: %d,\nPrice: %.2f,\nRented: %b\n", brand, model, year, systemID, price, rented);
    }
}
