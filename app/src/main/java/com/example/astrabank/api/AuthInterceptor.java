package com.example.astrabank.api;

import androidx.annotation.NonNull;

import com.example.astrabank.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Tạo request mới dựa trên request cũ và thêm Header
        Request newRequest = originalRequest.newBuilder()
                // Lấy key từ BuildConfig đã tạo ở Bước 1
                .header("x-api-key", BuildConfig.API_KEY)
                // Hoặc nếu dùng Bearer Token:
                // .header("Authorization", "Bearer " + BuildConfig.API_KEY)
                .build();

        return chain.proceed(newRequest);
    }
}
