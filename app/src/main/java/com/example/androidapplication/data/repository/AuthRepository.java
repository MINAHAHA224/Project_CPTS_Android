package com.example.androidapplication.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.api.ApiService;
import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.ErrorResponse;
import com.example.androidapplication.data.model.Resource;
import com.example.androidapplication.data.model.auth.LoginDTO;
import com.example.androidapplication.data.model.auth.RegisterDTO;
import com.example.androidapplication.data.model.auth.ResetPasswordDTO;
import com.example.androidapplication.data.model.user.InformationDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private ApiService apiService;

    public AuthRepository(Context context) {
        apiService = ApiClient.getClient(context).create(ApiService.class);
    }

    public LiveData<Resource<ApiResponse<InformationDTO>>> login(String email, String password) {
        MutableLiveData<Resource<ApiResponse<InformationDTO>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        LoginDTO loginDTO = new LoginDTO(email, password);
        apiService.login(loginDTO).enqueue(new Callback<ApiResponse<InformationDTO>>() {
            @Override
            public void onResponse(Call<ApiResponse<InformationDTO>> call, Response<ApiResponse<InformationDTO>> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    // --- SỬA LỖI Ở ĐÂY ---
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        try {
                            // 1. Đọc errorBody thành String. Thao tác này tiêu thụ luồng.
                            String errorBodyString = errorBody.string();

                            // 2. Bây giờ parse từ String. String có thể được đọc nhiều lần.
                            Gson gson = new Gson();
                            Type type = new TypeToken<ErrorResponse>() {}.getType();
                            ErrorResponse errorResponse = gson.fromJson(errorBodyString, type);

                            result.setValue(Resource.error(errorResponse, null));
                        } catch (Exception e) {
                            // Lỗi xảy ra nếu JSON không hợp lệ hoặc do lỗi đọc luồng
                            ErrorResponse fallbackError = new ErrorResponse();
                            fallbackError.setMessage("Error parsing response: " + e.getMessage());
                            result.setValue(Resource.error(fallbackError, null));
                        }
                    } else {
                        ErrorResponse fallbackError = new ErrorResponse();
                        fallbackError.setMessage("An unknown error occurred (empty error body).");
                        result.setValue(Resource.error(fallbackError, null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<InformationDTO>> call, Throwable t) {
                ErrorResponse networkError = new ErrorResponse();
                networkError.setMessage("Network error. Please check your connection.");
                result.setValue(Resource.error(networkError, null));
            }
        });
        return result;
    }

    // Áp dụng tương tự cho hàm register
    public LiveData<Resource<ApiResponse<InformationDTO>>> register(RegisterDTO registerDTO) {
        MutableLiveData<Resource<ApiResponse<InformationDTO>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.register(registerDTO).enqueue(new Callback<ApiResponse<InformationDTO>>() {
            @Override
            public void onResponse(Call<ApiResponse<InformationDTO>> call, Response<ApiResponse<InformationDTO>> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        try {
                            String errorBodyString = errorBody.string();
                            Gson gson = new Gson();
                            Type type = new TypeToken<ErrorResponse>() {}.getType();
                            ErrorResponse errorResponse = gson.fromJson(errorBodyString, type);
                            result.setValue(Resource.error(errorResponse, null));
                        } catch (Exception e) {
                            ErrorResponse fallbackError = new ErrorResponse();
                            fallbackError.setMessage("Error parsing response: " + e.getMessage());
                            result.setValue(Resource.error(fallbackError, null));
                        }
                    } else {
                        ErrorResponse fallbackError = new ErrorResponse();
                        fallbackError.setMessage("An unknown error occurred (empty error body).");
                        result.setValue(Resource.error(fallbackError, null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<InformationDTO>> call, Throwable t) {
                ErrorResponse networkError = new ErrorResponse();
                networkError.setMessage("Network error. Please check your connection.");
                result.setValue(Resource.error(networkError, null));
            }
        });
        return result;
    }

// Trong file .../data/repository/AuthRepository.java

    // ...
    // Thêm phương thức mới này
    public LiveData<Resource<ApiResponse<InformationDTO>>> loginWithGoogle(String code) {
        MutableLiveData<Resource<ApiResponse<InformationDTO>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.loginWithGoogle(code).enqueue(new Callback<ApiResponse<InformationDTO>>() {
            @Override
            public void onResponse(Call<ApiResponse<InformationDTO>> call, Response<ApiResponse<InformationDTO>> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    // Xử lý lỗi (bạn có thể copy logic parse lỗi từ hàm login thường)
                    ResponseBody errorBody = response.errorBody();
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setMessage("Google login failed on server.");
                    result.setValue(Resource.error(errorResponse, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<InformationDTO>> call, Throwable t) {
                ErrorResponse networkError = new ErrorResponse();
                networkError.setMessage("Network error. Please check your connection.");
                result.setValue(Resource.error(networkError, null));
            }
        });
        return result;
    }

    public LiveData<Resource<ApiResponse<InformationDTO>>> loginWithGoogleIdToken(String idToken) {
        MutableLiveData<Resource<ApiResponse<InformationDTO>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        Map<String, String> payload = new HashMap<>();
        payload.put("idToken", idToken);

        apiService.loginWithGoogleIdToken(payload).enqueue(new Callback<ApiResponse<InformationDTO>>() {
            @Override
            public void onResponse(Call<ApiResponse<InformationDTO>> call, Response<ApiResponse<InformationDTO>> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    // Xử lý lỗi
                    ErrorResponse error = new ErrorResponse();
                    error.setMessage("Server failed to process Google Sign-In.");
                    result.setValue(Resource.error(error, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<InformationDTO>> call, Throwable t) {
                // Xử lý lỗi mạng
                ErrorResponse error = new ErrorResponse();
                error.setMessage("Network error: " + t.getMessage());
                result.setValue(Resource.error(error, null));
            }
        });
        return result;
    }


    public LiveData<Resource<ApiResponse<Map<String, String>>>> forgotPassword(String email) {
        MutableLiveData<Resource<ApiResponse<Map<String, String>>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        apiService.forgotPassword(email).enqueue(new Callback<ApiResponse<Map<String, String>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, String>>> call, Response<ApiResponse<Map<String, String>>> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    // --- SỬA LỖI Ở ĐÂY ---
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        try {
                            // 1. Đọc errorBody thành String. Thao tác này tiêu thụ luồng.
                            String errorBodyString = errorBody.string();

                            // 2. Bây giờ parse từ String. String có thể được đọc nhiều lần.
                            Gson gson = new Gson();
                            Type type = new TypeToken<ErrorResponse>() {}.getType();
                            ErrorResponse errorResponse = gson.fromJson(errorBodyString, type);

                            result.setValue(Resource.error(errorResponse, null));
                        } catch (Exception e) {
                            // Lỗi xảy ra nếu JSON không hợp lệ hoặc do lỗi đọc luồng
                            ErrorResponse fallbackError = new ErrorResponse();
                            fallbackError.setMessage("Error parsing response: " + e.getMessage());
                            result.setValue(Resource.error(fallbackError, null));
                        }
                    } else {
                        ErrorResponse fallbackError = new ErrorResponse();
                        fallbackError.setMessage("An unknown error occurred (empty error body).");
                        result.setValue(Resource.error(fallbackError, null));
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Map<String, String>>> call, Throwable t) {
                ErrorResponse networkError = new ErrorResponse();
                networkError.setMessage("Network error. Please check your connection.");
                result.setValue(Resource.error(networkError, null));
            }
        });
        return result;
    }


    public LiveData<Resource<ApiResponse<Map<String, String>>>> verifyOtp(String email, String otp) {
        MutableLiveData<Resource<ApiResponse<Map<String, String>>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        apiService.verifyOtp(email, otp, "VERIFY-OTP").enqueue(new Callback<ApiResponse<Map<String, String>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, String>>> call, Response<ApiResponse<Map<String, String>>> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    // --- SỬA LỖI Ở ĐÂY ---
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        try {
                            // 1. Đọc errorBody thành String. Thao tác này tiêu thụ luồng.
                            String errorBodyString = errorBody.string();

                            // 2. Bây giờ parse từ String. String có thể được đọc nhiều lần.
                            Gson gson = new Gson();
                            Type type = new TypeToken<ErrorResponse>() {}.getType();
                            ErrorResponse errorResponse = gson.fromJson(errorBodyString, type);

                            result.setValue(Resource.error(errorResponse, null));
                        } catch (Exception e) {
                            // Lỗi xảy ra nếu JSON không hợp lệ hoặc do lỗi đọc luồng
                            ErrorResponse fallbackError = new ErrorResponse();
                            fallbackError.setMessage("Error parsing response: " + e.getMessage());
                            result.setValue(Resource.error(fallbackError, null));
                        }
                    } else {
                        ErrorResponse fallbackError = new ErrorResponse();
                        fallbackError.setMessage("An unknown error occurred (empty error body).");
                        result.setValue(Resource.error(fallbackError, null));
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Map<String, String>>> call, Throwable t) {
                ErrorResponse networkError = new ErrorResponse();
                networkError.setMessage("Network error. Please check your connection.");
                result.setValue(Resource.error(networkError, null));
            }
        });
        return result;
    }




    public LiveData<Resource<ApiResponse<Object>>> resetPassword(ResetPasswordDTO dto) {
        MutableLiveData<Resource<ApiResponse<Object>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        apiService.resetPassword(dto).enqueue(new Callback<ApiResponse<Object>>() {

            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    // --- SỬA LỖI Ở ĐÂY ---
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        try {
                            // 1. Đọc errorBody thành String. Thao tác này tiêu thụ luồng.
                            String errorBodyString = errorBody.string();

                            // 2. Bây giờ parse từ String. String có thể được đọc nhiều lần.
                            Gson gson = new Gson();
                            Type type = new TypeToken<ErrorResponse>() {}.getType();
                            ErrorResponse errorResponse = gson.fromJson(errorBodyString, type);

                            result.setValue(Resource.error(errorResponse, null));
                        } catch (Exception e) {
                            // Lỗi xảy ra nếu JSON không hợp lệ hoặc do lỗi đọc luồng
                            ErrorResponse fallbackError = new ErrorResponse();
                            fallbackError.setMessage("Error parsing response: " + e.getMessage());
                            result.setValue(Resource.error(fallbackError, null));
                        }
                    } else {
                        ErrorResponse fallbackError = new ErrorResponse();
                        fallbackError.setMessage("An unknown error occurred (empty error body).");
                        result.setValue(Resource.error(fallbackError, null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                ErrorResponse networkError = new ErrorResponse();
                networkError.setMessage("Network error. Please check your connection.");
                result.setValue(Resource.error(networkError, null));
            }
        });
        return result;
    }
}