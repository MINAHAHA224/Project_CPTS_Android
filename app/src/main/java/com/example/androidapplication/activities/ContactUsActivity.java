package com.example.androidapplication.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidapplication.R;

public class ContactUsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        // Nút Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}