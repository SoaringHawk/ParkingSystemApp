package com.example.parkingreservationapp.models;

public class TruckSpotFactory {

    public static ParkingSpot createSpot(String id, String location) {


            return new TruckParking(id, location); // Creates a truck parking spot


    }
}
