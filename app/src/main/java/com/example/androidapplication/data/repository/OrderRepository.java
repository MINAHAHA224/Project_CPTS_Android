package com.example.androidapplication.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.api.ApiService;
import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.order.InfoOrderRqDTO;
import com.example.androidapplication.data.model.order.Order;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private ApiService apiService;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public OrderRepository(Context context) {
        apiService = ApiClient.getClient(context).create(ApiService.class);
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public LiveData<ApiResponse<List<Order>>> getOrderHistory() {
        isLoading.setValue(true);
        MutableLiveData<ApiResponse<List<Order>>> data = new MutableLiveData<>();
        apiService.getOrderHistory().enqueue(new Callback<ApiResponse<List<Order>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {
                data.setValue(response.body());
                isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable t) {
                data.setValue(null);
                isLoading.setValue(false);
            }
        });
        return data;
    }

    public LiveData<ApiResponse<Map<String, String>>> placeOrder(InfoOrderRqDTO info) {
        isLoading.setValue(true);
        MutableLiveData<ApiResponse<Map<String, String>>> data = new MutableLiveData<>();
        apiService.processPayment(info).enqueue(new Callback<ApiResponse<Map<String, String>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, String>>> call, Response<ApiResponse<Map<String, String>>> response) {
                data.setValue(response.body());
                isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<ApiResponse<Map<String, String>>> call, Throwable t) {
                data.setValue(null);
                isLoading.setValue(false);
            }
        });
        return data;
    }
}