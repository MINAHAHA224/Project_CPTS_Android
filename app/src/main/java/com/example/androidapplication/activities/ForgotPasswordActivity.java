package com.example.androidapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.databinding.ActivityForgotPasswordBinding;
import com.example.androidapplication.utils.DialogUtils;
import com.example.androidapplication.viewmodel.AuthViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSendOtp.setOnClickListener(v -> sendOtp());
    }

    private void sendOtp() {
        String email = binding.emailEditText.getText().toString().trim();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            DialogUtils.showErrorDialog(this, "Vui lòng nhập email hợp lệ.");
            return;
        }

        authViewModel.forgotPassword(email).observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    setLoading(true);
                    break;
                case SUCCESS:
                    setLoading(false);
                    DialogUtils.showSuccessDialog(this, "Thành Công", resource.data.getMessage(), () -> {
                        // Chuyển sang màn hình nhập OTP, gửi kèm email
                        Intent intent = new Intent(ForgotPasswordActivity.this, VerifyOtpActivity.class);
                        intent.putExtra("USER_EMAIL", email);
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
        binding.btnSendOtp.setEnabled(!isLoading);
    }
}