package com.example.androidapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.androidapplication.R;
import com.example.androidapplication.activities.CartActivity;
import com.example.androidapplication.adapters.ProductAdapter;
import com.example.androidapplication.databinding.FragmentHomeBinding;
import com.example.androidapplication.viewmodel.ProductViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ProductViewModel productViewModel;
    private ProductAdapter featuredProductAdapter;
    private ProductAdapter newProductAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        setupRecyclerViews();
        observeViewModel();
        setupEventListeners();

        // Load data initially
        fetchHomePageData();
    }

    private void setupRecyclerViews() {
        // Adapter for Featured Products (Horizontal)
        featuredProductAdapter = new ProductAdapter(getContext(), new ArrayList<>());
        binding.featuredProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.featuredProductsRecyclerView.setAdapter(featuredProductAdapter);

        // Adapter for New Products (Grid)
        newProductAdapter = new ProductAdapter(getContext(), new ArrayList<>());
        binding.newProductsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.newProductsRecyclerView.setAdapter(newProductAdapter);
    }

    private void observeViewModel() {
        // Observe loading state
        productViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.swipeRefreshLayout.setRefreshing(isLoading);
        });

        // Observe home page data
        productViewModel.getHomePageData().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null && apiResponse.getData() != null) {
                // Update Featured Products
                if (apiResponse.getData().get("featuredProducts") != null) {
                    featuredProductAdapter = new ProductAdapter(getContext(), apiResponse.getData().get("featuredProducts"));
                    binding.featuredProductsRecyclerView.setAdapter(featuredProductAdapter);
                }

                // Update New Products
                if (apiResponse.getData().get("newProducts") != null) {
                    newProductAdapter = new ProductAdapter(getContext(), apiResponse.getData().get("newProducts"));
                    binding.newProductsRecyclerView.setAdapter(newProductAdapter);
                }
            } else {
                Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupEventListeners() {
        // Swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(this::fetchHomePageData);

        // Cart button click
        binding.actionCart.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CartActivity.class));
        });

        // Search button click (chúng ta sẽ làm chức năng này sau)
        binding.actionSearch.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Search functionality will be added soon!", Toast.LENGTH_SHORT).show();
        });

        // Tải ảnh banner (bạn có thể thay bằng ảnh động hoặc lấy từ API sau này)
        Glide.with(this)
                .load("https://lh3.googleusercontent.com/pw/AP1GczNw2z03jT1YqYvC6F-y621nZ2_F2D4Qd1_rQ7QYh8Q2Yc2b3e8s2n8zB8Z5J8M4K8xV3E4p3t3o1zRj8Q1jP6V1X1T3j4n6d0=w1920-h1080") // Thay URL ảnh banner của bạn vào đây
                .into(binding.imageBanner);
    }

    private void fetchHomePageData() {
        productViewModel.getHomePageData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}