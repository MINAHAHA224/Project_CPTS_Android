package com.example.androidapplication.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.order.InfoOrderRqDTO;
import com.example.androidapplication.data.model.order.Order;
import com.example.androidapplication.data.repository.OrderRepository;
import java.util.List;
import java.util.Map;

public class OrderViewModel extends AndroidViewModel {
    private OrderRepository orderRepository;
    private LiveData<Boolean> isLoading;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
        isLoading = orderRepository.getIsLoading();
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public LiveData<ApiResponse<List<Order>>> getOrderHistory() {
        return orderRepository.getOrderHistory();
    }

    public LiveData<ApiResponse<Map<String, String>>> placeOrder(InfoOrderRqDTO info) {
        return orderRepository.placeOrder(info);
    }
}