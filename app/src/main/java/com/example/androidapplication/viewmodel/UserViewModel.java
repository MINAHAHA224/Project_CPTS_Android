package com.example.androidapplication.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.Resource;
import com.example.androidapplication.data.model.user.ChangePasswordDTO;
import com.example.androidapplication.data.model.user.UserProfileUpdateDTO;
import com.example.androidapplication.data.repository.UserRepository;

import okhttp3.MultipartBody;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<Boolean> getIsLoading() { return userRepository.getIsLoading(); }

    public LiveData<Resource<ApiResponse<UserProfileUpdateDTO>>> getUserProfile() {
        return userRepository.getUserProfile();
    }

    // --- CÁC HÀM MỚI ---

    public LiveData<Resource<ApiResponse<Object>>> updateUserProfile(UserProfileUpdateDTO profile) {
        return userRepository.updateUserProfile(profile);
    }

    public LiveData<Resource<ApiResponse<Object>>> changePassword(ChangePasswordDTO changePasswordDTO) {
        return userRepository.changePassword(changePasswordDTO);
    }

    public LiveData<Resource<ApiResponse<Object>>> updateAvatar(MultipartBody.Part avatarFile) {
        return userRepository.updateAvatar(avatarFile);
    }
}