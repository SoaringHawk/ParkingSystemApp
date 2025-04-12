package com.example.parkingreservationapp.models;

import java.io.Serializable;
import java.util.Date;

// Class for parking reservation details
public class Reservation implements Serializable {
    private String id;
    private User user;
    private ParkingSpot parkingSpot;
    private Date startTime;
    private Date endTime;
    private double price;
    private boolean isPaid;

    // Constructor for reservation
    public Reservation(User user, ParkingSpot parkingSpot, Date startTime, Date endTime, double price) {
        this.user = user;
        this.parkingSpot = parkingSpot;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.isPaid = false;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public double getPrice() {
        return price;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
