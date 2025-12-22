package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.adapters.AdminTransactionAdapter;
import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Transaction;
import com.example.astrabank.models.User;
import com.example.astrabank.utils.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardActivity extends AppCompatActivity {
    private final String LOG_TAG = "AdminDashboardActivity";
    RecyclerView rvTransactions;

    LinearLayout llCustomer, llMortgage;
    TextView tvTotalTransactions, tvTotalCustomers;
    Button btLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        llCustomer = findViewById(R.id.btn_nav_customers);
        llMortgage = findViewById(R.id.btn_nav_mortgage);
        rvTransactions = findViewById(R.id.rcl_transactions);
        tvTotalTransactions = findViewById(R.id.tv_total_transactions);
        tvTotalCustomers = findViewById(R.id.tv_total_customers);
        btLogOut = findViewById(R.id.btn_logout_admin);

        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(LoadingPageActivity.class);
                LoginManager.getInstance().setUser(null);
                LoginManager.getInstance().setAccount(null);
            }
        });

        llCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(AdminCustomerActivity.class);
            }
        });

        llMortgage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(AdminMortgageActivity.class);
            }
        });

        callApiFindAllTransaction();

        callAPiFindAllCustomer();
    }

    private void callAPiFindAllCustomer() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<User>>> call = apiService.getListCustomers();

        call.enqueue(new Callback<ApiResponse<List<User>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<User>>> call, Response<ApiResponse<List<User>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<User>> apiResponse = response.body();

                    if (apiResponse != null) {
                        List<User> users = apiResponse.getResult();

                        if (users != null) {
                            tvTotalCustomers.setText(users.size() + "");
                        }
                        else {
                            Log.d(LOG_TAG, "Users not found");
                            Toast.makeText(AdminDashboardActivity.this, "Customers not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Log.d(LOG_TAG, "Api response is null");
                        Toast.makeText(AdminDashboardActivity.this, "Customers not found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(LOG_TAG, "Customers not found");
                    Toast.makeText(AdminDashboardActivity.this, "Customers not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<User>>> call, Throwable t) {
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AdminDashboardActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callApiFindAllTransaction(){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<Transaction>>> call = apiService.getAllTransaction();

        call.enqueue(new Callback<ApiResponse<List<Transaction>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Transaction>>> call, Response<ApiResponse<List<Transaction>>> response) {
                if (response.isSuccessful()){
                    ApiResponse<List<Transaction>> apiResponse = response.body();

                    if (apiResponse != null) {
                        List<Transaction> transactions = apiResponse.getResult();

                        if (transactions != null) {
                            Log.d(LOG_TAG, transactions.size() + "");
                            AdminTransactionAdapter adapter = new AdminTransactionAdapter(transactions);
                            tvTotalTransactions.setText(transactions.size() + "");
                            rvTransactions.setAdapter(adapter);
                            rvTransactions.setLayoutManager(new LinearLayoutManager(AdminDashboardActivity.this));
                        }
                        else {
                            Log.d(LOG_TAG, "List transactions is null");
                            Toast.makeText(AdminDashboardActivity.this, "Transactions not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Log.d(LOG_TAG, "Api response is null");
                        Toast.makeText(AdminDashboardActivity.this, "Transactions not found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(LOG_TAG, "Transactions not found");
                    Toast.makeText(AdminDashboardActivity.this, "Transactions not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Transaction>>> call, Throwable t) {
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AdminDashboardActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        startActivity(intent);
        finish();
    }
}