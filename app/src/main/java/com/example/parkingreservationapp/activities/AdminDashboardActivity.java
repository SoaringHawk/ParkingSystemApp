package com.example.parkingreservationapp.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.parkingreservationapp.R;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminDashboardActivity - Displays the dashboard for admins,
 * showing a list of occupied parking spots.
 */
public class AdminDashboardActivity extends AppCompatActivity {

    private ListView listViewOccupied;
    // For demonstration, a dummy list of occupied parking spots is used.
    private List<String> occupiedSpots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to the admin dashboard layout file (activity_admin_dashboard.xml)
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize UI component from the layout
        listViewOccupied = findViewById(R.id.listViewOccupied);

        // Initialize dummy data for occupied parking spots
        occupiedSpots = new ArrayList<>();
        occupiedSpots.add("Parking Spot A1 - Occupied");
        occupiedSpots.add("Parking Spot B2 - Occupied");
        occupiedSpots.add("Parking Spot C3 - Occupied");

        // Set up an ArrayAdapter to display the occupied spots in a ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, occupiedSpots);
        listViewOccupied.setAdapter(adapter);
    }
}
