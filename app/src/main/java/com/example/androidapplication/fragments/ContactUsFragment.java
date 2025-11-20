package com.example.androidapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.androidapplication.databinding.FragmentContactUsBinding;

public class ContactUsFragment extends Fragment {
    private FragmentContactUsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentContactUsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}