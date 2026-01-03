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
import com.example.androidapplication.utils.ToastHandler;
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
    private ProductDetail currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Xử lý nút Back (Thay vì dùng Toolbar mặc định, ta tự xử lý sự kiện click cho ảnh mũi tên)
        binding.btnBack.setOnClickListener(v -> finish());

        productId = getIntent().getLongExtra("PRODUCT_ID", -1);
        if (productId == -1) {
            ToastHandler.showToast(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT);
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
                ToastHandler.showToast(this, "Lỗi tải thông tin sản phẩm", Toast.LENGTH_SHORT);
            }
        });
    }

    private void setupEventListeners() {
        // Nút tăng số lượng
        binding.btnIncrease.setOnClickListener(v -> {
            // Kiểm tra tồn kho nếu cần (currentProduct.getQuantity())
            quantity++;
            binding.textQuantity.setText(String.valueOf(quantity));
        });

        // Nút giảm số lượng
        binding.btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                binding.textQuantity.setText(String.valueOf(quantity));
            }
        });

        // Nút thêm vào giỏ
        binding.addToCartButton.setOnClickListener(v -> addProductToCart());

        // Sự kiện chuyển Tab (Mô tả / Thông số)
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
        // Không cần set title cho CollapsingToolbar nữa vì ta đã dùng TextView thường
        // binding.collapsingToolbar.setTitle(product.getName()); -> XÓA

        binding.productDetailName.setText(product.getName());

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        binding.productDetailPrice.setText(currencyFormat.format(product.getPrice()));

        Glide.with(this)
                .load(ApiClient.BASE_URL + "resources/images/product/" + product.getImage())
                .centerInside() // Đổi thành centerInside để thấy toàn bộ ảnh laptop
                .into(binding.productDetailImage);

        // Mặc định hiển thị tab đầu tiên
        updateContentForTab(0);
    }

    private void updateContentForTab(int position) {
        if (currentProduct == null) return;

        if (position == 0) { // Tab Mô tả chi tiết
            if (currentProduct.getDetailDesc() != null) {
                binding.textContent.setText(Html.fromHtml(currentProduct.getDetailDesc(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                binding.textContent.setText("Đang cập nhật mô tả...");
            }
        } else { // Tab Thông số kỹ thuật
            // Giả lập dữ liệu thông số (hoặc lấy từ field shortDesc nếu bạn muốn)
            String specs = "<b>Thông số kỹ thuật:</b><br><br>" +
                    "• <b>CPU:</b> Intel Core i5/i7<br>" +
                    "• <b>RAM:</b> 8GB/16GB DDR4<br>" +
                    "• <b>SSD:</b> 512GB NVMe PCIe<br>" +
                    "• <b>Màn hình:</b> 15.6 inch FHD IPS<br>" +
                    "• <b>Pin:</b> 3 Cell 50Whr<br>" +
                    "• <b>Trọng lượng:</b> 2.2 kg";

            // Nếu sản phẩm có shortDesc thì hiển thị, không thì hiện mẫu trên
            if(currentProduct.getShortDesc() != null && !currentProduct.getShortDesc().isEmpty()){
                binding.textContent.setText(Html.fromHtml(currentProduct.getShortDesc(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                binding.textContent.setText(Html.fromHtml(specs, Html.FROM_HTML_MODE_LEGACY));
            }
        }
    }

    private void addProductToCart() {
        // Gọi API thêm vào giỏ với số lượng
        cartViewModel.addProductToCart(productId, (long) quantity).observe(this, apiResponse -> {
            if (apiResponse != null && apiResponse.getStatus() == 200) {
                ToastHandler.showToast(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT);
            } else {
                String errorMessage = (apiResponse != null) ? apiResponse.getMessage() : "Lỗi thêm giỏ hàng";
                ToastHandler.showToast(this, errorMessage, Toast.LENGTH_SHORT);
            }
        });
    }
}