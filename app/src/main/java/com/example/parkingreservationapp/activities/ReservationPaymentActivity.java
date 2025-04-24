package com.example.parkingreservationapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.parkingreservationapp.R;
import com.example.parkingreservationapp.models.*;
import com.example.parkingreservationapp.PaymentAdapterPackage.*;
import com.example.parkingreservationapp.utils.TimeUtils;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReservationPaymentActivity extends AppCompatActivity {

    private TextView tvSpotInfo, tvReservationDetails;
    private EditText etName, etLicensePlate;
    private Button btnBack, btnSelectDate, btnSelectStartTime, btnSelectEndTime, btnConfirmAndPay;
    private RadioGroup paymentMethodGroup;

    private String spotId;
    private Date selectedDate;
    private int startHour, startMinute, endHour, endMinute;
    private Date startTime, endTime;
    private Reservation reservation;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_payment);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        spotId = getIntent().getStringExtra("spotId");

        initViews();

        btnBack.setOnClickListener(v -> finish());
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnSelectStartTime.setOnClickListener(v -> showStartTimePicker());
        btnSelectEndTime.setOnClickListener(v -> showEndTimePicker());

        btnConfirmAndPay.setOnClickListener(v -> {
            if (reservation == null) {
                confirmReservation();
            } else {
                completePayment();
            }
        });
    }

    private void initViews() {
        tvSpotInfo = findViewById(R.id.tvSpotInfo);
        tvReservationDetails = findViewById(R.id.tvReservationDetails);
        etName = findViewById(R.id.etName);
        etLicensePlate = findViewById(R.id.etLicensePlate);
        btnBack = findViewById(R.id.btnBack);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectStartTime = findViewById(R.id.btnSelectStartTime);
        btnSelectEndTime = findViewById(R.id.btnSelectEndTime);
        btnConfirmAndPay = findViewById(R.id.btnConfirmAndPay);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);

        tvSpotInfo.setText("Reserve parking spot: " + spotId);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, month, dayOfMonth);
                    selectedDate = selectedCalendar.getTime();
                    btnSelectDate.setText(TimeUtils.formatDate(selectedDate));
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showStartTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    startHour = hourOfDay;
                    startMinute = minute;
                    btnSelectStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
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

    private void showEndTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    endHour = hourOfDay;
                    endMinute = minute;
                    btnSelectEndTime.setText(String.format("%02d:%02d", hourOfDay, minute));
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

    private void confirmReservation() {
        String name = etName.getText().toString().trim();
        String licensePlate = etLicensePlate.getText().toString().trim();

        if (name.isEmpty() || licensePlate.isEmpty() || selectedDate == null || startTime == null || endTime == null) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!endTime.after(startTime)) {
            Toast.makeText(this, "End time must be after start time", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(name, licensePlate);
        double hours = (endTime.getTime() - startTime.getTime()) / (1000.0 * 60.0 * 60.0);
        double price = hours * 10;

        ParkingSpot parkingSpot = new CarParking(spotId, "Sample Location");
        parkingSpot.occupied();

        reservation = new Reservation(user, parkingSpot, startTime, endTime, price);

        String summary = "Parking Spot: " + spotId + "\n" +
                "User: " + name + "\n" +
                "License Plate: " + licensePlate + "\n" +
                "Start: " + TimeUtils.formatDateTime(startTime) + "\n" +
                "End: " + TimeUtils.formatDateTime(endTime) + "\n" +
                "Price: ¥" + price;

        tvReservationDetails.setText(summary);
        btnConfirmAndPay.setText("Complete Payment");
        Toast.makeText(this, "Reservation created. Please complete payment.", Toast.LENGTH_SHORT).show();
    }

    private String getSelectedPaymentType() {
        int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.rbCash) return "cash";
        if (selectedId == R.id.rbCreditCard) return "creditcard";
        return null;
    }

    private void completePayment() {
        String paymentType = getSelectedPaymentType();
        if (paymentType == null) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        PaymentProcessor processor = new PaymentAdapter(paymentType);
        processor.processPayment(reservation.getPrice());

        reservation.setPaid(true);

        // Update parking spot availability in Firestore
        updateParkingSpotInFirestore(reservation.getParkingSpot().getId(), false, endTime);

        // Save the reservation to Firestore
        saveReservationToFirestore();

        // Return the spot ID to MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("spotId", reservation.getParkingSpot().getId());
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, "Payment successful! Reservation created.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateParkingSpotInFirestore(String spotId, boolean isAvailable, Date reservedUntil) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("available", isAvailable);
        updates.put("reservedUntil", reservedUntil);

        FirebaseFirestore.getInstance()
                .collection("parkingSpots")
                .document(spotId)
                .update(updates)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Spot updated"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating spot", e));
    }

    private void saveReservationToFirestore() {
        if (reservation == null) return;

        Map<String, Object> reservationData = new HashMap<>();
        reservationData.put("spotId", reservation.getParkingSpot().getId());
        reservationData.put("userId", reservation.getUser().getName()); // You might want to use actual user ID
        reservationData.put("licensePlate", reservation.getUser().getLicensePlate());
        reservationData.put("startTime", reservation.getStartTime());
        reservationData.put("endTime", reservation.getEndTime());
        reservationData.put("price", reservation.getPrice());
        reservationData.put("paid", reservation.isPaid());
        reservationData.put("paymentTimestamp", FieldValue.serverTimestamp());
        reservationData.put("createdAt", FieldValue.serverTimestamp());

        db.collection("reservations")
                .add(reservationData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Reservation saved with ID: " + documentReference.getId());
                    // You could store this reservation ID in your reservation object if needed
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error saving reservation", e);
                    Toast.makeText(this, "Failed to save reservation", Toast.LENGTH_SHORT).show();
                });
    }
}