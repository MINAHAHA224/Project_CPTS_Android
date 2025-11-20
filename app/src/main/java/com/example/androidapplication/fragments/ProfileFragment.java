package com.example.androidapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.androidapplication.R;
import com.example.androidapplication.activities.AuthActivity;
import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.data.model.Status;
import com.example.androidapplication.databinding.FragmentProfileBinding;
import com.example.androidapplication.utils.SharedPrefManager;
import com.example.androidapplication.viewmodel.UserViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private UserViewModel userViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        observeViewModel();
        setupEventListeners();

        // Load user profile data
        userViewModel.getUserProfile();

        // Load the default fragment into the container
        if (savedInstanceState == null) {
            loadFragment(new UpdateInfoFragment());
        }
    }

    private void observeViewModel() {
        userViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // --- ĐÂY LÀ ĐOẠN CODE ĐÃ ĐƯỢC SỬA LỖI ---
        userViewModel.getUserProfile().observe(getViewLifecycleOwner(), resource -> {
            if (resource.status == Status.SUCCESS && resource.data != null && resource.data.getData() != null) {
                // Nếu thành công, lấy data từ resource.data.getData()
                binding.profileName.setText(resource.data.getData().getFullName());
                binding.profileEmail.setText(resource.data.getData().getEmail());
                Glide.with(this)
                        .load(ApiClient.BASE_URL + "resources/images/avatar/" + resource.data.getData().getAvatar())
                        .placeholder(R.drawable.ic_person)
                        .circleCrop()
                        .into(binding.profileAvatar);

                boolean canChangePassword = resource.data.getData().isHasChangePass();
                binding.btnChangePassword.setEnabled(canChangePassword);
                binding.btnChangePassword.setAlpha(canChangePassword ? 1.0f : 0.5f);

            } else if (resource.status == Status.ERROR) {
                // Nếu lỗi, hiển thị thông báo
                Toast.makeText(getContext(), "Failed to load profile: " + resource.error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // --- KẾT THÚC ĐOẠN SỬA LỖI ---
    }

    private void setupEventListeners() {
        binding.btnEditProfile.setOnClickListener(v -> loadFragment(new UpdateInfoFragment()));

        // Gắn sự kiện cho nút Thay đổi Avatar
        binding.btnChangeAvatar.setOnClickListener(v -> loadFragment(new UpdateAvatarFragment()));

        binding.btnChangePassword.setOnClickListener(v -> {
            if (binding.btnChangePassword.isEnabled()) {
                loadFragment(new ChangePasswordFragment());
            } else {
                Toast.makeText(getContext(), "Không thể đổi mật khẩu cho tài khoản Google", Toast.LENGTH_SHORT).show();
            }
        });

//        binding.btnOrderHistory.setOnClickListener(v -> loadFragment(new ProfileOrderHistoryFragment()));

        binding.btnContactUs.setOnClickListener(v -> loadFragment(new ContactUsFragment()));

        binding.btnAboutUs.setOnClickListener(v -> loadFragment(new AboutUsFragment()));

        binding.btnLogout.setOnClickListener(v -> logout());
    }

    private void loadFragment(Fragment fragment) {
        // Sử dụng getChildFragmentManager() vì container nằm trong Fragment cha (ProfileFragment)
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.profile_fragment_container, fragment);
        transaction.commit();
    }

    private void logout() {
        if (getContext() != null) {
            SharedPrefManager.getInstance(getContext()).clear();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}