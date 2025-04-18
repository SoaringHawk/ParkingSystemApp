package com.example.parkingreservationapp.models;

import java.io.Serializable;

// Truck parking spot class, extends from ParkingSpot
public class TruckParking extends ParkingSpot implements Serializable {
    // Constructor with ID and location

    public TruckParking() {
        super("", ""); // This is required for Firestore deserialization
    }
    public TruckParking(String id, String location) {
        super(id, location);
    }

    // Method to get a description of the truck parking spot
    @Override
    public String getDescription() {
        return "Truck Parking Spot " + getId() + " at " + getLocation();
    }
}
