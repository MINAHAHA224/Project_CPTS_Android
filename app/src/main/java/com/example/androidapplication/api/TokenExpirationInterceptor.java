package com.example.androidapplication.api;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.androidapplication.activities.AuthActivity;
import com.example.androidapplication.utils.SharedPrefManager;
import com.example.androidapplication.utils.ToastHandler;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenExpirationInterceptor implements Interceptor {
    private Context context;

    public TokenExpirationInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        // Backend của bạn khi gặp ExpiredJwtException thường sẽ trả về 401 hoặc 403
        // Kiểm tra mã lỗi
//        if (response.code() == 401 || response.code() == 403 || response.code() == 500) {
        if (response.code() == 401 || response.code() == 403 || response.code() == 500) {
            // Dữ liệu phản hồi body có thể chứa thông báo lỗi của backend, nhưng ta không cần thiết đọc nó ở đây
            // Ta chỉ cần biết là Token hỏng rồi.

            // Vì Interceptor chạy ở luồng mạng (Background Thread), muốn chuyển màn hình phải về Main Thread
            new Handler(Looper.getMainLooper()).post(() -> {

                // 1. Xóa sạch Token cũ trong máy đi
                SharedPrefManager.getInstance(context).clear();

                // 2. Thông báo nhẹ cho người dùng
//                Snackbar.make(context, "Giỏ hàng trống!", Snackbar.LENGTH_SHORT).show();

                ToastHandler.showToast(context, "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại!", Toast.LENGTH_LONG);

                // 3. Chuyển ngay lập tức về màn hình Login
                Intent intent = new Intent(context, AuthActivity.class);
                // Cờ này cực quan trọng: Xóa sạch các Activity cũ (Home, Profile...) để người dùng không bấm Back quay lại được
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            });
        }

        return response;
    }
}