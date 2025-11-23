package com.example.androidapplication.api;

import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // IP máy ảo mặc định là 10.0.2.2, nếu chạy máy thật thì đổi thành IP LAN (ví dụ 192.168.1.x)
    public static final String BASE_URL = "http://10.0.2.2:8080/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            // Log để xem request/response trong Logcat (Debug)
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    // 1. Interceptor thêm Token vào Header (Để gửi đi)
                    .addInterceptor(new AuthInterceptor(context))

                    // 2. Interceptor kiểm tra Token hết hạn (Để xử lý lỗi trả về) - QUAN TRỌNG
                    .addInterceptor(new TokenExpirationInterceptor(context))

                    // 3. Interceptor Log
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}