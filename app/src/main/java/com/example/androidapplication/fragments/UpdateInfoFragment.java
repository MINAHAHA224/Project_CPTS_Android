package com.example.androidapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.data.model.Status;
import com.example.androidapplication.data.model.user.UserProfileUpdateDTO;
import com.example.androidapplication.databinding.FragmentUpdateInfoBinding;
import com.example.androidapplication.utils.DialogUtils;
import com.example.androidapplication.viewmodel.UserViewModel;

public class UpdateInfoFragment extends Fragment {
    private FragmentUpdateInfoBinding binding;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdateInfoBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Quan sát dữ liệu profile để điền vào form
        userViewModel.getUserProfile().observe(getViewLifecycleOwner(), resource -> {
            if (resource.status == Status.SUCCESS && resource.data != null && resource.data.getData() != null) {
                UserProfileUpdateDTO profile = resource.data.getData();
                binding.emailEditText.setText(profile.getEmail());
                binding.fullNameEditText.setText(profile.getFullName());
                binding.phoneEditText.setText(profile.getPhone());
                binding.addressEditText.setText(profile.getAddress());
            }
        });

        binding.btnUpdate.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {
        String fullName = binding.fullNameEditText.getText().toString().trim();
        String phone = binding.phoneEditText.getText().toString().trim();
        String address = binding.addressEditText.getText().toString().trim();

        if (fullName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            DialogUtils.showErrorDialog(requireContext(), "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        UserProfileUpdateDTO dto = new UserProfileUpdateDTO();
        dto.setEmail(binding.emailEditText.getText().toString());
        dto.setFullName(fullName);
        dto.setPhone(phone);
        dto.setAddress(address);

        userViewModel.updateUserProfile(dto).observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    // Có thể hiển thị loading trên nút
                    binding.btnUpdate.setEnabled(false);
                    break;
                case SUCCESS:
                    binding.btnUpdate.setEnabled(true);
                    DialogUtils.showSuccessDialog(requireContext(), "Thành Công", "Cập nhật thông tin thành công.", null);
                    // Yêu cầu ViewModel tải lại dữ liệu profile
                    userViewModel.getUserProfile();
                    break;
                case ERROR:
                    binding.btnUpdate.setEnabled(true);
                    DialogUtils.showErrorDialog(requireContext(), resource.error.getMessage());
                    break;
            }
        });
    }
}