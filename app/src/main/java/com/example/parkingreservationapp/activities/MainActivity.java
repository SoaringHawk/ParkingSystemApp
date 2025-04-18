package com.example.parkingreservationapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.parkingreservationapp.R;
import com.example.parkingreservationapp.adapters.ParkingSpotAdapter;
import com.example.parkingreservationapp.models.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RESERVATION_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private ParkingSpotAdapter adapter;
    private List<ParkingSpot> parkingSpots = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        // Initialize parking spots
        initializeParkingSpots();

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParkingSpotAdapter(parkingSpots, this, spot -> {
            Intent intent = new Intent(MainActivity.this, ReservationPaymentActivity.class);
            intent.putExtra("spotId", spot.getId());
            startActivityForResult(intent, RESERVATION_REQUEST_CODE);
        });
        recyclerView.setAdapter(adapter);

        // Button to view reservations
        Button btnViewReservations = findViewById(R.id.btnViewReservations);
        btnViewReservations.setOnClickListener(v -> {
            // TODO: Implement viewing of reservations
        });
    }

    private void initializeParkingSpots() {
        ParkingLotManager manager = ParkingLotManager.getInstance();
        parkingSpots = manager.getSpots();

        if (parkingSpots.isEmpty()) {
            manager.addSpot(CarSpotFactory.createSpot("A1", "Floor 1 Zone A"));
            manager.addSpot(CarSpotFactory.createSpot("A2", "Floor 1 Zone A"));
            manager.addSpot(CarSpotFactory.createSpot("B1", "Floor 1 Zone B"));
            manager.addSpot(CarSpotFactory.createSpot("B2", "Floor 1 Zone B"));
            manager.addSpot(CarSpotFactory.createSpot("C1", "Floor 2 Zone C"));
            manager.addSpot(CarSpotFactory.createSpot("C2", "Floor 2 Zone C"));

            parkingSpots = manager.getSpots();
            saveParkingSpotsToFirestore(parkingSpots);
        }
    }

    private void saveParkingSpotsToFirestore(List<ParkingSpot> spots) {
        for (ParkingSpot spot : spots) {
            ParkingSpotFirebasemodel model = new ParkingSpotFirebasemodel(
                    spot.getId(),
                    spot.getLocation(),
                    spot.isAvailable(),
                    spot.getReservedUntil()
            );

            db.collection("parkingSpots")
                    .document(model.getId())
                    .set(model)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Saved: " + model.getId()))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error saving " + model.getId(), e));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESERVATION_REQUEST_CODE && resultCode == RESULT_OK) {
            String updatedSpotId = data.getStringExtra("spotId");
            updateLocalSpotStatus(updatedSpotId);
        }
    }

    private void updateLocalSpotStatus(String spotId) {
        for (int i = 0; i < parkingSpots.size(); i++) {
            ParkingSpot spot = parkingSpots.get(i);
            if (spot.getId().equals(spotId)) {
                spot.occupied(); // Mark as occupied
                adapter.notifyItemChanged(i); // Update RecyclerView

                // Also update in ParkingLotManager
                ParkingLotManager.getInstance().updateSpotAvailability(spotId, false);
                break;
            }
        }
    }
}