package com.example.androidapplication.activities;

import android.annotation.SuppressLint;
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

        // --- SỬA Ở ĐÂY: Dùng btnBack thay vì toolbar ---
        binding.btnBack.setOnClickListener(v -> finish());

        String payUrl = getIntent().getStringExtra("PAYMENT_URL");
        if (payUrl == null || payUrl.isEmpty()) {
            finish();
            return;
        }

        // Fake User-Agent để tránh bị Momo chặn trên WebView
        String desktopUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36";
        binding.webView.getSettings().setUserAgentString(desktopUserAgent);

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);

        // Xử lý WebViewClient để không mở trình duyệt ngoài
        binding.webView.setWebViewClient(new WebViewClient());

        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                binding.progressBar.setVisibility(newProgress < 100 ? View.VISIBLE : View.GONE);
            }
        });

        binding.webView.loadUrl(payUrl);
    }
}