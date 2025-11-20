package com.example.androidapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.data.model.user.ChangePasswordDTO;
import com.example.androidapplication.databinding.FragmentChangePasswordBinding;
import com.example.androidapplication.utils.DialogUtils;
import com.example.androidapplication.viewmodel.UserViewModel;

public class ChangePasswordFragment extends Fragment {
    private FragmentChangePasswordBinding binding;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnUpdatePassword.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String currentPass = binding.currentPasswordEditText.getText().toString().trim();
        String newPass = binding.newPasswordEditText.getText().toString().trim();
        String confirmPass = binding.confirmNewPasswordEditText.getText().toString().trim();

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            DialogUtils.showErrorDialog(requireContext(), "Vui lòng nhập đầy đủ các trường.");
            return;
        }
        if (newPass.length() < 6) {
            DialogUtils.showErrorDialog(requireContext(), "Mật khẩu mới phải có ít nhất 6 ký tự.");
            return;
        }
        if (!newPass.equals(confirmPass)) {
            DialogUtils.showErrorDialog(requireContext(), "Mật khẩu xác nhận không khớp.");
            return;
        }

        ChangePasswordDTO dto = new ChangePasswordDTO(currentPass, newPass, confirmPass);

        userViewModel.changePassword(dto).observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    binding.btnUpdatePassword.setEnabled(false);
                    break;
                case SUCCESS:
                    binding.btnUpdatePassword.setEnabled(true);
                    DialogUtils.showSuccessDialog(requireContext(), "Thành Công", "Đổi mật khẩu thành công.", null);
                    // Xóa trống các ô input
                    binding.currentPasswordEditText.setText("");
                    binding.newPasswordEditText.setText("");
                    binding.confirmNewPasswordEditText.setText("");
                    break;
                case ERROR:
                    binding.btnUpdatePassword.setEnabled(true);
                    DialogUtils.showErrorDialog(requireContext(), resource.error.getMessage());
                    break;
            }
        });
    }
}