//package com.example.parkingreservationapp;
//
//import com.example.parkingreservationapp.models.ParkingSpot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ParkingLevel extends ParkingSpot {
//
//
//        private List<ParkingSpot> spots = new ArrayList<>();
//
//        public void addSpot(ParkingSpot spot) {
//            spots.add(spot);
//        }
//
//        @Override
//        public String getDescription() {
//            StringBuilder description = new StringBuilder("Parking Level containing: \n");
//            for (ParkingSpot spot : spots) {
//                description.append("- ").append(spot.getDescription()).append("\n");
//            }
//            return description.toString();
//        }
//
//}
