package com.example.parkingreservationapp.utils;

import android.util.Log;
import com.example.parkingreservationapp.models.ParkingSpot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * DataLoader - Handles loading and saving of parking spots data with Firebase.
 */
public class DataLoader {

    private static final String TAG = "DataLoader";
    private static final String PARKING_SPOTS_NODE = "parking_spots";
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference parkingSpotsRef = database.getReference(PARKING_SPOTS_NODE);

    // This method loads parking spots from Firebase Realtime Database
    public static void loadParkingSpots(OnDataLoadedListener listener) {
        parkingSpotsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ParkingSpot> parkingSpots = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ParkingSpot spot = snapshot.getValue(ParkingSpot.class);
                    if (spot != null) {
                        parkingSpots.add(spot);
                    }
                }
                listener.onDataLoaded(parkingSpots);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading data: " + databaseError.getMessage());
                listener.onDataLoadFailed(databaseError.getMessage());
            }
        });
    }

    // This method saves a list of parking spots to Firebase
    public static void saveParkingSpots(List<ParkingSpot> parkingSpots) {
        for (ParkingSpot spot : parkingSpots) {
            String spotId = spot.getId();
            parkingSpotsRef.child(spotId).setValue(spot)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Parking spot saved: " + spotId))
                    .addOnFailureListener(e -> Log.e(TAG, "Error saving parking spot: " + e.getMessage()));
        }
    }

    // Interface to notify when data is loaded from Firebase
    public interface OnDataLoadedListener {
        void onDataLoaded(List<ParkingSpot> parkingSpots);
        void onDataLoadFailed(String error);
    }
}
