package com.example.androidapplication.activities;

import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.data.model.auth.RegisterDTO;
import com.example.androidapplication.databinding.ActivityRegisterBinding;
import com.example.androidapplication.utils.DialogUtils;
import com.example.androidapplication.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        binding.signInText.setText(Html.fromHtml("Đã có tài khoản? <b>Đăng nhập</b>", Html.FROM_HTML_MODE_LEGACY));
        binding.signInText.setOnClickListener(v -> {
            // Kết thúc activity hiện tại để quay lại màn hình Login
            finish();
        });

        binding.registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        // Lấy dữ liệu từ các EditText
        String firstName = binding.firstNameEditText.getText().toString().trim();
        String lastName = binding.lastNameEditText.getText().toString().trim();
        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();
        String confirmPassword = binding.confirmPasswordEditText.getText().toString().trim();

        // --- Kiểm tra lỗi frontend ---
        if (!validateInput(firstName, lastName, email, password, confirmPassword)) {
            return; // Dừng lại nếu input không hợp lệ
        }

        // --- Gọi ViewModel để đăng ký ---
        RegisterDTO registerDTO = new RegisterDTO(firstName, lastName, email, password, confirmPassword);

        authViewModel.register(registerDTO).observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    setLoading(true);
                    break;
                case SUCCESS:
                    setLoading(false);
                    // Đăng ký thành công!
                    // resource.data ở đây là ApiResponse<InformationDTO>
                    // Hiển thị dialog thành công và quay về màn hình Login khi người dùng nhấn OK.
                    DialogUtils.showSuccessDialog(this,
                            "Registration Successful!",
                            "Your account has been created. Please log in to continue.",
                            () -> finish() // Sử dụng lambda để truyền hành động "finish()"
                    );
                    break;
                case ERROR:
                    setLoading(false);
                    // Đăng ký thất bại, hiển thị lỗi từ backend
                    // resource.error ở đây là ErrorResponse
                    String errorMessage = (resource.error != null && resource.error.getMessage() != null)
                            ? resource.error.getMessage()
                            : "An unknown registration error occurred.";
                    DialogUtils.showErrorDialog(this, errorMessage);
                    break;
            }
        });
    }

    private boolean validateInput(String firstName, String lastName, String email, String password, String confirmPassword) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            DialogUtils.showErrorDialog(this, "All fields are required.");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            DialogUtils.showErrorDialog(this, "Please enter a valid email address.");
            return false;
        }
        if (password.length() < 6) {
            DialogUtils.showErrorDialog(this, "Password must be at least 6 characters long.");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            DialogUtils.showErrorDialog(this, "Passwords do not match.");
            return false;
        }
        return true;
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.registerButton.setVisibility(View.INVISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.registerButton.setVisibility(View.VISIBLE);
        }
    }
}