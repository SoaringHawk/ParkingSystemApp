package com.example.parkingreservationapp.models;

import java.util.Date;

public class ParkingSpotFirebasemodel {
    private String id;
    private String location;
    private boolean available;
    private Date reservedUntil;

    public ParkingSpotFirebasemodel() {
        // Required empty constructor for Firestore
    }

    public ParkingSpotFirebasemodel(String id, String location, boolean available, Date reservedUntil) {
        this.id = id;
        this.location = location;
        this.available = available;
        this.reservedUntil = reservedUntil;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Date getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(Date reservedUntil) {
        this.reservedUntil = reservedUntil;
    }
}
