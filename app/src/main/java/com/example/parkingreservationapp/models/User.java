package com.example.parkingreservationapp.models;

// Class for user information
public class User {
    private String name;
    private String licensePlate;

    // Constructor for user
    public User(String name, String licensePlate) {
        this.name = name;
        this.licensePlate = licensePlate;
    }

    // Getters for user details
    public String getName() {
        return name;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
}
