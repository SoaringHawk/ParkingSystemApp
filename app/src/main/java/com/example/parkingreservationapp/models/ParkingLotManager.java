package com.example.parkingreservationapp.models;

import com.example.parkingreservationapp.models.ParkingSpot;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotManager {


    private static ParkingLotManager instance;
    private List<ParkingSpot> spots = new ArrayList<>();

    private ParkingLotManager() {}

    public static ParkingLotManager getInstance() {
        if (instance == null) {
            instance = new ParkingLotManager();
        }
        return instance;
    }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public void displaySpots() {
        for (ParkingSpot spot : spots) {
            System.out.println(spot.getDescription());
        }
    }
    public List<ParkingSpot> getSpots() {
        return spots;
    }
}
