package com.example.androidapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrderSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        Button btnContinue = findViewById(R.id.btnContinueShopping);
        TextView btnViewOrder = findViewById(R.id.btnViewOrder);

        // 1. Sự kiện Tiếp tục mua sắm -> Về trang chủ (Tab Home hoặc Product)
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
            // Xóa hết activity cũ để người dùng không back lại màn hình đặt hàng được
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // 2. Sự kiện Xem đơn hàng -> Về trang chủ nhưng chuyển sang tab Orders
        btnViewOrder.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Gửi tín hiệu để MainActivity biết cần mở tab nào
            intent.putExtra("NAVIGATE_TO", "ORDERS");
            startActivity(intent);
            finish();
        });
    }
}