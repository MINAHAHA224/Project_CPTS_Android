package com.example.androidapplication.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.data.model.Status;
import com.example.androidapplication.databinding.FragmentUpdateAvatarBinding;
import com.example.androidapplication.utils.DialogUtils;
import com.example.androidapplication.viewmodel.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpdateAvatarFragment extends Fragment {

    private FragmentUpdateAvatarBinding binding;
    private UserViewModel userViewModel;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    binding.avatarPreview.setImageURI(uri);
                    binding.btnUpdateAvatar.setEnabled(true);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdateAvatarBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSelectImage.setOnClickListener(v -> mGetContent.launch("image/*"));
        binding.btnUpdateAvatar.setOnClickListener(v -> updateAvatar());
    }

    private void updateAvatar() {
        if (selectedImageUri == null) {
            DialogUtils.showErrorDialog(requireContext(), "Vui lòng chọn một ảnh.");
            return;
        }

        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri)) {
            if (inputStream == null) {
                throw new IOException("Unable to open input stream for URI");
            }
            byte[] fileBytes = getBytes(inputStream);
            String mimeType = requireContext().getContentResolver().getType(selectedImageUri);
            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), fileBytes);
            MultipartBody.Part body = MultipartBody.Part.createFormData("avatarFile", "avatar.jpg", requestFile);

            userViewModel.updateAvatar(body).observe(getViewLifecycleOwner(), resource -> {
                switch (resource.status) {
                    case LOADING:
                        binding.btnUpdateAvatar.setEnabled(false);
                        binding.btnUpdateAvatar.setText("Đang tải lên...");
                        break;
                    case SUCCESS:
                        binding.btnUpdateAvatar.setEnabled(true);
                        binding.btnUpdateAvatar.setText("Cập Nhật Ảnh");
                        DialogUtils.showSuccessDialog(requireContext(), "Thành Công", "Cập nhật avatar thành công.", null);
                        // Yêu cầu ViewModel tải lại dữ liệu profile để cập nhật ảnh ở menu trái
                        userViewModel.getUserProfile();
                        break;
                    case ERROR:
                        binding.btnUpdateAvatar.setEnabled(true);
                        binding.btnUpdateAvatar.setText("Cập Nhật Ảnh");
                        DialogUtils.showErrorDialog(requireContext(), resource.error.getMessage());
                        break;
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            DialogUtils.showErrorDialog(requireContext(), "Lỗi khi đọc file ảnh.");
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}