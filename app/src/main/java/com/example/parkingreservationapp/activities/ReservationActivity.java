package com.example.parkingreservationapp.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.parkingreservationapp.R;
import com.example.parkingreservationapp.models.CarParking; // Using concrete subclass
import com.example.parkingreservationapp.models.OccupiedState;
import com.example.parkingreservationapp.models.ParkingSpot;
import com.example.parkingreservationapp.models.Reservation;
import com.example.parkingreservationapp.models.User;
import com.example.parkingreservationapp.utils.TimeUtils;
import java.util.Calendar;
import java.util.Date;

/**
 * Activity for making a parking reservation.
 *
 * This activity collects user input, allows the user to select a reservation date,
 * start time, and end time, and creates a Reservation object that is passed to PaymentActivity.
 * After confirming the reservation, an electronic receipt is displayed via an AlertDialog,
 * and the app then navigates back to the MainActivity.
 *
 * Note: This activity is linked to its layout file (activity_reservation.xml) via setContentView.
 */
public class ReservationActivity extends AppCompatActivity {

    private TextView tvSpotInfo;
    private EditText etName, etLicensePlate;
    private Button btnBack, btnSelectDate, btnSelectStartTime, btnSelectEndTime, btnConfirm;
    private String spotId;
    private Date selectedDate; // The selected date for the reservation
    private int startHour, startMinute, endHour, endMinute;
    private Date startTime, endTime; // Start and end times for the reservation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind this activity to its corresponding layout file
        setContentView(R.layout.activity_reservation);

        // Retrieve the parking spot ID passed from the previous activity
        spotId = getIntent().getStringExtra("spotId");

        // Initialize UI components from the layout
        initViews();

        // Set up the back button to finish this activity and return to the previous screen
        btnBack.setOnClickListener(v -> finish());

        // Set up date and time pickers
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnSelectStartTime.setOnClickListener(v -> showStartTimePicker());
        btnSelectEndTime.setOnClickListener(v -> showEndTimePicker());

        // Set up the confirmation button to create a reservation when clicked
        btnConfirm.setOnClickListener(v -> confirmReservation());
    }

    /**
     * Initialize the views by finding them in the layout.
     */
    private void initViews() {
        tvSpotInfo = findViewById(R.id.tvSpotInfo);
        etName = findViewById(R.id.etName);
        etLicensePlate = findViewById(R.id.etLicensePlate);
        btnBack = findViewById(R.id.btnBack);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectStartTime = findViewById(R.id.btnSelectStartTime);
        btnSelectEndTime = findViewById(R.id.btnSelectEndTime);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Display the parking spot information retrieved from the previous activity
        tvSpotInfo.setText("Reserve parking spot: " + spotId);
    }

    /**
     * Display a DatePickerDialog to allow the user to select a reservation date.
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, month, dayOfMonth);
                    selectedDate = selectedCalendar.getTime();
                    // Update the date button with the selected date
                    btnSelectDate.setText(TimeUtils.formatDate(selectedDate));
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    /**
     * Display a TimePickerDialog to allow the user to select the start time of the reservation.
     */
    private void showStartTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    startHour = hourOfDay;
                    startMinute = minute;
                    // Update the start time button with the selected time
                    btnSelectStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    // Set startTime using the selected date and time
                    if (selectedDate != null) {
                        Calendar startCalendar = Calendar.getInstance();
                        startCalendar.setTime(selectedDate);
                        startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
                        startCalendar.set(Calendar.MINUTE, startMinute);
                        startTime = startCalendar.getTime();
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        );
        timePickerDialog.show();
    }

    /**
     * Display a TimePickerDialog to allow the user to select the end time of the reservation.
     */
    private void showEndTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    endHour = hourOfDay;
                    endMinute = minute;
                    // Update the end time button with the selected time
                    btnSelectEndTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    // Set endTime using the selected date and time
                    if (selectedDate != null) {
                        Calendar endCalendar = Calendar.getInstance();
                        endCalendar.setTime(selectedDate);
                        endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
                        endCalendar.set(Calendar.MINUTE, endMinute);
                        endTime = endCalendar.getTime();
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        );
        timePickerDialog.show();
    }

    /**
     * Confirm the reservation by validating user input and creating a Reservation object.
     * After creation, an AlertDialog displays the electronic receipt and then navigates back to the main activity.
     */
    private void confirmReservation() {
        String name = etName.getText().toString().trim();
        String licensePlate = etLicensePlate.getText().toString().trim();

        if (name.isEmpty() || licensePlate.isEmpty()) {
            Toast.makeText(this, "Please enter your name and license plate number.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!name.matches("^[a-zA-Z ]+$")) {
            Toast.makeText(this, "Name cannot contain numbers or symbols.", Toast.LENGTH_SHORT).show();
            return;
        }

        licensePlate = licensePlate.toUpperCase();

        if (!licensePlate.matches("^[A-Z0-9]{6,8}$")) {
            Toast.makeText(this, "License plate must be 6–8 characters (A-Z, 0–9 only).", Toast.LENGTH_SHORT).show();
            return;
        }


        if (selectedDate == null) {
            Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (btnSelectStartTime.getText().toString().equals("Select Start Time") || startTime == null) {
            Toast.makeText(this, "Please select a start time.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (btnSelectEndTime.getText().toString().equals("Select End Time") || endTime == null) {
            Toast.makeText(this, "Please select an end time.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!endTime.after(startTime)) {
            Toast.makeText(this, "End time must be after start time.", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(name, licensePlate);
        long durationInMillis = endTime.getTime() - startTime.getTime();
        double durationInHours = durationInMillis / (1000.0 * 60.0 * 60.0);
        double price = durationInHours * 10;

        ParkingSpot parkingSpot = new CarParking(spotId, "Sample Location");
        parkingSpot.occupied();

        Reservation reservation = new Reservation(user, parkingSpot, startTime, endTime, price);

        // Go directly to PaymentActivity with the reservation
        showReceiptDialog(reservation);
    }

    /**
     * Displays an AlertDialog containing the reservation details as an electronic receipt.
     * Once the user confirms, navigates back to the main activity.
     *
     * @param reservation the Reservation object to display
     */
    private void showReceiptDialog(Reservation reservation) {
        String message = "Reservation Receipt:\n\n" +
                "Parking Spot: " + reservation.getParkingSpot().getId() + "\n" +
                "User: " + reservation.getUser().getName() + "\n" +
                "License Plate: " + reservation.getUser().getLicensePlate() + "\n" +
                "Start Time: " + TimeUtils.formatDateTime(reservation.getStartTime()) + "\n" +
                "End Time: " + TimeUtils.formatDateTime(reservation.getEndTime()) + "\n" +
                "Price: " + reservation.getPrice();

        new AlertDialog.Builder(this)
                .setTitle("Electronic Receipt")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    Log.d("ReservationActivity", "Launching PaymentActivity...");
                    // After showing the receipt, navigate back to the main activity.
                    Intent intent = new Intent(ReservationActivity.this, PaymentActivity.class);
                    intent.putExtra("reservationId", reservation.getId());
                    startActivityForResult(intent, 2);
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            // Pass result back to MainActivity
            String spotId = data.getStringExtra("spotId");
            Intent resultIntent = new Intent();
            resultIntent.putExtra("spotId", spotId);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
