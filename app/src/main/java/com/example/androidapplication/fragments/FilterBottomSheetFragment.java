package com.example.androidapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.R;
import com.example.androidapplication.databinding.FragmentFilterBottomSheetBinding;
import com.example.androidapplication.viewmodel.ProductViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment {

    private FragmentFilterBottomSheetBinding binding;
    private ProductViewModel productViewModel;
    private FilterListener filterListener;

    // Interface to communicate with ProductListFragment
    public interface FilterListener {
        void onApplyFilters(Map<String, List<String>> selectedFilters, String sort);
    }

    public void setFilterListener(FilterListener listener) {
        this.filterListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeViewModel();
        setupEventListeners();

        // Fetch filter options from API
        productViewModel.getFilterOptions();
        // Manually add price range chips
        populatePriceChips();
    }

    private void observeViewModel() {
        productViewModel.getFilterOptions().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null && apiResponse.getData() != null) {
                Map<String, List<String>> options = apiResponse.getData();
                if (options.containsKey("factories")) {
                    populateChipGroup(binding.chipGroupFactory, options.get("factories"));
                }
                if (options.containsKey("targets")) {
                    populateChipGroup(binding.chipGroupTarget, options.get("targets"));
                }
            } else {
                Toast.makeText(getContext(), "Failed to load filter options", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupEventListeners() {
        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.btnApply.setOnClickListener(v -> applyFilters());
        binding.btnReset.setOnClickListener(v -> resetFilters());
    }

    private void applyFilters() {
        Map<String, List<String>> selectedFilters = new HashMap<>();
        selectedFilters.put("factory", getSelectedChips(binding.chipGroupFactory));
        selectedFilters.put("target", getSelectedChips(binding.chipGroupTarget));
        selectedFilters.put("price", getSelectedPriceChips(binding.chipGroupPrice));

        String sort = getSelectedSort(binding.radioGroupSort);

        if (filterListener != null) {
            filterListener.onApplyFilters(selectedFilters, sort);
        }
        dismiss();
    }

    private void resetFilters() {
        binding.chipGroupFactory.clearCheck();
        binding.chipGroupTarget.clearCheck();
        binding.chipGroupPrice.clearCheck();
        binding.radioGroupSort.clearCheck();
    }

    private void populateChipGroup(ChipGroup chipGroup, List<String> items) {
        chipGroup.removeAllViews();
        for (String item : items) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_filter, chipGroup, false);
            chip.setText(item);
            chipGroup.addView(chip);
        }
    }

    private void populatePriceChips() {
        Map<String, String> priceRanges = new HashMap<>();
        priceRanges.put("Dưới 10 triệu", "duoi-10-trieu");
        priceRanges.put("10 - 15 triệu", "10-15-trieu");
        priceRanges.put("15 - 20 triệu", "15-20-trieu");
        priceRanges.put("Trên 20 triệu", "tren-20-trieu");

        binding.chipGroupPrice.removeAllViews();
        for (Map.Entry<String, String> entry : priceRanges.entrySet()) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_filter, binding.chipGroupPrice, false);
            chip.setText(entry.getKey());
            chip.setTag(entry.getValue()); // Store the API value in the tag
            binding.chipGroupPrice.addView(chip);
        }
    }

    private List<String> getSelectedChips(ChipGroup chipGroup) {
        return chipGroup.getCheckedChipIds().stream()
                .map(id -> ((Chip) chipGroup.findViewById(id)).getText().toString())
                .collect(Collectors.toList());
    }

    private List<String> getSelectedPriceChips(ChipGroup chipGroup) {
        return chipGroup.getCheckedChipIds().stream()
                .map(id -> ((Chip) chipGroup.findViewById(id)).getTag().toString())
                .collect(Collectors.toList());
    }

    private String getSelectedSort(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.radio_sort_asc) {
            return "gia-tang-dan";
        } else if (selectedId == R.id.radio_sort_desc) {
            return "gia-giam-dan";
        }
        return ""; // Default or no sort
    }
}