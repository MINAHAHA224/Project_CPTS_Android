package com.example.androidapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidapplication.R;
import com.example.androidapplication.utils.SharedPrefManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Cần tạo layout này

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Check if user is logged in
            if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                // User is logged in, go to MainActivity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // User is not logged in, go to AuthActivity
                startActivity(new Intent(SplashActivity.this, AuthActivity.class));
            }
            finish();
        }, 2000); // 2 seconds delay
    }
}