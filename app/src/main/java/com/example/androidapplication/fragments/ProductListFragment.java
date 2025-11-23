package com.example.androidapplication.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.R;
import com.example.androidapplication.adapters.ProductAdapter;
import com.example.androidapplication.data.model.product.Product;
import com.example.androidapplication.data.model.product.ProductFilterResponse;
import com.example.androidapplication.databinding.FragmentProductListBinding;
import com.example.androidapplication.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;
import android.widget.EditText; // Import cái này
import android.view.inputmethod.EditorInfo; // Import cái này

public class ProductListFragment extends Fragment {

    private FragmentProductListBinding binding;
    private ProductViewModel productViewModel;
    private ProductAdapter productAdapter;
    private List<Product> mProductList; // List dữ liệu gốc

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
        mProductList = new ArrayList<>();

        productAdapter = new ProductAdapter(getContext(), mProductList);
        binding.gridViewProducts.setAdapter(productAdapter);

        // 1. Load dữ liệu ban đầu (null = lấy tất cả)
        fetchProducts(null);

        // 2. Sự kiện nút Lọc
        binding.btnFilter.setOnClickListener(v -> showFilterDialog());

        // 3. Sự kiện Swipe Refresh (Kéo để reload)
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            // Khi refresh thì xóa ô tìm kiếm và load lại tất cả
            binding.edtSearch.setText("");
            fetchProducts(null);
        });

        // 4. --- XỬ LÝ TÌM KIẾM ---
        binding.edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyword = binding.edtSearch.getText().toString().trim();
                fetchProducts(keyword); // Gọi hàm tìm kiếm
                return true;
            }
            return false;
        });
    }

//    private void loadAllProducts() {
//        binding.swipeRefreshLayout.setRefreshing(true);
//        productViewModel.getAllProducts().observe(getViewLifecycleOwner(), apiResponse -> {
//            binding.swipeRefreshLayout.setRefreshing(false);
//            if (apiResponse != null && apiResponse.getData() != null) {
//                mProductList.clear();
//                mProductList.addAll(apiResponse.getData());
//                productAdapter.notifyDataSetChanged(); // Cập nhật UI GridView
//            } else {
//                Toast.makeText(getContext(), "Không tải được dữ liệu", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    // Hàm gọi API lấy sản phẩm (Có search hoặc không)
    private void fetchProducts(String keyword) {
        binding.swipeRefreshLayout.setRefreshing(true);

        // Gọi API qua ViewModel, truyền keyword vào
        productViewModel.getAllProducts(keyword).observe(getViewLifecycleOwner(), apiResponse -> {
            binding.swipeRefreshLayout.setRefreshing(false);

            if (apiResponse != null && apiResponse.getData() != null) {
                mProductList.clear();
                mProductList.addAll(apiResponse.getData());
                productAdapter.notifyDataSetChanged();

                if (mProductList.isEmpty()) {
                    Toast.makeText(getContext(), "Không tìm thấy sản phẩm nào", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // --- LOGIC HIỂN THỊ VÀ XỬ LÝ DIALOG FILTER ---
    private void showFilterDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter);

        // Làm trong suốt nền dialog để bo góc đẹp hơn
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // Ánh xạ View trong Dialog
        RadioGroup rgSort = dialog.findViewById(R.id.rgSort);
        RadioGroup rgPrice = dialog.findViewById(R.id.rgPrice);

        CheckBox cbDell = dialog.findViewById(R.id.cbDell);
        CheckBox cbAsus = dialog.findViewById(R.id.cbAsus);
        CheckBox cbMsi = dialog.findViewById(R.id.cbMsi);
        CheckBox cbHp = dialog.findViewById(R.id.cbHp);
        CheckBox cbLenovo = dialog.findViewById(R.id.cbLenovo);
        CheckBox cbAcer = dialog.findViewById(R.id.cbAcer);

        Button btnApply = dialog.findViewById(R.id.btnApplyFilter);

        btnApply.setOnClickListener(v -> {
            // 1. Lấy giá trị Sắp xếp
            String sortValue = "";
            int selectedSortId = rgSort.getCheckedRadioButtonId();
            if (selectedSortId == R.id.rbSortAsc) sortValue = "gia-tang-dan";
            else if (selectedSortId == R.id.rbSortDesc) sortValue = "gia-giam-dan";

            // 2. Lấy giá trị Khoảng giá
            List<String> priceList = new ArrayList<>();
            int selectedPriceId = rgPrice.getCheckedRadioButtonId();
            // Mapping ID sang giá trị API cần (Trùng khớp với tag trong XML dialog nếu có)
            if (selectedPriceId == R.id.rbPriceLow) priceList.add("duoi-10-trieu");
            else if (selectedPriceId == R.id.rbPriceMid) priceList.add("10-20-trieu");
            else if (selectedPriceId == R.id.rbPriceHigh) priceList.add("tren-20-trieu");

            // 3. Lấy danh sách Hãng
            List<String> factoryList = new ArrayList<>();
            if (cbDell.isChecked()) factoryList.add("Dell");
            if (cbAsus.isChecked()) factoryList.add("Asus");
            if (cbMsi.isChecked()) factoryList.add("MSI");
            if (cbHp.isChecked()) factoryList.add("HP");
            if (cbLenovo.isChecked()) factoryList.add("Lenovo");
            if (cbAcer.isChecked()) factoryList.add("Acer");

            // 4. Gọi API Lọc
            callFilterApi(factoryList, priceList, sortValue);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void callFilterApi(List<String> factories, List<String> prices, String sort) {
        binding.swipeRefreshLayout.setRefreshing(true);

        // Gọi ViewModel
        // Lưu ý: targets truyền null nếu không lọc theo nhu cầu
        productViewModel.filterProducts(factories, null, prices, sort).observe(getViewLifecycleOwner(), apiResponse -> {
            binding.swipeRefreshLayout.setRefreshing(false);

            if (apiResponse != null && apiResponse.getData() != null) {
                ProductFilterResponse response = apiResponse.getData();

                if (response.getProducts() != null && !response.getProducts().isEmpty()) {
                    mProductList.clear(); // Xóa list cũ
                    mProductList.addAll(response.getProducts()); // Thêm list mới
                    productAdapter.notifyDataSetChanged(); // Báo Adapter vẽ lại
                    Toast.makeText(getContext(), "Tìm thấy " + mProductList.size() + " sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    mProductList.clear();
                    productAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Không tìm thấy sản phẩm phù hợp", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Lỗi khi lọc sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}