package com.example.parkingreservationapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.parkingreservationapp.R;
import com.example.parkingreservationapp.adapters.ParkingSpotAdapter;
import com.example.parkingreservationapp.models.CarSpotFactory;
import com.example.parkingreservationapp.models.OccupiedState;
import com.example.parkingreservationapp.models.ParkingLotManager;
import com.example.parkingreservationapp.models.ParkingSpot;

import java.util.ArrayList;
import java.util.List;

// Main activity handling the display of parking spots
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ParkingSpotAdapter adapter;
    private List<ParkingSpot> parkingSpots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize parking spots
        initializeParkingSpots();

        // Set up the RecyclerView for parking spots
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParkingSpotAdapter(parkingSpots, this, spot -> {
            // Start ReservationActivity when a spot is selected
            Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
            intent.putExtra("spotId", spot.getId());
            startActivityForResult(intent, 1);
        });
        recyclerView.setAdapter(adapter);

        // Button to view reservations
        Button btnViewReservations = findViewById(R.id.btnViewReservations);
        btnViewReservations.setOnClickListener(v -> {
            // TODO: Implement viewing of reservations
        });
    }

    // Initialize the list of parking spots
    private void initializeParkingSpots() {
        // Create parking spots using the factory pattern
        ParkingLotManager manager = ParkingLotManager.getInstance();
        parkingSpots = manager.getSpots(); // <-- use the shared list

        // Populate only once
        if (parkingSpots.isEmpty()) {
            manager.addSpot(CarSpotFactory.createSpot( "A1", "Floor 1 Zone A"));
            manager.addSpot(CarSpotFactory.createSpot( "A2", "Floor 1 Zone A"));
            manager.addSpot(CarSpotFactory.createSpot( "B1", "Floor 1 Zone B"));
            manager.addSpot(CarSpotFactory.createSpot( "B2", "Floor 1 Zone B"));
            manager.addSpot(CarSpotFactory.createSpot( "C1", "Floor 2 Zone C"));
            manager.addSpot(CarSpotFactory.createSpot( "C2", "Floor 2 Zone C"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String updatedSpotId = data.getStringExtra("spotId");

            // Find the parking spot by ID and update its status
            for (int i = 0; i < parkingSpots.size(); i++) {
                ParkingSpot spot = parkingSpots.get(i);
                if (spot.getId().equals(updatedSpotId)) {
                    spot.occupied(); // Update availability
                    adapter.notifyItemChanged(i); // Notify RecyclerView
                    break;
                }
            }
        }
    }
}

//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//}