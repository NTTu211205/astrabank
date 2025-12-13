package com.example.astrabank.api;

import com.example.astrabank.api.request.LoginPhoneRequest;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Account;
import com.example.astrabank.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("users/login-with-phone")
    Call<ApiResponse<User>> loginWithPhone(@Body LoginPhoneRequest loginPhoneRequest);

    @GET("accounts/my-account")
    Call<ApiResponse<Account>> getDefaultAccount(@Query("userId") String uid, @Query("accountType") String accountType);

}
