package com.example.androidapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView; // Import ListView
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.adapters.CartAdapter;
import com.example.androidapplication.data.model.cart.Cart;
import com.example.androidapplication.databinding.ActivityCartBinding;
import com.example.androidapplication.viewmodel.CartViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemListener {

    private ActivityCartBinding binding;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    private double currentTotalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Ẩn title mặc định để dùng title custom
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        setupListView();
        observeViewModel();

        binding.buttonCheckout.setOnClickListener(v -> {
            if (currentTotalPrice > 0) {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra("TOTAL_PRICE", currentTotalPrice);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchCart();
    }

    private void setupListView() {
        // Khởi tạo Adapter rỗng ban đầu
        cartAdapter = new CartAdapter(this, new ArrayList<>(), this);
        // Gán adapter cho ListView (Chú ý ID trong XML là list_view_cart)
        binding.listViewCart.setAdapter(cartAdapter);
    }

//    private void observeViewModel() {
//        cartViewModel.getIsLoading().observe(this, isLoading -> {
//            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
//        });
//
//        cartViewModel.getCart().observe(this, apiResponse -> {
//            if (apiResponse != null && apiResponse.getData() != null) {
//                updateCartUI(apiResponse.getData());
//            } else {
//                updateCartUI(null);
//            }
//        });
//    }
private void observeViewModel() {
    cartViewModel.getIsLoading().observe(this, isLoading -> {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    });

    // --- SỬA ĐOẠN NÀY ---
    // Lắng nghe biến cartData cố định trong ViewModel
    cartViewModel.getCartLiveData().observe(this, apiResponse -> {
        if (apiResponse != null && apiResponse.getData() != null) {
            updateCartUI(apiResponse.getData());
        } else {
            updateCartUI(null);
        }
    });
}

    private void updateCartUI(Cart cart) {
        if (cart == null || cart.getCartDetails() == null || cart.getCartDetails().isEmpty()) {
            binding.emptyCartView.setVisibility(View.VISIBLE);
            binding.checkoutLayout.setVisibility(View.GONE);
            binding.listViewCart.setVisibility(View.GONE);
            this.currentTotalPrice = 0.0;

            // Xóa dữ liệu adapter
            cartAdapter.updateData(new ArrayList<>());
        } else {
            binding.emptyCartView.setVisibility(View.GONE);
            binding.checkoutLayout.setVisibility(View.VISIBLE);
            binding.listViewCart.setVisibility(View.VISIBLE);

            // Cập nhật dữ liệu cho Adapter
            cartAdapter.updateData(cart.getCartDetails());

            // Cập nhật tổng tiền
            this.currentTotalPrice = cart.getTotalPrice();
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            binding.totalPriceValue.setText(currencyFormat.format(this.currentTotalPrice));
        }
    }

//    private void fetchCart() {
//        cartViewModel.getCart();
//    }
private void fetchCart() {
    // Gọi hàm loadCart() để ViewModel kích hoạt API và bắn dữ liệu về LiveData ở trên
    cartViewModel.loadCart();
}

    // --- IMPLEMENT CÁC HÀM TỪ INTERFACE ADAPTER ---

//    @Override
//    public void onDelete(long productId) {
//        cartViewModel.deleteProductFromCart(productId).observe(this, response -> {
//            if (response != null && response.getStatus() == 200) {
//                Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
//                fetchCart(); // Load lại giỏ hàng
//            } else {
//                Toast.makeText(this, "Lỗi xóa sản phẩm", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public void onIncrease(long productId) {
//        // Gọi API thêm (logic thêm 1 giống API thêm mới)
//        cartViewModel.addProductToCart(productId).observe(this, response -> {
//            if (response != null && response.getStatus() == 200) {
//                fetchCart();
//            }
//        });
//    }
//
//    @Override
//    public void onDecrease(long productId) {
//        // Gọi API giảm 1
//        cartViewModel.deleteOneProductFromCart(productId).observe(this, response -> {
//            if (response != null && response.getStatus() == 200) {
//                fetchCart();
//            }
//        });
//    }
@Override
public void onDelete(long productId) {
    binding.progressBar.setVisibility(View.VISIBLE);
    cartViewModel.deleteProductFromCart(productId).observe(this, response -> {
        if (response != null && response.getStatus() == 200) {
            // Xóa thành công -> Gọi fetchCart() -> ViewModel load lại -> UI tự update
            fetchCart();
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
        } else {
            binding.progressBar.setVisibility(View.GONE); // Tắt loading nếu lỗi
            Toast.makeText(this, "Lỗi xóa sản phẩm", Toast.LENGTH_SHORT).show();
        }
    });
}

    @Override
    public void onIncrease(long productId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        cartViewModel.addProductToCart(productId).observe(this, response -> {
            if (response != null && response.getStatus() == 200) {
                fetchCart();
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDecrease(long productId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        cartViewModel.deleteOneProductFromCart(productId).observe(this, response -> {
            if (response != null && response.getStatus() == 200) {
                fetchCart();
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
}