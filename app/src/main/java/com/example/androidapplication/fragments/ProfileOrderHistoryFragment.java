package com.example.androidapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androidapplication.adapters.OrderHistoryAdapter;
import com.example.androidapplication.databinding.FragmentProfileOrderHistoryBinding;
import com.example.androidapplication.viewmodel.OrderViewModel;

import java.util.ArrayList;

public class ProfileOrderHistoryFragment extends Fragment {
    private FragmentProfileOrderHistoryBinding binding;
    private OrderViewModel orderViewModel;
    private OrderHistoryAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileOrderHistoryBinding.inflate(inflater, container, false);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        observeViewModel();

        binding.swipeRefreshLayout.setOnRefreshListener(() -> orderViewModel.getOrderHistory());
        orderViewModel.getOrderHistory();
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderHistoryAdapter(getContext(), new ArrayList<>());
        binding.recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewOrders.setAdapter(orderAdapter);
    }

    private void observeViewModel() {
        orderViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.swipeRefreshLayout.setRefreshing(isLoading);
        });

        orderViewModel.getOrderHistory().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null && apiResponse.getData() != null) {
                orderAdapter = new OrderHistoryAdapter(getContext(), apiResponse.getData());
                binding.recyclerViewOrders.setAdapter(orderAdapter);
            } else {
                Toast.makeText(getContext(), "Failed to load order history", Toast.LENGTH_SHORT).show();
            }
        });
    }
}