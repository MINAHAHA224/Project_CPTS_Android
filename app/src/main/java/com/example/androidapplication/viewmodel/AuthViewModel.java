package com.example.androidapplication.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.Resource;
import com.example.androidapplication.data.model.auth.RegisterDTO;
import com.example.androidapplication.data.model.auth.ResetPasswordDTO;
import com.example.androidapplication.data.model.user.InformationDTO;
import com.example.androidapplication.data.repository.AuthRepository;

import java.util.Map;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public LiveData<Resource<ApiResponse<InformationDTO>>> login(String email, String password) {
        return authRepository.login(email, password);
    }

    public LiveData<Resource<ApiResponse<InformationDTO>>> register(RegisterDTO registerDTO) {
        return authRepository.register(registerDTO);
    }

    public LiveData<Resource<ApiResponse<InformationDTO>>> loginWithGoogle(String code) {
        return authRepository.loginWithGoogle(code);
    }

    public LiveData<Resource<ApiResponse<InformationDTO>>> loginWithGoogleIdToken(String idToken) {
        return authRepository.loginWithGoogleIdToken(idToken);
    }

    public LiveData<Resource<ApiResponse<Map<String, String>>>> forgotPassword(String email) {
        return authRepository.forgotPassword(email);
    }

    public LiveData<Resource<ApiResponse<Map<String, String>>>> verifyOtp(String email, String otp) {
        return authRepository.verifyOtp(email, otp);
    }

    public LiveData<Resource<ApiResponse<Object>>> resetPassword(ResetPasswordDTO dto) {
        return authRepository.resetPassword(dto);
    }
}