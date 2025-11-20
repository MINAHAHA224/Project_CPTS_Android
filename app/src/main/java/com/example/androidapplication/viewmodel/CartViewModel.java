package com.example.androidapplication.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.cart.Cart;
import com.example.androidapplication.data.repository.CartRepository;

public class CartViewModel extends AndroidViewModel {
    private CartRepository cartRepository;
    private LiveData<Boolean> isLoading;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository(application);
        isLoading = cartRepository.getIsLoading();
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public LiveData<ApiResponse<Cart>> getCart() { return cartRepository.getCart(); }

    public LiveData<ApiResponse<Object>> addProductToCart(long productId) {
        return cartRepository.addProductToCart(productId);
    }

    public LiveData<ApiResponse<Object>> deleteProductFromCart(long productId) {
        return cartRepository.deleteProductFromCart(productId);
    }

    public LiveData<ApiResponse<Object>> deleteOneProductFromCart(long productId) {
        return cartRepository.deleteOneProductFromCart(productId);
    }
    public LiveData<ApiResponse<Object>> addProductToCart(long productId, long quantity) {
        return cartRepository.addProductDetailToCart(productId, quantity);
    }
}