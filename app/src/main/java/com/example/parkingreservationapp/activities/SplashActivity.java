package com.example.parkingreservationapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.example.parkingreservationapp.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        LottieAnimationView animationView = findViewById(R.id.lottieAnimationView);
        animationView.setSpeed(0.5f);
        animationView.playAnimation();


        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            finish(); // no return
        }, SPLASH_DURATION);
    }
}
