package com.example.parkingreservationapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.parkingreservationapp.R;
import com.example.parkingreservationapp.models.Reservation;
import com.example.parkingreservationapp.utils.TimeUtils;

// Activity for handling parking reservation payment
public class PaymentActivity extends AppCompatActivity {
    private TextView tvReservationDetails;
    private Button btnPay;
    private Reservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Retrieve reservation information from the intent
        reservation = (Reservation) getIntent().getSerializableExtra("reservation");

        // Initialize the UI components
        initViews();

        // Set up the payment button click listener
        btnPay.setOnClickListener(v -> completePayment());
    }

    // Initializes views and displays reservation details
    private void initViews() {
        tvReservationDetails = findViewById(R.id.tvReservationDetails);
        btnPay = findViewById(R.id.btnPay);

        // Display reservation details
        String details = "Your parking spot: " + reservation.getParkingSpot().getId() + "\n" +
                "Time: " + TimeUtils.formatDateTime(reservation.getStartTime()) + " - " +
                TimeUtils.formatTime(reservation.getEndTime()) + "\n" +
                "License Plate: " + reservation.getUser().getLicensePlate() + "\n" +
                "Price: ¥" + reservation.getPrice();

        tvReservationDetails.setText(details);
    }

    // Handles payment completion
    private void completePayment() {
        // Simulate payment processing
        reservation.setPaid(true);

        // Simulate saving reservation information (in a real application this should be saved to a database)
        Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity
    }
}
