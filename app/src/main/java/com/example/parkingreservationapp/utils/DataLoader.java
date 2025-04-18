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
import com.google.firebase.firestore.*; // Cloud Firestore


/**
 * DataLoader - Handles loading and saving of parking spots data with Firebase.
 */
public class DataLoader {

    private static final String TAG = "DataLoader";
    private static final String PARKING_SPOTS_COLLECTION = "parkingSpots";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void loadParkingSpots(OnDataLoadedListener listener) {
        db.collection(PARKING_SPOTS_COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ParkingSpot> parkingSpots = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        ParkingSpot spot = document.toObject(ParkingSpot.class);
                        if (spot != null) {
                            parkingSpots.add(spot);
                        }
                    }
                    listener.onDataLoaded(parkingSpots);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading data: " + e.getMessage());
                    listener.onDataLoadFailed(e.getMessage());
                });
    }

    public static void saveParkingSpots(List<ParkingSpot> parkingSpots) {
        for (ParkingSpot spot : parkingSpots) {
            String spotId = spot.getId();
            db.collection(PARKING_SPOTS_COLLECTION)
                    .document(spotId)
                    .set(spot)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Parking spot saved: " + spotId))
                    .addOnFailureListener(e -> Log.e(TAG, "Error saving parking spot: " + e.getMessage()));
        }
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(List<ParkingSpot> parkingSpots);
        void onDataLoadFailed(String error);
    }
}

