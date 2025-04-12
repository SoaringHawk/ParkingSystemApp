package com.example.parkingreservationapp.models;

import java.util.Date;

public abstract class ParkingSpot {
    private String id;
    private String location;
    private boolean isAvailable;
    private Date reservedUntil;

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
        if (parkingState instanceof OccupiedState){
            return false;
        }else{
            return true;
        }
    }

    public void setAvailable(IParkingState state) {
        parkingState = state;
    }

    public void occupied(){
        parkingState.occupied(this);
    }

    public void Available(){
        parkingState.Available(this);
    }

    public Date getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(Date reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public abstract String getDescription();

}
