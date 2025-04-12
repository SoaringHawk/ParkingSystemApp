package com.example.parkingreservationapp.models;

public interface IParkingState {

    public void Available(ParkingSpot spot);

    public void occupied(ParkingSpot spot);
}
