package com.example.androidapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.R;
import com.example.androidapplication.data.model.Status;
import com.example.androidapplication.data.model.user.ChangePasswordDTO;
import com.example.androidapplication.utils.DialogUtils; // Dùng lại tiện ích Dialog có sẵn của bạn
import com.example.androidapplication.viewmodel.UserViewModel;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtCurrentPass, edtNewPass, edtConfirmPass;
    private Button btnSave;
    private ImageView btnBack;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // 1. Ánh xạ View
        edtCurrentPass = findViewById(R.id.edtCurrentPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        // 2. Khởi tạo ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // 3. Sự kiện nút Back
        btnBack.setOnClickListener(v -> finish());

        // 4. Sự kiện Lưu mật khẩu
        btnSave.setOnClickListener(v -> handleChangePassword());
    }

    private void handleChangePassword() {
        String currentPass = edtCurrentPass.getText().toString().trim();
        String newPass = edtNewPass.getText().toString().trim();
        String confirmPass = edtConfirmPass.getText().toString().trim();

        // Validate cơ bản (giống logic cũ của bạn)
        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.length() < 6) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi API qua ViewModel
        ChangePasswordDTO dto = new ChangePasswordDTO(currentPass, newPass, confirmPass);

        // Disable nút để tránh bấm nhiều lần
        btnSave.setEnabled(false);
        btnSave.setText("Đang xử lý...");

        userViewModel.changePassword(dto).observe(this, resource -> {
            if (resource.status == Status.SUCCESS) {
                // Thành công
                btnSave.setEnabled(true);
                btnSave.setText("LƯU MẬT KHẨU");

                // Hiển thị Dialog thành công rồi đóng Activity
                DialogUtils.showSuccessDialog(this, "Thành công", "Đổi mật khẩu thành công!", () -> {
                    finish();
                });

            } else if (resource.status == Status.ERROR) {
                // Thất bại
                btnSave.setEnabled(true);
                btnSave.setText("LƯU MẬT KHẨU");
                String msg = resource.error != null ? resource.error.getMessage() : "Lỗi không xác định";
                DialogUtils.showErrorDialog(this, msg);
            }
        });
    }
}