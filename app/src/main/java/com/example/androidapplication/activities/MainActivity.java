package com.example.androidapplication.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.androidapplication.R;
import com.example.androidapplication.databinding.ActivityMainBinding;
import com.example.androidapplication.fragments.HomeFragment;
import com.example.androidapplication.fragments.OrdersFragment;
import com.example.androidapplication.fragments.ProductListFragment;
import com.example.androidapplication.fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // --- THÊM ĐOẠN NÀY ---
        // Kiểm tra xem có yêu cầu chuyển tab không (Từ OrderSuccessActivity gửi sang)
        String navigateTo = getIntent().getStringExtra("NAVIGATE_TO");
        if ("ORDERS".equals(navigateTo)) {
            // Load Fragment Orders
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new com.example.androidapplication.fragments.OrdersFragment())
                    .commit();
            // Update trạng thái BottomNav
            binding.bottomNavView.setSelectedItemId(R.id.navigation_orders);
        } else {
            // Mặc định load Home
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new com.example.androidapplication.fragments.HomeFragment())
                        .commit();
            }
        }
        // ---------------------

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.navigation_products) { // THÊM ELSE IF NÀY
                selectedFragment = new ProductListFragment();
            } else if (itemId == R.id.navigation_orders) {
                selectedFragment = new OrdersFragment();
            } else if (itemId == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}