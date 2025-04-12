package com.example.parkingreservationapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.parkingreservationapp.R;

/**
 * WelcomeActivity - The starting page of the Parking Reservation App.
 * Users choose whether to continue as an Admin or a Customer.
 */
public class WelcomeActivity extends AppCompatActivity {

    private Button btnAdmin, btnCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to the welcome layout file (activity_welcome.xml)
        setContentView(R.layout.activity_welcome);

        // Initialize UI components from the layout
        btnAdmin = findViewById(R.id.btnAdmin);
        btnCustomer = findViewById(R.id.btnCustomer);

        // When Admin button is clicked, navigate to AdminLoginActivity
        btnAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });

        // When Customer button is clicked, navigate to MainActivity (customer main page)
        btnCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}

