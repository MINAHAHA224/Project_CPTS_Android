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

import com.bumptech.glide.Glide;
import com.example.androidapplication.R;
import com.example.androidapplication.activities.CartActivity;
import com.example.androidapplication.activities.MainActivity;
import com.example.androidapplication.adapters.ProductAdapter;
import com.example.androidapplication.data.model.product.Product; // Đảm bảo import đúng Model
import com.example.androidapplication.databinding.FragmentHomeBinding;
import com.example.androidapplication.utils.ExpandableHeightGridView;
import com.example.androidapplication.utils.ToastHandler;
import com.example.androidapplication.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ProductViewModel productViewModel;
    private ProductAdapter productAdapter; // Dùng Adapter mới (BaseAdapter)
    private List<Product> productList;

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

        // Khởi tạo list và adapter
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productList);

        // Cấu hình GridView
        binding.gvNewProducts.setExpanded(true); // Bật chế độ mở rộng chiều cao
        binding.gvNewProducts.setAdapter(productAdapter);

        observeViewModel();
        setupEventListeners();

        // Load dữ liệu
        fetchHomePageData();
    }

    private void observeViewModel() {
        // Loading
        productViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.swipeRefreshLayout.setRefreshing(isLoading);
        });

        // Dữ liệu trang chủ
        productViewModel.getHomePageData().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null && apiResponse.getData() != null) {
                productList.clear();
                // Lấy danh sách sản phẩm mới từ API
                if (apiResponse.getData().get("newProducts") != null) {
                    productList.addAll(apiResponse.getData().get("newProducts"));
                }
                // Thông báo adapter cập nhật lại giao diện
                productAdapter.notifyDataSetChanged();
            } else {
                ToastHandler.showToast(getContext(), "Không thể tải dữ liệu", Toast.LENGTH_SHORT);
            }
        });
    }

    private void setupEventListeners() {
        // Swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(this::fetchHomePageData);

        // Click giỏ hàng
        binding.actionCart.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CartActivity.class));
        });

        // Click tìm kiếm
        binding.actionSearch.setOnClickListener(v -> {
            // Lấy MainActivity hiện tại
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();

                // Tìm thanh Bottom Navigation
                BottomNavigationView bottomNav = mainActivity.findViewById(R.id.bottom_nav_view);

                // Chuyển tab sang tab "Sản phẩm" (ID này phải khớp với menu bottom_nav_menu.xml)
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.navigation_products);
                }
            }
        });

        // Banner giả (sau này thay bằng link thật)
        Glide.with(this)
                .load("https://tse1.mm.bing.net/th/id/OIP.ulCA3Ao9gj2sC3lcz7vRgQHaEK?rs=1&pid=ImgDetMain&o=7&rm=3")
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