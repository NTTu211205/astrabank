package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.adapters.MortgageAdapter;
import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Account;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMortgageActivity extends AppCompatActivity {
    private final String LOG_TAG = "AdminMortgageActivity";
    RecyclerView rvMortgageAccounts;
    ImageButton btAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_mortgage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvMortgageAccounts = findViewById(R.id.rv_admin);
        btAdd = findViewById(R.id.btn_add);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(AdminDashboardActivity.class);
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(AdminAddMortgageActivity.class);
            }
        });

        callApiFindMortgageAccounts();
    }

    private void callApiFindMortgageAccounts() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<Account>>> call = apiService.getMortgageAccounts();

        call.enqueue(new Callback<ApiResponse<List<Account>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Account>>> call, Response<ApiResponse<List<Account>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<Account>> apiResponse = response.body();

                    if (apiResponse != null && apiResponse.getResult() != null) {
                        List<Account> accounts = apiResponse.getResult();

                        setUpRecyclerView(accounts);
                    }
                    else {
                        Log.d(LOG_TAG, "Error from server, not found mortgage accounts");
                        Toast.makeText(AdminMortgageActivity.this, "Error from server, not found mortgage accounts", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(LOG_TAG, "Mortgage accounts not found");
                    Toast.makeText(AdminMortgageActivity.this, "Mortgage accounts not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Account>>> call, Throwable t) {
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AdminMortgageActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecyclerView(List<Account> accounts) {
        MortgageAdapter adapter = new MortgageAdapter(accounts, this, new MortgageAdapter.OnItemClick() {
            @Override
            public void onItemClick(Account account) {
                Intent intent = new Intent(AdminMortgageActivity.this, AdminLoanActivity.class);
                intent.putExtra("accountNumber", account.getAccountNumber());
                intent.putExtra("rate", account.getInterestRate());
                intent.putExtra("loanId", account.getPresentLoanId());
                startActivity(intent);
                finish();            }
        });

        rvMortgageAccounts.setAdapter(adapter);
        rvMortgageAccounts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        startActivity(intent);
        finish();
    }

    private void changeScreen(Class<?> nextScreen, String loanId) {
        Intent intent = new Intent(this, nextScreen);
        intent.putExtra("loanId", loanId);
        startActivity(intent);
        finish();
    }
}