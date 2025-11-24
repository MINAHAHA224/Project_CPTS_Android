package com.example.androidapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView; // <--- QUAN TRỌNG: Import TextView
import android.view.View; // ProgressBar là View

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.androidapplication.R;
import com.example.androidapplication.activities.AboutUsActivity;
import com.example.androidapplication.activities.AuthActivity;
import com.example.androidapplication.activities.ChangePasswordActivity;
import com.example.androidapplication.activities.ContactUsActivity;
import com.example.androidapplication.activities.MainActivity;
import com.example.androidapplication.activities.UpdateInfoActivity;
import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.data.model.Status;
import com.example.androidapplication.utils.SharedPrefManager;
import com.example.androidapplication.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// ... các import khác ...

public class ProfileFragment extends Fragment {
    private TextView btnRegisterFace;
    private UserViewModel userViewModel;

    private ImageView imgAvatar;
    private TextView txtName, txtEmail;

    // --- SỬA LỖI TẠI ĐÂY ---
    // Đổi tất cả từ Button thành TextView
    private TextView btnEditProfile, btnChangePass, btnOrderHistory, btnLogout;
    private TextView btnContactUs, btnAboutUs;
    // ------------------------

    private View progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        imgAvatar = view.findViewById(R.id.profile_avatar);
        txtName = view.findViewById(R.id.profile_name);
        txtEmail = view.findViewById(R.id.profile_email);
        progressBar = view.findViewById(R.id.progress_bar);
        btnRegisterFace = view.findViewById(R.id.btn_register_face);

        // --- ÁNH XẠ LẠI (Không cần ép kiểu (Button) nữa) ---
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnChangePass = view.findViewById(R.id.btn_change_password);
        btnOrderHistory = view.findViewById(R.id.btn_order_history);

        btnContactUs = view.findViewById(R.id.btn_contact_us);
        btnAboutUs = view.findViewById(R.id.btn_about_us);

        btnLogout = view.findViewById(R.id.btn_logout);
        // ----------------------------------------------------

        observeViewModel();
        userViewModel.getUserProfile();
        setupEvents();
    }

    private void setupEvents() {
        // -- Cập nhật thông tin (Mở Activity mới) --
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UpdateInfoActivity.class);
            startActivity(intent);
        });

        // -- Đổi mật khẩu (Mở Activity mới) --
        btnChangePass.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        btnRegisterFace.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), com.example.androidapplication.activities.FaceRegisterActivity.class));
        });

        // -- Lịch sử đơn hàng (Chuyển Tab trên MainActivity) --
        btnOrderHistory.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                // Giả sử ID của menu item Orders là R.id.navigation_orders
                BottomNavigationView bottomNav = mainActivity.findViewById(R.id.bottom_nav_view);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.navigation_orders);
                }
            }
        });



        // -- Đăng xuất --
        btnLogout.setOnClickListener(v -> {
            SharedPrefManager.getInstance(getContext()).clear();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        });


        btnContactUs.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ContactUsActivity.class));
        });

        btnAboutUs.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AboutUsActivity.class));
        });
    }

    private void observeViewModel() {
        userViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        userViewModel.getUserProfile().observe(getViewLifecycleOwner(), resource -> {
            if (resource.status == Status.SUCCESS && resource.data != null && resource.data.getData() != null) {
                txtName.setText(resource.data.getData().getFullName());
                txtEmail.setText(resource.data.getData().getEmail());

                Glide.with(this)
                        .load(ApiClient.BASE_URL + "resources/images/profile/" + resource.data.getData().getAvatar())
                        .placeholder(R.drawable.ic_launcher_foreground) // Ảnh mặc định
                        .into(imgAvatar);
            }
        });
    }
}