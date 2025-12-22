package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.adapters.AdminCustomerAdapter;
import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCustomerActivity extends AppCompatActivity {
    private final String LOG_TAG = "AdminCustomerActivity";
    RecyclerView rvCustomers;
    Button btAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvCustomers = findViewById(R.id.rv_admin_customers);
        btAdd = findViewById(R.id.btn_add_customer);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreenNoFinish(InputPhoneNumberActivity.class);
            }
        });

        callAPiFindAllCustomer();

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(AdminDashboardActivity.class);
            }
        });
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
                            AdminCustomerAdapter adapter = new AdminCustomerAdapter(users);

                            rvCustomers.setAdapter(adapter);
                            rvCustomers.setLayoutManager(new LinearLayoutManager(AdminCustomerActivity.this));
                        }
                        else {
                            Log.d(LOG_TAG, "Users not found");
                            Toast.makeText(AdminCustomerActivity.this, "Customers not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Log.d(LOG_TAG, "Api response is null");
                        Toast.makeText(AdminCustomerActivity.this, "Customers not found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(LOG_TAG, "Customers not found");
                    Toast.makeText(AdminCustomerActivity.this, "Customers not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<User>>> call, Throwable t) {
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AdminCustomerActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeScreenNoFinish(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        intent.putExtra("admin", "create");
        startActivity(intent);
    }


    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        startActivity(intent);
        finish();
    }
}