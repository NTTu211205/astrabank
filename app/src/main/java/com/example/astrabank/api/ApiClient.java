package com.example.astrabank.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
//    private static final String BASE_URL = "https://astrabank-backend.onrender.com/api/";
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";

    public static Retrofit getClient() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // --- THÊM DÒNG NÀY ĐỂ GẮN KEY ---
                .addInterceptor(new AuthInterceptor())
                // ---------------------------------

                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        // 3. Tạo Retrofit
        return new Retrofit.Builder()
                .baseUrl(BASE_URL) // Đảm bảo BASE_URL kết thúc bằng dấu / (ví dụ: https://api.com/)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient) // Đưa client đã gắn key vào đây
                .build();
    }
}
