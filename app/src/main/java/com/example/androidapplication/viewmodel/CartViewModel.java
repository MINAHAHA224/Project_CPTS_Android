//package com.example.androidapplication.viewmodel;
//
//import android.app.Application;
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//import com.example.androidapplication.data.model.ApiResponse;
//import com.example.androidapplication.data.model.cart.Cart;
//import com.example.androidapplication.data.repository.CartRepository;
//
//public class CartViewModel extends AndroidViewModel {
//    private CartRepository cartRepository;
//    private LiveData<Boolean> isLoading;
//
//    public CartViewModel(@NonNull Application application) {
//        super(application);
//        cartRepository = new CartRepository(application);
//        isLoading = cartRepository.getIsLoading();
//    }
//
//    public LiveData<Boolean> getIsLoading() { return isLoading; }
//
//    public LiveData<ApiResponse<Cart>> getCart() { return cartRepository.getCart(); }
//
//    public LiveData<ApiResponse<Object>> addProductToCart(long productId) {
//        return cartRepository.addProductToCart(productId);
//    }
//
//    public LiveData<ApiResponse<Object>> deleteProductFromCart(long productId) {
//        return cartRepository.deleteProductFromCart(productId);
//    }
//
//    public LiveData<ApiResponse<Object>> deleteOneProductFromCart(long productId) {
//        return cartRepository.deleteOneProductFromCart(productId);
//    }
//    public LiveData<ApiResponse<Object>> addProductToCart(long productId, long quantity) {
//        return cartRepository.addProductDetailToCart(productId, quantity);
//    }
//}


package com.example.androidapplication.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer; // Import này

import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.cart.Cart;
import com.example.androidapplication.data.repository.CartRepository;

public class CartViewModel extends AndroidViewModel {
    private CartRepository cartRepository;
    private LiveData<Boolean> isLoading;

    // --- THÊM BIẾN NÀY ---
    // Biến này sẽ giữ dữ liệu giỏ hàng xuyên suốt
    private MutableLiveData<ApiResponse<Cart>> cartData = new MutableLiveData<>();

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository(application);
        isLoading = cartRepository.getIsLoading();
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }

    // --- SỬA HÀM NÀY ---
    // Hàm này chỉ trả về LiveData để Activity lắng nghe (Observe)
    public LiveData<ApiResponse<Cart>> getCartLiveData() {
        return cartData;
    }

    // --- THÊM HÀM NÀY ---
    // Hàm này dùng để GỌI API và cập nhật dữ liệu vào biến cartData
    public void loadCart() {
        // Gọi Repository, sau đó lấy kết quả gán vào cartData của ViewModel
        // Lưu ý: repository.getCart() trả về LiveData 1 lần, ta dùng observeForever để lấy giá trị
        cartRepository.getCart().observeForever(new Observer<ApiResponse<Cart>>() {
            @Override
            public void onChanged(ApiResponse<Cart> response) {
                cartData.setValue(response);
                // Quan trọng: Sau khi lấy xong thì bỏ observe để tránh rò rỉ bộ nhớ
                cartRepository.getCart().removeObserver(this);
            }
        });
    }

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