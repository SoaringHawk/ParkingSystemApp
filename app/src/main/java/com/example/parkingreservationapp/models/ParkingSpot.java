package com.example.parkingreservationapp.models;

import java.io.Serializable;
import java.util.Date;

public abstract class ParkingSpot implements Serializable {
    private String id;
    private String location;
    private boolean isAvailable;
    private Date reservedUntil;
    private String type; // Add the type field

    IParkingState parkingState;

    public ParkingSpot(String id, String location) {
        this.id = id;
        this.location = location;
        this.isAvailable = true;
        this.parkingState = new AvailableState();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public boolean isAvailable() {
        return parkingState instanceof AvailableState;
    }

    public void setAvailable(IParkingState state) {
        parkingState = state;
    }

    public void occupied() {
        parkingState.occupied(this);
    }

    public void Available() {
        parkingState.Available(this);
    }

    public Date getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(Date reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public abstract String getDescription();
}

