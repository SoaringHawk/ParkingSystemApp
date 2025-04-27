package com.example.parkingreservationapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.parkingreservationapp.R;
import com.example.parkingreservationapp.models.Reservation;
import com.example.parkingreservationapp.utils.TimeUtils;
import com.example.parkingreservationapp.PaymentAdapterPackage.*;


// Activity for handling parking reservation payment
public class PaymentActivity extends AppCompatActivity {
    private TextView tvReservationDetails;
    private Button btnPay;
    private RadioGroup paymentMethodGroup;
    private Reservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PaymentActivity", "onCreate: PaymentActivity started");
        setContentView(R.layout.activity_payment);
        Toast.makeText(this, "PaymentActivity loaded", Toast.LENGTH_SHORT).show();

        reservation = (Reservation) getIntent().getSerializableExtra("reservation");

        initViews();

        btnPay.setOnClickListener(v -> completePayment());
    }

    private void initViews() {
        tvReservationDetails = findViewById(R.id.tvReservationDetails);
        btnPay = findViewById(R.id.btnPay);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup); // Link to RadioGroup

        String details = "Your parking spot: " + reservation.getParkingSpot().getId() + "\n" +
                "Time: " + TimeUtils.formatDateTime(reservation.getStartTime()) + " - " +
                TimeUtils.formatTime(reservation.getEndTime()) + "\n" +
                "License Plate: " + reservation.getUser().getLicensePlate() + "\n" +
                "Price: $" + reservation.getPrice();

        tvReservationDetails.setText(details);
    }

    private String getSelectedPaymentType() {
        int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.rbCash) {
            return "cash";
        } else if (selectedId == R.id.rbCreditCard) {
            return "creditcard";
        }
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

        //Set result to send back to ReservationActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("spotId", reservation.getParkingSpot().getId());
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();

        finish();
    }
}
