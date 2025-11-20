package com.example.androidapplication.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.androidapplication.adapters.OrderHistoryAdapter;
import com.example.androidapplication.databinding.FragmentOrdersBinding;
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

        setupRecyclerView();
        observeViewModel();

        binding.swipeRefreshLayout.setOnRefreshListener(this::fetchOrderHistory);

        fetchOrderHistory();
    }

    private void setupRecyclerView() {
        orderHistoryAdapter = new OrderHistoryAdapter(getContext(), new ArrayList<>());
        binding.recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewOrders.setAdapter(orderHistoryAdapter);
    }

    private void observeViewModel() {
        orderViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.swipeRefreshLayout.setRefreshing(isLoading);
        });

        orderViewModel.getOrderHistory().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null && apiResponse.getData() != null) {
                if (apiResponse.getData().isEmpty()) {
                    // Show empty view if needed
                } else {
                    orderHistoryAdapter = new OrderHistoryAdapter(getContext(), apiResponse.getData());
                    binding.recyclerViewOrders.setAdapter(orderHistoryAdapter);
                }
            } else {
                Toast.makeText(getContext(), "Failed to load order history", Toast.LENGTH_SHORT).show();
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