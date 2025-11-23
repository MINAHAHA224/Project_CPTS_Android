package com.example.androidapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidapplication.data.model.auth.ResetPasswordDTO;
import com.example.androidapplication.databinding.ActivityResetPasswordBinding;
import com.example.androidapplication.utils.DialogUtils;
import com.example.androidapplication.viewmodel.AuthViewModel;

public class ResetPasswordActivity extends AppCompatActivity {

    private ActivityResetPasswordBinding binding;
    private AuthViewModel authViewModel;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // --- SỬA LỖI Ở ĐÂY ---
        // XÓA dòng binding.toolbar... đi

        // THAY BẰNG DÒNG NÀY:
//        binding.btnBack.setOnClickListener(v -> finish());
        // --------------------

        binding.btnResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String newPass = binding.newPasswordEditText.getText().toString().trim();
        String confirmPass = binding.confirmNewPasswordEditText.getText().toString().trim();

        if (newPass.length() < 6) {
            DialogUtils.showErrorDialog(this, "Mật khẩu phải có ít nhất 6 ký tự.");
            return;
        }
        if (!newPass.equals(confirmPass)) {
            DialogUtils.showErrorDialog(this, "Mật khẩu xác nhận không khớp.");
            return;
        }

        ResetPasswordDTO dto = ResetPasswordDTO.builder()
                .email(userEmail)
                .password(newPass)
                .confirmPassword(confirmPass)
                .build();

        authViewModel.resetPassword(dto).observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    setLoading(true);
                    break;
                case SUCCESS:
                    setLoading(false);
                    DialogUtils.showSuccessDialog(this, "Thành Công", "Mật khẩu đã được đặt lại. Vui lòng đăng nhập.", () -> {
                        Intent intent = new Intent(ResetPasswordActivity.this, AuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    });
                    break;
                case ERROR:
                    setLoading(false);
                    DialogUtils.showErrorDialog(this, resource.error != null ? resource.error.getMessage() : "Lỗi đặt lại mật khẩu");
                    break;
            }
        });
    }

    private void setLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnResetPassword.setEnabled(!isLoading);
    }
}