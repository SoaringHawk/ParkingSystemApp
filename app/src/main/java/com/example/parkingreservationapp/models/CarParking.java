package com.example.parkingreservationapp.models
        ;

import java.io.Serializable;

// Car parking spot class, extends from ParkingSpot
public class CarParking extends ParkingSpot implements Serializable {
    // Constructor with ID and location

    public CarParking() {
        super("", ""); // This is required for Firestore deserialization
    }
    public CarParking(String id, String location) {
        super(id, location);
    }

    // Method to get a description of the car parking spot
    @Override
    public String getDescription() {
        return "Car Parking Spot " + getId() + " at " + getLocation();
    }
}
