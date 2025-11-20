package com.example.androidapplication.activities;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.data.model.product.ProductDetail;
import com.example.androidapplication.databinding.ActivityProductDetailBinding;
import com.example.androidapplication.viewmodel.CartViewModel;
import com.example.androidapplication.viewmodel.ProductViewModel;
import com.google.android.material.tabs.TabLayout;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private ProductViewModel productViewModel;
    private CartViewModel cartViewModel;
    private long productId;
    private int quantity = 1;
    private ProductDetail currentProduct; // Store current product details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        productId = getIntent().getLongExtra("PRODUCT_ID", -1);
        if (productId == -1) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        observeViewModel();
        setupEventListeners();

        productViewModel.getProductDetail(productId);
    }

    private void observeViewModel() {
        productViewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        productViewModel.getProductDetail(productId).observe(this, apiResponse -> {
            if (apiResponse != null && apiResponse.getData() != null) {
                currentProduct = apiResponse.getData();
                updateUI(currentProduct);
            } else {
                Toast.makeText(this, "Failed to load product details", Toast.LENGTH_SHORT).show();
            }
        });

        // We will call the add to cart API in the event listener, so no observer here.
    }

    private void setupEventListeners() {
        binding.btnIncrease.setOnClickListener(v -> {
            if (currentProduct != null && quantity < currentProduct.getQuantity()) {
                quantity++;
                binding.textQuantity.setText(String.valueOf(quantity));
            }
        });

        binding.btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                binding.textQuantity.setText(String.valueOf(quantity));
            }
        });

        binding.addToCartButton.setOnClickListener(v -> addProductToCart());

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateContentForTab(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void updateUI(ProductDetail product) {
        binding.collapsingToolbar.setTitle(product.getName());
        binding.productDetailName.setText(product.getName());

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        binding.productDetailPrice.setText(currencyFormat.format(product.getPrice()));

        Glide.with(this)
                .load(ApiClient.BASE_URL + "resources/images/product/" + product.getImage())
                .into(binding.productDetailImage);

        // Set default content for the first tab
        updateContentForTab(0);
    }

    private void updateContentForTab(int position) {
        if (currentProduct == null) return;

        if (position == 0) { // Mô tả chi tiết
            binding.textContent.setText(Html.fromHtml(currentProduct.getDetailDesc(), Html.FROM_HTML_MODE_LEGACY));
        } else { // Thông số kỹ thuật
            // Build the specs string from sample data as requested
            String specs = "<b>Thông số cơ bản:</b><br>" +
                    "<b>CPU:</b> Intel Core i5-11400H (6 nhân 12 luồng, tối đa 4.50GHz)<br>" +
                    "<b>RAM:</b> 8GB DDR4 3200MHz (2 khe, nâng cấp tối đa 32GB)<br>" +
                    "<b>Ổ cứng:</b> 512GB NVMe PCIe Gen3x4 SSD<br>" +
                    "<b>Màn hình:</b> 15.6 inch FHD (1920x1080), 144Hz, IPS-Level<br>" +
                    "<b>Card đồ họa:</b> NVIDIA GeForce RTX 3050 4GB GDDR6<br>" +
                    "<b>Kết nối:</b> Wi-Fi 6, Bluetooth 5.1, LAN<br>" +
                    "<b>Hệ điều hành:</b> Windows 11 Home";
            binding.textContent.setText(Html.fromHtml(specs, Html.FROM_HTML_MODE_LEGACY));
        }
    }

    private void addProductToCart() {
        // Here we use the new API that takes quantity
        cartViewModel.addProductToCart(productId, (long) quantity).observe(this, apiResponse -> {
            if (apiResponse != null && apiResponse.getStatus() == 200) {
                Toast.makeText(this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();
            } else {
                String errorMessage = (apiResponse != null) ? apiResponse.getMessage() : "Failed to add to cart";
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}