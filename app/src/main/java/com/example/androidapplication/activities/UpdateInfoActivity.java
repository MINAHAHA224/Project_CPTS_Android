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
import com.example.androidapplication.data.model.user.UserProfileUpdateDTO;
import com.example.androidapplication.utils.DialogUtils;
import com.example.androidapplication.viewmodel.UserViewModel;

public class UpdateInfoActivity extends AppCompatActivity {

    private EditText edtFullName, edtPhone, edtAddress;
    private Button btnUpdate;
    private ImageView btnBack;
    private UserViewModel userViewModel;
    // Lưu email để gửi lại (vì API update thường yêu cầu full object)
    private String currentEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        // 1. Ánh xạ
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);

        // 2. ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // 3. Load dữ liệu cũ lên
        loadCurrentInfo();

        // 4. Sự kiện
        btnBack.setOnClickListener(v -> finish());

        btnUpdate.setOnClickListener(v -> {
            UserProfileUpdateDTO dto = new UserProfileUpdateDTO();
            dto.setEmail(currentEmail); // Giữ nguyên email
            dto.setFullName(edtFullName.getText().toString());
            dto.setPhone(edtPhone.getText().toString());
            dto.setAddress(edtAddress.getText().toString());

            userViewModel.updateUserProfile(dto).observe(this, resource -> {
                if (resource.status == Status.SUCCESS) {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng activity
                } else if (resource.status == Status.ERROR) {
                    Toast.makeText(this, "Lỗi: " + resource.error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loadCurrentInfo() {
        userViewModel.getUserProfile().observe(this, resource -> {
            if (resource.status == Status.SUCCESS && resource.data != null) {
                UserProfileUpdateDTO data = resource.data.getData();
                edtFullName.setText(data.getFullName());
                edtPhone.setText(data.getPhone());
                edtAddress.setText(data.getAddress());
                currentEmail = data.getEmail();
            }
        });
    }
}