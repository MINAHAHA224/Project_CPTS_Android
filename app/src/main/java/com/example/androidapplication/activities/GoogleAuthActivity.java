package com.example.androidapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapplication.api.ApiClient;
import com.example.androidapplication.data.model.user.InformationDTO;
import com.example.androidapplication.databinding.ActivityGoogleAuthBinding;
import com.example.androidapplication.utils.SharedPrefManager;
import com.google.gson.Gson;

public class GoogleAuthActivity extends AppCompatActivity {

    private ActivityGoogleAuthBinding binding;
    public static final String GOOGLE_AUTH_URL = ApiClient.BASE_URL + "api/v1/auth/google";

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoogleAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nút back trên toolbar để đóng activity
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Cấu hình WebView
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });

        // Thêm một interface để JavaScript có thể gọi code Java
        binding.webView.addJavascriptInterface(new WebAppInterface(), "Android");

        // Tải trang đăng nhập Google từ backend
        binding.webView.loadUrl(GOOGLE_AUTH_URL);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // Sau khi trang callback của backend tải xong, chúng ta sẽ tiêm một đoạn JavaScript
            // vào trang để lấy dữ liệu người dùng (nếu có).
            // Backend của bạn sau khi thành công sẽ redirect về trang chủ kèm theo thông tin user.
            // Chúng ta cần một cách để backend "gửi" thông tin đó cho WebView.
            // Giải pháp: Backend sau khi login google thành công sẽ render ra một trang đơn giản
            // chứa JSON của InformationDTO và gọi vào interface của Android.

            // Backend cần sửa lại để trả về một trang HTML có đoạn script sau:
            /*
             <html><body>
                <script type="text/javascript">
                    // Lấy dữ liệu user từ model mà backend truyền vào
                    var userData = ${informationDTO_as_json_string};
                    Android.processUserData(JSON.stringify(userData));
                </script>
             </body></html>
            */
            // Đoạn code dưới đây để minh họa việc tiêm JS
            String script = "javascript:if(document.body.innerText.includes('{')){" +
                    "Android.processUserData(document.body.innerText);" +
                    "}";
            view.loadUrl(script);
        }
    }

    // Class này chứa các phương thức mà JavaScript trong WebView có thể gọi
    public class WebAppInterface {
        @JavascriptInterface
        public void processUserData(String json) {
            // JavaScript đã gọi hàm này và truyền vào chuỗi JSON của InformationDTO
            Log.d("WebAppInterface", "User data received: " + json);
            try {
                Gson gson = new Gson();
                InformationDTO user = gson.fromJson(json, InformationDTO.class);

                // Lưu thông tin người dùng và chuyển hướng
                SharedPrefManager.getInstance(getApplicationContext()).saveUser(user);

                runOnUiThread(() -> {
                    Intent intent = new Intent(GoogleAuthActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
            } catch (Exception e) {
                Log.e("WebAppInterface", "Error parsing user data", e);
            }
        }
    }
}