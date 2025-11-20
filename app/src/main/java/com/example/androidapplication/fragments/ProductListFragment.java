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
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.androidapplication.adapters.ProductAdapter;
import com.example.androidapplication.data.model.product.Product;
import com.example.androidapplication.data.model.product.ProductFilterResponse;
import com.example.androidapplication.databinding.FragmentProductListBinding;
import com.example.androidapplication.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProductListFragment extends Fragment implements FilterBottomSheetFragment.FilterListener {

    private FragmentProductListBinding binding;
    private ProductViewModel productViewModel;
    private ProductAdapter productAdapter;

    // Store current filters
    private Map<String, List<String>> currentFilters;
    private String currentSort;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        setupRecyclerView();
        observeViewModel();
        setupEventListeners();

        // Initial fetch without filters
        fetchProducts();
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(getContext(), new ArrayList<>());
        binding.recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerViewProducts.setAdapter(productAdapter);
    }

    private void observeViewModel() {
        productViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.swipeRefreshLayout.setRefreshing(isLoading);
        });

        // Observer for getting ALL products (initial load)
        productViewModel.getAllProducts().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null && apiResponse.getData() != null) {
                productAdapter = new ProductAdapter(getContext(), apiResponse.getData());
                binding.recyclerViewProducts.setAdapter(productAdapter);
            } else {
                Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });

        // Observer for FILTERED products
        productViewModel.filterProducts(null, null, null, null).observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null && apiResponse.getData() != null) {
                // --- SỬA LỖI TRIỆT ĐỂ Ở ĐÂY ---
                // Giờ đây, getData() trả về một đối tượng ProductFilterResponse đã được parse đúng
                ProductFilterResponse filterResponse = apiResponse.getData();
                List<Product> productList = filterResponse.getProducts(); // Lấy danh sách sản phẩm một cách an toàn

                productAdapter = new ProductAdapter(getContext(), productList);
                binding.recyclerViewProducts.setAdapter(productAdapter);
            } else {
                Toast.makeText(getContext(), "Failed to apply filter", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupEventListeners() {
        binding.btnFilter.setOnClickListener(v -> {
            FilterBottomSheetFragment bottomSheet = new FilterBottomSheetFragment();
            bottomSheet.setFilterListener(this);
            bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
        });

        binding.swipeRefreshLayout.setOnRefreshListener(this::fetchProducts);
    }

    private void fetchProducts() {
        if (currentFilters == null && currentSort == null) {
            // Fetch all products if no filter is applied
            productViewModel.getAllProducts();
        } else {
            // Fetch with filters
            List<String> factories = currentFilters.getOrDefault("factory", Collections.emptyList());
            List<String> targets = currentFilters.getOrDefault("target", Collections.emptyList());
            List<String> prices = currentFilters.getOrDefault("price", Collections.emptyList());
            productViewModel.filterProducts(factories, targets, prices, currentSort);
        }
    }

    @Override
    public void onApplyFilters(Map<String, List<String>> selectedFilters, String sort) {
        this.currentFilters = selectedFilters;
        this.currentSort = sort;
        fetchProducts(); // Re-fetch data with new filters
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}