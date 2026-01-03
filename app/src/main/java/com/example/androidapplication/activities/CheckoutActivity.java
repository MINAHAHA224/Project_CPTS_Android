package com.example.androidapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.data.model.order.InfoOrderRqDTO;
import com.example.androidapplication.databinding.ActivityCheckoutBinding;
import com.example.androidapplication.utils.DialogUtils;
import com.example.androidapplication.utils.ToastHandler;
import com.example.androidapplication.viewmodel.OrderViewModel;
import com.example.androidapplication.viewmodel.UserViewModel;

import java.text.NumberFormat;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    private OrderViewModel orderViewModel;
    private UserViewModel userViewModel;
    private double totalPriceFromCart = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // --- SỬA LỖI CRASH TẠI ĐÂY ---
        // 1. Xóa setSupportActionBar vì ta dùng Custom Header
        // 2. Xử lý nút Back thủ công
        binding.btnBack.setOnClickListener(v -> finish());

        // Lấy dữ liệu từ Intent
        totalPriceFromCart = getIntent().getDoubleExtra("TOTAL_PRICE", 0.0);

        // Format tiền tệ
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        binding.totalPriceValue.setText(currencyFormat.format(totalPriceFromCart));

        // Init ViewModel
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        observeViewModel();

        // Gọi API lấy thông tin user để điền sẵn vào form
        userViewModel.getUserProfile();

        binding.buttonPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void observeViewModel() {
        // Loading cho User ViewModel
        userViewModel.getIsLoading().observe(this, isLoading -> {
            // Chỉ hiện loading nếu chưa có dữ liệu
            if(isLoading && binding.nameEditText.getText().toString().isEmpty()) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        // Lấy thông tin User điền vào Form
        userViewModel.getUserProfile().observe(this, resource -> {
            switch (resource.status) {
                case SUCCESS:
                    if (resource.data != null && resource.data.getData() != null) {
                        binding.nameEditText.setText(resource.data.getData().getFullName());
                        binding.phoneEditText.setText(resource.data.getData().getPhone());
                        binding.addressEditText.setText(resource.data.getData().getAddress());
                    }
                    break;
                case ERROR:
                    // Không cần báo lỗi, người dùng có thể tự nhập
                    break;
            }
        });

        // Loading cho Order ViewModel (Khi bấm đặt hàng)
        orderViewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.buttonPlaceOrder.setEnabled(!isLoading);
        });
    }

    private void placeOrder() {
        String name = binding.nameEditText.getText().toString().trim();
        String phone = binding.phoneEditText.getText().toString().trim();
        String address = binding.addressEditText.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            ToastHandler.showToast(this, "Vui lòng điền đầy đủ thông tin giao hàng", Toast.LENGTH_SHORT);
            return;
        }

        String paymentMethod = binding.radioCod.isChecked() ? "COD" : "MOMO";

        InfoOrderRqDTO orderInfo = new InfoOrderRqDTO();
        orderInfo.setReceiverName(name);
        orderInfo.setReceiverPhone(phone);
        orderInfo.setReceiverAddress(address);
        orderInfo.setPaymentMethod(paymentMethod);
        orderInfo.setTotalPriceToSaveOrder(this.totalPriceFromCart);

        orderViewModel.placeOrder(orderInfo).observe(this, apiResponse -> {
            if (apiResponse != null && apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                String method = apiResponse.getData().get("typePayment");

                if ("MOMO".equalsIgnoreCase(method)) {
                    String paymentUrl = apiResponse.getData().get("paymentUrl");
                    if (paymentUrl != null && !paymentUrl.isEmpty()) {
                        Intent momoIntent = new Intent(CheckoutActivity.this, MomoPaymentActivity.class);
                        momoIntent.putExtra("PAYMENT_URL", paymentUrl);
                        startActivity(momoIntent);
                        finish();
                    } else {
                        DialogUtils.showErrorDialog(this, "Lỗi: Không lấy được link thanh toán Momo.");
                    }
                } else {
                    // COD Thành công -> Chuyển sang màn hình OrderSuccessActivity
                    Intent intent = new Intent(CheckoutActivity.this, OrderSuccessActivity.class);
                    // Cờ này để khi bấm back ở màn hình Success sẽ không quay lại Checkout
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            } else {
                String errorMessage = (apiResponse != null) ? apiResponse.getMessage() : "Đặt hàng thất bại";
                DialogUtils.showErrorDialog(this, errorMessage);
            }
        });
    }
}