package com.example.androidapplication.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.api.ApiService;
import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.cart.Cart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {
    private ApiService apiService;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public CartRepository(Context context) {
        apiService = ApiClient.getClient(context).create(ApiService.class);
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public LiveData<ApiResponse<Cart>> getCart() {
        isLoading.setValue(true);
        MutableLiveData<ApiResponse<Cart>> data = new MutableLiveData<>();
        apiService.getCart().enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                data.setValue(response.body());
                isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable t) {
                data.setValue(null);
                isLoading.setValue(false);
            }
        });
        return data;
    }

    public LiveData<ApiResponse<Object>> addProductToCart(long productId) {
        MutableLiveData<ApiResponse<Object>> data = new MutableLiveData<>();
        apiService.addProductToCart(productId).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<ApiResponse<Object>> deleteProductFromCart(long productId) {
        MutableLiveData<ApiResponse<Object>> data = new MutableLiveData<>();
        apiService.deleteProductFromCart(productId).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<ApiResponse<Object>> deleteOneProductFromCart(long productId) {
        MutableLiveData<ApiResponse<Object>> data = new MutableLiveData<>();
        apiService.deleteOneProductFromCart(productId).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<ApiResponse<Object>> addProductDetailToCart(long productId, long quantity) {
        MutableLiveData<ApiResponse<Object>> data = new MutableLiveData<>();
        apiService.addProductDetailToCart(productId, quantity).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}