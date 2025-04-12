package com.example.parkingreservationapp.models;

public class OccupiedState implements IParkingState{

    @Override
    public void Available(ParkingSpot spot) {
        spot.setAvailable(new AvailableState());
    }

    @Override
    public void occupied(ParkingSpot spot) {
        spot.setAvailable(new OccupiedState());
    }
}
