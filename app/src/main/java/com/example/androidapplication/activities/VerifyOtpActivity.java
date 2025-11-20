package com.example.androidapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidapplication.databinding.ActivityVerifyOtpBinding;
import com.example.androidapplication.utils.DialogUtils;
import com.example.androidapplication.viewmodel.AuthViewModel;

public class VerifyOtpActivity extends AppCompatActivity {

    private ActivityVerifyOtpBinding binding;
    private AuthViewModel authViewModel;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        if (userEmail == null || userEmail.isEmpty()) {
            DialogUtils.showErrorDialog(this, "Lỗi: Không tìm thấy email.");
            return;
        }

        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.btnVerify.setOnClickListener(v -> verifyOtp());
    }

    private void verifyOtp() {
        String otp = binding.otpEditText.getText().toString().trim();
        if (otp.length() < 6) {
            DialogUtils.showErrorDialog(this, "Mã OTP phải có 6 ký tự.");
            return;
        }

        authViewModel.verifyOtp(userEmail, otp).observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    setLoading(true);
                    break;
                case SUCCESS:
                    setLoading(false);
                    DialogUtils.showSuccessDialog(this, "Thành Công", "Xác minh OTP thành công!", () -> {
                        Intent intent = new Intent(VerifyOtpActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        startActivity(intent);
                    });
                    break;
                case ERROR:
                    setLoading(false);
                    DialogUtils.showErrorDialog(this, resource.error.getMessage());
                    break;
            }
        });
    }

    private void setLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnVerify.setEnabled(!isLoading);
    }
}