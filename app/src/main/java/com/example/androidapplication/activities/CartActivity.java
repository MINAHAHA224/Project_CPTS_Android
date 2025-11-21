


package com.example.androidapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.androidapplication.adapters.CartAdapter;
import com.example.androidapplication.data.model.cart.Cart;
import com.example.androidapplication.databinding.ActivityCartBinding;
import com.example.androidapplication.viewmodel.CartViewModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemInteractionListener {

    private ActivityCartBinding binding;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    private double currentTotalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        setupRecyclerView();
        observeViewModel();

        binding.buttonCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putExtra("TOTAL_PRICE", currentTotalPrice);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchCart();
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(this, new ArrayList<>(), this);
        binding.recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewCart.setAdapter(cartAdapter);
    }

    private void observeViewModel() {
        cartViewModel.getIsLoading().observe(this, isLoading -> {
            if (!isChangingConfigurations()) {
                binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        cartViewModel.getCart().observe(this, apiResponse -> {
            if (apiResponse != null && apiResponse.getData() != null) {
                updateCartUI(apiResponse.getData());
            } else {
                Toast.makeText(this, "Failed to load cart", Toast.LENGTH_SHORT).show();
                updateCartUI(null);
            }
        });
    }

    private void updateCartUI(Cart cart) {
        if (cart == null || cart.getCartDetails() == null || cart.getCartDetails().isEmpty()) {
            binding.emptyCartView.setVisibility(View.VISIBLE);
            binding.checkoutLayout.setVisibility(View.GONE);
            binding.recyclerViewCart.setVisibility(View.GONE);
            this.currentTotalPrice = 0.0;
            // Nếu adapter đã tồn tại, hãy xóa dữ liệu của nó
            if (cartAdapter != null) {
                cartAdapter.updateCartItems(new ArrayList<>());
            }
        } else {
            binding.emptyCartView.setVisibility(View.GONE);
            binding.checkoutLayout.setVisibility(View.VISIBLE);
            binding.recyclerViewCart.setVisibility(View.VISIBLE);

            // --- ĐÂY LÀ SỰ THAY ĐỔI QUAN TRỌNG NHẤT ---
            // THAY VÌ TẠO MỚI, HÃY GỌI HÀM CẬP NHẬT CỦA ADAPTER HIỆN CÓ
            cartAdapter.updateCartItems(cart.getCartDetails());
            // --- KẾT THÚC THAY ĐỔI ---

            // Cập nhật lại tổng tiền vào biến của Activity
            this.currentTotalPrice = cart.getTotalPrice();

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            binding.totalPriceValue.setText(currencyFormat.format(this.currentTotalPrice));
        }
    }

    private void fetchCart() {
        cartViewModel.getCart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // --- CÁC HÀM XỬ LÝ SỰ KIỆN TỪ ADAPTER ---

    @Override
    public void onDeleteItem(long productId) {
        // ViewModel sẽ gọi API xóa toàn bộ (deleteProductFromCart)
        cartViewModel.deleteProductFromCart(productId).observe(this, response -> {
            if (response != null && response.getStatus() == 200) {
                Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                fetchCart(); // QUAN TRỌNG: Tải lại giỏ hàng
            } else {
                Toast.makeText(this, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onIncreaseQuantity(long productId) {
        cartViewModel.addProductToCart(productId).observe(this, response -> {
            if (response != null && response.getStatus() == 200) {
                fetchCart(); // QUAN TRỌNG: Tải lại giỏ hàng
            } else {
                Toast.makeText(this, "Lỗi khi tăng số lượng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDecreaseQuantity(long productId) {
        // ViewModel sẽ gọi API giảm 1 (deleteOneProductFromCart)
        cartViewModel.deleteOneProductFromCart(productId).observe(this, response -> {
            if (response != null && response.getStatus() == 200) {
                fetchCart(); // QUAN TRỌNG: Tải lại giỏ hàng
            } else {
                Toast.makeText(this, "Lỗi khi giảm số lượng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}