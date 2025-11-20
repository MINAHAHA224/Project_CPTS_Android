//package com.example.androidapplication.activities;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//import com.example.androidapplication.data.model.order.InfoOrderRqDTO;
//import com.example.androidapplication.databinding.ActivityCheckoutBinding;
//import com.example.androidapplication.viewmodel.OrderViewModel;
//import com.example.androidapplication.viewmodel.UserViewModel;
//
//import java.text.NumberFormat;
//import java.util.Locale;
//
//public class CheckoutActivity extends AppCompatActivity {
//
//    private ActivityCheckoutBinding binding;
//    private OrderViewModel orderViewModel;
//    private UserViewModel userViewModel;
//
//    private double totalPriceFromCart = 0.0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        totalPriceFromCart = getIntent().getDoubleExtra("TOTAL_PRICE" ,   0.0);
//// 2. Định dạng và hiển thị nó
//        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//        binding.totalPriceValue.setText("Total: " + currencyFormat.format(totalPriceFromCart));
//
//
//        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
//        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
//
//        observeViewModel();
//        userViewModel.getUserProfile(); // Fetch user info to pre-fill the form
//
//        binding.buttonPlaceOrder.setOnClickListener(v -> placeOrder());
//    }
//
//    private void observeViewModel() {
//        userViewModel.getIsLoading().observe(this, isLoading -> {
//            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
//        });
//
//        userViewModel.getUserProfile().observe(this, apiResponse -> {
//            if(apiResponse != null && apiResponse.getData() != null) {
//                binding.nameEditText.setText(apiResponse.getData().getFullName());
//                binding.phoneEditText.setText(apiResponse.getData().getPhone());
//                binding.addressEditText.setText(apiResponse.getData().getAddress());
//            }
//        });
//
//        orderViewModel.getIsLoading().observe(this, isLoading -> {
//            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
//            binding.buttonPlaceOrder.setEnabled(!isLoading);
//        });
//
////        orderViewModel.placeOrder(null).observe(this, apiResponse -> {
////            if(apiResponse != null && apiResponse.getStatus() == 200) {
////                Toast.makeText(this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
////                String paymentMethod = apiResponse.getData().get("typePayment");
////                if ("MOMO".equalsIgnoreCase(paymentMethod)) {
////                    String paymentUrl = apiResponse.getData().get("paymentUrl");
////                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
////                    startActivity(browserIntent);
////                }
////
////                // Navigate to MainActivity and clear back stack
////                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                startActivity(intent);
////                finish();
////
////            } else {
////                Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
////            }
////        });
//    }
//
//    private void placeOrder() {
//        String name = binding.nameEditText.getText().toString().trim();
//        String phone = binding.phoneEditText.getText().toString().trim();
//        String address = binding.addressEditText.getText().toString().trim();
//        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
//            Toast.makeText(this, "Please fill all shipping information", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String paymentMethod = binding.radioCod.isChecked() ? "COD" : "MOMO";
//
//        InfoOrderRqDTO orderInfo = new InfoOrderRqDTO();
//        orderInfo.setReceiverName(name);
//        orderInfo.setReceiverPhone(phone);
//        orderInfo.setReceiverAddress(address);
//        orderInfo.setPaymentMethod(paymentMethod);
//        orderInfo.setTotalPriceToSaveOrder(this.totalPriceFromCart);
//        // totalPrice is calculated on the backend, so we don't need to send it.
//
//        orderViewModel.placeOrder(orderInfo).observe(this, apiResponse -> {
//            if(apiResponse != null && apiResponse.getStatus() == 200) {
//                Toast.makeText(this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
//                if ("MOMO".equalsIgnoreCase(paymentMethod)) {
//                    String paymentUrl = apiResponse.getData().get("paymentUrl");
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
//                    startActivity(browserIntent);
//                }
//
//                // Navigate to MainActivity and clear back stack
//                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//
//            } else {
//                Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//}




//package com.example.androidapplication.activities;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.androidapplication.data.model.order.InfoOrderRqDTO;
//import com.example.androidapplication.databinding.ActivityCheckoutBinding;
//import com.example.androidapplication.utils.DialogUtils;
//import com.example.androidapplication.viewmodel.OrderViewModel;
//import com.example.androidapplication.viewmodel.UserViewModel;
//
//import java.text.NumberFormat;
//import java.util.Locale;
//
//public class CheckoutActivity extends AppCompatActivity {
//
//    private ActivityCheckoutBinding binding;
//    private OrderViewModel orderViewModel;
//    private UserViewModel userViewModel;
//    private double totalPriceFromCart = 0.0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        // Nhận và hiển thị tổng tiền
//        totalPriceFromCart = getIntent().getDoubleExtra("TOTAL_PRICE", 0.0);
//        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//        binding.totalPriceValue.setText("Total: " + currencyFormat.format(totalPriceFromCart));
//
//        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
//        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
//
//        observeViewModel();
//        userViewModel.getUserProfile(); // Tải thông tin người dùng
//
//        binding.buttonPlaceOrder.setOnClickListener(v -> placeOrder());
//    }
//
//    private void observeViewModel() {
//        userViewModel.getIsLoading().observe(this, isLoading -> {
//            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
//        });
//
//        userViewModel.getUserProfile().observe(this, apiResponse -> {
//            if(apiResponse != null && apiResponse.getData() != null) {
//                binding.nameEditText.setText(apiResponse.getData().getFullName());
//                binding.phoneEditText.setText(apiResponse.getData().getPhone());
//                binding.addressEditText.setText(apiResponse.getData().getAddress());
//            }
//        });
//
//        orderViewModel.getIsLoading().observe(this, isLoading -> {
//            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
//            binding.buttonPlaceOrder.setEnabled(!isLoading);
//        });
//    }
//
//    private void placeOrder() {
//        String name = binding.nameEditText.getText().toString().trim();
//        String phone = binding.phoneEditText.getText().toString().trim();
//        String address = binding.addressEditText.getText().toString().trim();
//
//        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
//            Toast.makeText(this, "Please fill all shipping information", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String paymentMethod = binding.radioCod.isChecked() ? "COD" : "MOMO";
//
//        InfoOrderRqDTO orderInfo = new InfoOrderRqDTO();
//        orderInfo.setReceiverName(name);
//        orderInfo.setReceiverPhone(phone);
//        orderInfo.setReceiverAddress(address);
//        orderInfo.setPaymentMethod(paymentMethod);
//        orderInfo.setTotalPriceToSaveOrder(this.totalPriceFromCart);
//
//        // --- BẮT ĐẦU PHẦN SỬA LOGIC QUAN TRỌNG ---
//
//        // Gọi API và lắng nghe kết quả ngay tại đây
//        orderViewModel.placeOrder(orderInfo).observe(this, apiResponse -> {
//            if (apiResponse != null && apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
//                String method = apiResponse.getData().get("typePayment");
//
//                if ("MOMO".equalsIgnoreCase(method)) {
//                    // XỬ LÝ CHO MOMO
//                    String paymentUrl = apiResponse.getData().get("paymentUrl");
//                    if (paymentUrl != null && !paymentUrl.isEmpty()) {
//                        // Mở link thanh toán trong trình duyệt
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
//                        startActivity(browserIntent);
//                        // QUAN TRỌNG: Kết thúc activity này để người dùng không quay lại được
//                        finish();
//                    } else {
//                        DialogUtils.showErrorDialog(this, "Không nhận được link thanh toán Momo.");
//                    }
//                } else {
//                    // XỬ LÝ CHO COD VÀ CÁC PHƯƠNG THỨC KHÁC
//                    Toast.makeText(this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
//                    // Chuyển về trang chủ
//                    Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//                }
//
//            } else {
//                // Xử lý khi API thất bại
//                String errorMessage = (apiResponse != null) ? apiResponse.getMessage() : "Failed to place order";
//                DialogUtils.showErrorDialog(this, errorMessage);
//            }
//        });
//
//        // --- KẾT THÚC PHẦN SỬA LOGIC ---
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//}



package com.example.androidapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.data.model.Status; // THÊM IMPORT NÀY
import com.example.androidapplication.data.model.order.InfoOrderRqDTO;
import com.example.androidapplication.databinding.ActivityCheckoutBinding;
import com.example.androidapplication.utils.DialogUtils;
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

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        totalPriceFromCart = getIntent().getDoubleExtra("TOTAL_PRICE", 0.0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        binding.totalPriceValue.setText("Total: " + currencyFormat.format(totalPriceFromCart));

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        observeViewModel();
        userViewModel.getUserProfile();

        binding.buttonPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void observeViewModel() {
        // --- BẮT ĐẦU PHẦN SỬA LỖI ---
        userViewModel.getIsLoading().observe(this, isLoading -> {
            // Chỉ hiển thị loading chung cho cả màn hình
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // SỬA LẠI OBSERVER NÀY ĐỂ XỬ LÝ ĐỐI TƯỢNG RESOURCE
        userViewModel.getUserProfile().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    // KIỂM TRA DỮ LIỆU BÊN TRONG RESOURCE
                    if (resource.data != null && resource.data.getData() != null) {
                        // TRUY CẬP ĐÚNG: resource.data.getData()
                        binding.nameEditText.setText(resource.data.getData().getFullName());
                        binding.phoneEditText.setText(resource.data.getData().getPhone());
                        binding.addressEditText.setText(resource.data.getData().getAddress());
                    }
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi khi tải thông tin người dùng: " + resource.error.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        // --- KẾT THÚC PHẦN SỬA LỖI ---

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
            Toast.makeText(this, "Please fill all shipping information", Toast.LENGTH_SHORT).show();
            return;
        }

        String paymentMethod = binding.radioCod.isChecked() ? "COD" : "MOMO";

        InfoOrderRqDTO orderInfo = new InfoOrderRqDTO();
        orderInfo.setReceiverName(name);
        orderInfo.setReceiverPhone(phone);
        orderInfo.setReceiverAddress(address);
        orderInfo.setPaymentMethod(paymentMethod);
        orderInfo.setTotalPriceToSaveOrder(this.totalPriceFromCart);

        // Luồng này vẫn giữ nguyên vì chúng ta chưa sửa OrderViewModel
        orderViewModel.placeOrder(orderInfo).observe(this, apiResponse -> {
            if (apiResponse != null && apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                String method = apiResponse.getData().get("typePayment");

                if ("MOMO".equalsIgnoreCase(method)) {
                    String paymentUrl = apiResponse.getData().get("paymentUrl");
                    if (paymentUrl != null && !paymentUrl.isEmpty()) {
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
//                        startActivity(browserIntent);
                        Intent momoIntent = new Intent(CheckoutActivity.this, MomoPaymentActivity.class);
                        momoIntent.putExtra("PAYMENT_URL", paymentUrl);
                        startActivity(momoIntent);
                        finish();
                    } else {
                        DialogUtils.showErrorDialog(this, "Không nhận được link thanh toán Momo.");
                    }
                } else {
                    Toast.makeText(this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            } else {
                String errorMessage = (apiResponse != null) ? apiResponse.getMessage() : "Failed to place order";
                DialogUtils.showErrorDialog(this, errorMessage);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}