package com.example.astrabank.api;

import com.example.astrabank.api.request.ChangePINRequest;
import com.example.astrabank.api.request.EmailLoginRequest;
import com.example.astrabank.api.request.LoginPhoneRequest;
import com.example.astrabank.api.request.SavingAccountRequest;
import com.example.astrabank.api.request.TransactionRequest;
import com.example.astrabank.api.request.UpdateUserRequest;
import com.example.astrabank.api.response.AccountResponse;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Account;
import com.example.astrabank.models.Bank;
import com.example.astrabank.models.Loan;
import com.example.astrabank.models.LoanReceipt;
import com.example.astrabank.models.Notification;
import com.example.astrabank.models.Transaction;
import com.example.astrabank.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("users/login-with-phone")
    Call<ApiResponse<User>> loginWithPhone(@Body LoginPhoneRequest loginPhoneRequest);

    @GET("users/validate-transferring")
    Call<ApiResponse<Boolean>> validateTransaction(
            @Query("userId") String userId,
            @Query("transactionPIN") String transactionPIN);

    @PUT("users/update-profile/{userId}")
    Call<ApiResponse<User>> updateProfile(@Path("userId") String userId, @Body UpdateUserRequest updateUserRequest);

    @PUT("users/change-pin")
    Call<ApiResponse<Boolean>> changePin(@Body ChangePINRequest changePINRequest);

    @GET("users/find-by-email/{email}")
    Call<ApiResponse<User>> findUserByEmail(@Path("email") String email);

    @GET("accounts/my-account")
    Call<ApiResponse<Account>> getDefaultAccount(@Query("userId") String uid, @Query("accountType") String accountType);

    @POST("users/login")
    Call<ApiResponse<User>> login (@Body EmailLoginRequest emailLoginRequest);

    @GET("banks/all")
    Call<ApiResponse<List<Bank>>> getAllBank();

    @GET("accounts/{accountNumber}")
    Call<ApiResponse<AccountResponse>> findAccount(@Path("accountNumber") String accountNumber);

    @GET("accounts/{accountNumber}/{bankSymbol}")
    Call<ApiResponse<AccountResponse>> findAccount(@Path("accountNumber") String accountNumber, @Path("bankSymbol") String bankSymbol);

    @GET("accounts/findAll/{userId}")
    Call<ApiResponse<List<Account>>> getAllMyAccount (@Path("userId") String userId);

    @GET("accounts/find-mortgage/{accountNumber}")
    Call<ApiResponse<Account>> findMortgageAccount(@Path("accountNumber") String accountNumber);

    @POST("accounts/create-saving-account")
    Call<ApiResponse<Account>> createSavingAccount(@Body SavingAccountRequest savingAccountRequest);

    @GET("accounts/find-loan/{loanId}")
    Call<ApiResponse<Loan>> findLoan(@Path("loanId") String loanId);

    @GET("accounts/find-receipt/{loanId}")
    Call<ApiResponse<List<LoanReceipt>>> findReceipt(@Path("loanId") String loanId);

    @POST("transactions/transfer")
    Call<ApiResponse<Transaction>> progressTransfer(@Body TransactionRequest transactionRequest);

    @GET("transactions/histories/{accountNumber}")
    Call<ApiResponse<List<Transaction>>> getHistories(@Path("accountNumber") String accountNumber);

    @POST("transactions/sendTransaction")
    Call<ApiResponse<Transaction>> sendTransaction(@Body TransactionRequest transactionRequest);

    @GET("transactions/{userId}")
    Call<ApiResponse<List<Notification>>> getUserHistories(@Path("userId") String userId);

    @GET("banks/{bankSymbol}")
    Call<ApiResponse<Bank>> getBank(@Path("bankSymbol") String bankSymbol);

}
