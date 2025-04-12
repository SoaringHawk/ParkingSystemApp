package com.example.parkingreservationapp.models;

public class CarSpotFactory {
    public static ParkingSpot createSpot(String id, String location) {

        return new CarParking(id, location); // Creates a car parking spot


    }
}
