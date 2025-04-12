package com.example.parkingreservationapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.parkingreservationapp.R;

/**
 * AdminLoginActivity - Allows an admin to log in using a password.
 * On successful login, navigates to AdminDashboardActivity.
 */
public class AdminLoginActivity extends AppCompatActivity {

    private EditText etPassword;
    private Button btnLogin;
    // Define a sample admin password
    private static final String ADMIN_PASSWORD = "balala";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to the admin login layout file (activity_admin_login.xml)
        setContentView(R.layout.activity_admin_login);

        // Initialize UI components from the layout
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // When the Login button is clicked, validate the password
        btnLogin.setOnClickListener(v -> {
            String enteredPassword = etPassword.getText().toString().trim();
            if (enteredPassword.equals(ADMIN_PASSWORD)) {
                // Password is correct; navigate to AdminDashboardActivity
                Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Incorrect password; show error message
                Toast.makeText(AdminLoginActivity.this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

