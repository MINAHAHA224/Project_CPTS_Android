package com.example.androidapplication.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView; // Nhớ Import ListView
import android.widget.Toast;
import com.example.androidapplication.adapters.OrderHistoryAdapter;
import com.example.androidapplication.databinding.FragmentOrdersBinding;
import com.example.androidapplication.utils.ToastHandler;
import com.example.androidapplication.viewmodel.OrderViewModel;
import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;
    private OrderViewModel orderViewModel;
    private OrderHistoryAdapter orderHistoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        // SỬA LỖI Ở ĐÂY: Không dùng LayoutManager nữa
        orderHistoryAdapter = new OrderHistoryAdapter(getContext(), new ArrayList<>());
        binding.listViewOrders.setAdapter(orderHistoryAdapter); // Gán vào ListView

        observeViewModel();

        binding.swipeRefreshLayout.setOnRefreshListener(this::fetchOrderHistory);

        fetchOrderHistory();
    }

    private void observeViewModel() {
        orderViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.swipeRefreshLayout.setRefreshing(isLoading);
        });

        orderViewModel.getOrderHistory().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null && apiResponse.getData() != null) {
                // Vì Adapter BaseAdapter không có hàm updateList tự viết, ta tạo mới adapter gán vào
                // Hoặc bạn có thể viết thêm hàm updateData trong OrderHistoryAdapter giống CartAdapter
                orderHistoryAdapter = new OrderHistoryAdapter(getContext(), apiResponse.getData());
                binding.listViewOrders.setAdapter(orderHistoryAdapter);
            } else {
                ToastHandler.showToast(getContext(), "Lỗi tải lịch sử đơn hàng", Toast.LENGTH_SHORT);
            }
        });
    }

    private void fetchOrderHistory() {
        orderViewModel.getOrderHistory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}