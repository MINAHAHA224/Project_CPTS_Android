package com.example.androidapplication.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.api.ApiService;
import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.product.Product;
import com.example.androidapplication.data.model.product.ProductDetail;
import com.example.androidapplication.data.model.product.ProductFilterResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private ApiService apiService;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public ProductRepository(Context context) {
        apiService = ApiClient.getClient(context).create(ApiService.class);
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<ApiResponse<List<Product>>> getAllProducts(String search ) {
        isLoading.setValue(true);
        MutableLiveData<ApiResponse<List<Product>>> data = new MutableLiveData<>();
        apiService.getAllProducts(search).enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Product>>> call, Response<ApiResponse<List<Product>>> response) {
                data.setValue(response.body());
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Product>>> call, Throwable t) {
                data.setValue(null);
                isLoading.setValue(false);
            }
        });
        return data;
    }

    public LiveData<ApiResponse<ProductDetail>> getProductDetail(long productId) {
        isLoading.setValue(true);
        MutableLiveData<ApiResponse<ProductDetail>> data = new MutableLiveData<>();
        apiService.getProductDetail(productId).enqueue(new Callback<ApiResponse<ProductDetail>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProductDetail>> call, Response<ApiResponse<ProductDetail>> response) {
                data.setValue(response.body());
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<ApiResponse<ProductDetail>> call, Throwable t) {
                data.setValue(null);
                isLoading.setValue(false);
            }
        });
        return data;
    }

    public LiveData<ApiResponse<Map<String, List<Product>>>> getHomePageData() {
        isLoading.setValue(true);
        MutableLiveData<ApiResponse<Map<String, List<Product>>>> data = new MutableLiveData<>();
        apiService.getHomePageData().enqueue(new Callback<ApiResponse<Map<String, List<Product>>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, List<Product>>>> call, Response<ApiResponse<Map<String, List<Product>>>> response) {
                data.setValue(response.body());
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, List<Product>>>> call, Throwable t) {
                data.setValue(null);
                isLoading.setValue(false);
            }
        });
        return data;
    }


    public LiveData<ApiResponse<Map<String, List<String>>>> getFilterOptions() {
        isLoading.setValue(true);
        MutableLiveData<ApiResponse<Map<String, List<String>>>> data = new MutableLiveData<>();
        apiService.getFilterOptions().enqueue(new Callback<ApiResponse<Map<String, List<String>>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, List<String>>>> call, Response<ApiResponse<Map<String, List<String>>>> response) {
                data.setValue(response.body());
                isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<ApiResponse<Map<String, List<String>>>> call, Throwable t) {
                data.setValue(null);
                isLoading.setValue(false);
            }
        });
        return data;
    }

    public LiveData<ApiResponse<ProductFilterResponse>> filterProducts( // SỬA Ở ĐÂY
                                                                        List<String> factories, List<String> targets, List<String> prices, String sort) {
        isLoading.setValue(true);
        MutableLiveData<ApiResponse<ProductFilterResponse>> data = new MutableLiveData<>(); // SỬA Ở ĐÂY
        apiService.filterProducts("1", factories, targets, prices, sort).enqueue(new Callback<ApiResponse<ProductFilterResponse>>() { // SỬA Ở ĐÂY
            @Override
            public void onResponse(Call<ApiResponse<ProductFilterResponse>> call, Response<ApiResponse<ProductFilterResponse>> response) { // SỬA Ở ĐÂY
                data.setValue(response.body());
                isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<ApiResponse<ProductFilterResponse>> call, Throwable t) { // SỬA Ở ĐÂY
                data.setValue(null);
                isLoading.setValue(false);
            }
        });
        return data;
    }

}