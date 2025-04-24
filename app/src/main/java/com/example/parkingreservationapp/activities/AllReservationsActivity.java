package com.example.parkingreservationapp.activities;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingreservationapp.R;
import com.example.parkingreservationapp.adapters.ReservationsAdapter;
import com.example.parkingreservationapp.models.CarParking;
import com.example.parkingreservationapp.models.Reservation;
import com.example.parkingreservationapp.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllReservationsActivity extends AppCompatActivity {

    private static final String TAG = "AllReservationsActivity";
    private RecyclerView recyclerView;
    private ReservationsAdapter adapter;
    private List<Reservation> reservationList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private String currentLicensePlate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reservations);

        // Initialize views
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.reservationsRecyclerView);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservationList = new ArrayList<>();
        adapter = new ReservationsAdapter(this, reservationList);
        recyclerView.setAdapter(adapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Show license plate input dialog
        showLicensePlateInputDialog();
    }

    private void showLicensePlateInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter License Plate");
        builder.setMessage("Please enter your vehicle's license plate number");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        input.setHint("e.g. ABC123");
        builder.setView(input);

        builder.setPositiveButton("Search", (dialog, which) -> {
            currentLicensePlate = input.getText().toString().trim().toUpperCase();
            if (!currentLicensePlate.isEmpty()) {
                loadReservations();
            } else {
                Toast.makeText(this, "Please enter a license plate", Toast.LENGTH_SHORT).show();
                showLicensePlateInputDialog(); // Show dialog again
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            finish(); // Close activity if canceled
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void loadReservations() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("reservations")
                .whereEqualTo("licensePlate", currentLicensePlate)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        reservationList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                Reservation reservation = parseReservation(document);
                                reservationList.add(reservation);
                                Log.d(TAG, "Added reservation: " + document.getId());
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing document " + document.getId(), e);
                            }
                        }

                        if (reservationList.isEmpty()) {
                            Toast.makeText(this,
                                    "No reservations found for plate: " + currentLicensePlate,
                                    Toast.LENGTH_LONG).show();
                        }

                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "Loaded " + reservationList.size() + " reservations");
                    } else {
                        Log.w(TAG, "Error getting documents", task.getException());
                        Toast.makeText(this, "Failed to load reservations", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Reservation parseReservation(QueryDocumentSnapshot document) {
        // Create User object
        User user = new User(
                document.getString("userId"),
                document.getString("licensePlate")
        );

        // Create ParkingSpot object
        CarParking parkingSpot = new CarParking(
                document.getString("spotId"),
                "Location not specified"
        );

        // Create and return Reservation
        Reservation reservation = new Reservation(
                user,
                parkingSpot,
                document.getDate("startTime"),
                document.getDate("endTime"),
                document.getDouble("price")
        );

        // Set additional fields
        reservation.setId(document.getId());
        reservation.setPaid(Boolean.TRUE.equals(document.getBoolean("paid")));

        return reservation;
    }
}