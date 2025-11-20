package com.example.androidapplication.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.api.ApiService;
import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.ErrorResponse;
import com.example.androidapplication.data.model.Resource;
import com.example.androidapplication.data.model.user.ChangePasswordDTO;
import com.example.androidapplication.data.model.user.UserProfileUpdateDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private ApiService apiService;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public UserRepository(Context context) {
        apiService = ApiClient.getClient(context).create(ApiService.class);
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }

    // Hàm tiện ích để parse lỗi từ Response
    private ErrorResponse parseError(Response<?> response) {
        ResponseBody errorBody = response.errorBody();
        if (errorBody != null) {
            try {
                String errorBodyString = errorBody.string();
                Gson gson = new Gson();
                Type type = new TypeToken<ErrorResponse>() {}.getType();
                return gson.fromJson(errorBodyString, type);
            } catch (Exception e) {
                // Fallback error
            }
        }
        ErrorResponse fallbackError = new ErrorResponse();
        fallbackError.setMessage("An unknown error occurred (code: " + response.code() + ")");
        return fallbackError;
    }

    // Hàm tiện ích cho lỗi mạng
    private ErrorResponse getNetworkError(Throwable t) {
        ErrorResponse networkError = new ErrorResponse();
        networkError.setMessage("Network error: " + t.getMessage());
        return networkError;
    }


    public LiveData<Resource<ApiResponse<UserProfileUpdateDTO>>> getUserProfile() {
        MutableLiveData<Resource<ApiResponse<UserProfileUpdateDTO>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        isLoading.setValue(true);
        apiService.getUserProfile().enqueue(new Callback<ApiResponse<UserProfileUpdateDTO>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserProfileUpdateDTO>> call, Response<ApiResponse<UserProfileUpdateDTO>> response) {
                if(response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error(parseError(response), null));
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<ApiResponse<UserProfileUpdateDTO>> call, Throwable t) {
                result.setValue(Resource.error(getNetworkError(t), null));
                isLoading.setValue(false);
            }
        });
        return result;
    }

    // --- CÁC HÀM MỚI ---

    public LiveData<Resource<ApiResponse<Object>>> updateUserProfile(UserProfileUpdateDTO profile) {
        MutableLiveData<Resource<ApiResponse<Object>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        isLoading.setValue(true);
        apiService.updateUserProfile(profile).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if(response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error(parseError(response), null));
                }
                isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                result.setValue(Resource.error(getNetworkError(t), null));
                isLoading.setValue(false);
            }
        });
        return result;
    }

    public LiveData<Resource<ApiResponse<Object>>> changePassword(ChangePasswordDTO changePasswordDTO) {
        MutableLiveData<Resource<ApiResponse<Object>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        isLoading.setValue(true);
        apiService.changePassword(changePasswordDTO).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if(response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error(parseError(response), null));
                }
                isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                result.setValue(Resource.error(getNetworkError(t), null));
                isLoading.setValue(false);
            }
        });
        return result;
    }

    public LiveData<Resource<ApiResponse<Object>>> updateAvatar(MultipartBody.Part avatarFile) {
        MutableLiveData<Resource<ApiResponse<Object>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        isLoading.setValue(true);
        apiService.updateAvatar(avatarFile).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if(response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error(parseError(response), null));
                }
                isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                result.setValue(Resource.error(getNetworkError(t), null));
                isLoading.setValue(false);
            }
        });
        return result;
    }
}