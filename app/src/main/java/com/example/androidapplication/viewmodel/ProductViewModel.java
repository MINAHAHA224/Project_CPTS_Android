package com.example.androidapplication.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.product.Product;
import com.example.androidapplication.data.model.product.ProductDetail;
import com.example.androidapplication.data.model.product.ProductFilterResponse;
import com.example.androidapplication.data.repository.ProductRepository;
import java.util.List;
import java.util.Map;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository productRepository;
    private LiveData<Boolean> isLoading;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
        isLoading = productRepository.getIsLoading();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<ApiResponse<List<Product>>> getAllProducts(String search) {
        return productRepository.getAllProducts(search);
    }

    public LiveData<ApiResponse<ProductDetail>> getProductDetail(long productId) {
        return productRepository.getProductDetail(productId);
    }

    public LiveData<ApiResponse<Map<String, List<Product>>>> getHomePageData() {
        return productRepository.getHomePageData();
    }


    public LiveData<ApiResponse<Map<String, List<String>>>> getFilterOptions() {
        return productRepository.getFilterOptions();
    }

    public LiveData<ApiResponse<ProductFilterResponse>> filterProducts( // SỬA Ở ĐÂY
                                                                        List<String> factories, List<String> targets, List<String> prices, String sort) {
        return productRepository.filterProducts(factories, targets, prices, sort);
    }
}