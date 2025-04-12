package com.example.parkingreservationapp.models;

// Truck parking spot class, extends from ParkingSpot
public class TruckParking extends ParkingSpot {
    // Constructor with ID and location
    public TruckParking(String id, String location) {
        super(id, location);
    }

    // Method to get a description of the truck parking spot
    @Override
    public String getDescription() {
        return "Truck Parking Spot " + getId() + " at " + getLocation();
    }
}
