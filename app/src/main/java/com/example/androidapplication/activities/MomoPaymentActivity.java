package com.example.androidapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidapplication.databinding.ActivityMomoPaymentBinding;

public class MomoPaymentActivity extends AppCompatActivity {

    private ActivityMomoPaymentBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMomoPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        String payUrl = getIntent().getStringExtra("PAYMENT_URL");
        if (payUrl == null || payUrl.isEmpty()) {
            finish();
            return;
        }

        // --- BẮT ĐẦU PHẦN SỬA LỖI QUAN TRỌNG ---

        // 1. Lấy User-Agent mặc định
        String defaultUserAgent = binding.webView.getSettings().getUserAgentString();
        // 2. Tạo một User-Agent giả mạo của trình duyệt Desktop (Chrome trên Windows)
        String desktopUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36";

        // 3. Set User-Agent mới cho WebView
        binding.webView.getSettings().setUserAgentString(desktopUserAgent);

        // --- KẾT THÚC PHẦN SỬA LỖI ---

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true); // Cần thiết cho một số trang thanh toán
        binding.webView.setWebViewClient(new WebViewClient()); // WebViewClient đơn giản là đủ
        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                binding.progressBar.setVisibility(newProgress < 100 ? View.VISIBLE : View.GONE);
            }
        });

        binding.webView.loadUrl(payUrl);
    }
}