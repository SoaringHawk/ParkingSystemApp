package com.example.parkingreservationapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkingreservationapp.R;
import org.json.JSONObject;

public class StripeCheckoutActivity extends AppCompatActivity {
    private int amount;
    private String spotId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_checkout);

        amount = getIntent().getIntExtra("amount", 0);
        spotId = getIntent().getStringExtra("spotId");

        startCheckout();
    }

    private void startCheckout() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://10.0.2.2:4242/create-checkout-session";

        JSONObject body = new JSONObject();
        try {
            body.put("amount", amount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                response -> {
                    try {
                        String checkoutUrl = response.getString("url");
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(checkoutUrl));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to start checkout", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error creating checkout session", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }
}
