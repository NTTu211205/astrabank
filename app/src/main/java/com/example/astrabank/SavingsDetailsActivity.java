package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.models.Transaction;
import com.example.astrabank.adapters.TransactionAdapter;
import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Account;
import com.example.astrabank.utils.LoginManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavingsDetailsActivity extends AppCompatActivity {
    private final String LOG_TAG = "SavingsDetailsActivity";

    private RecyclerView rvHistory;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList;
    ImageButton btnBack;
    // Header Views
    private TextView tvTotalBalance;
    private TextView tvInterestRate;
    private TextView tvLastProfit;

    TextView tvAccountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_saving_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        setupHeaderData();

        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        rvHistory = findViewById(R.id.rv_transaction_history);
        tvTotalBalance = findViewById(R.id.tv_total_balance);
        tvInterestRate = findViewById(R.id.tv_interest_rate);
        tvLastProfit = findViewById(R.id.tv_last_profit);
        btnBack = findViewById(R.id.btnBack);
        tvAccountNumber = findViewById(R.id.tv_account_number);
    }

    private void setupHeaderData() {
        findSavingAccount(LoginManager.getInstance().getUser().getUserID(), "SAVING");
    }

    private void setupRecyclerView(String accountNumber) {
        transactionList = new ArrayList<>();


        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // 2. Gọi API
        Call<ApiResponse<List<Transaction>>> call = apiService.getHistories(accountNumber);

        call.enqueue(new Callback<ApiResponse<List<Transaction>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Transaction>>> call, Response<ApiResponse<List<Transaction>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Transaction>> apiResponse = response.body();

                    if (apiResponse.getResult() != null) {
                        List<Transaction> listGiaoDich = apiResponse.getResult();

                        if (listGiaoDich != null && !listGiaoDich.isEmpty()) {
                            adapter = new TransactionAdapter(listGiaoDich);
                            rvHistory.setLayoutManager(new LinearLayoutManager(SavingsDetailsActivity.this));
                            rvHistory.setAdapter(adapter);
                        } else {
                            Log.d(LOG_TAG, "No histories");
                        }
                    } else {
                        Log.d(LOG_TAG, "Get histories error");
                    }
                } else {
                    Log.d(LOG_TAG, "Histories not found");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Transaction>>> call, Throwable t) {
                Toast.makeText(SavingsDetailsActivity.this, "Connection Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private String formatMoney(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Bắt buộc dùng dấu chấm
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount);
    }

    private void findSavingAccount(String userID, String checking) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Account>> call = apiService.getDefaultAccount(userID, checking);

        call.enqueue(new Callback<ApiResponse<Account>>() {
            @Override
            public void onResponse(Call<ApiResponse<Account>> call, Response<ApiResponse<Account>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Account> apiResponse = response.body();

                    if (apiResponse != null) {
                        if (apiResponse.getResult() != null) {
                            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
                            Account account = apiResponse.getResult();
                            String accountNumber = account.getAccountNumber();

                            setupRecyclerView(accountNumber);
                            tvAccountNumber.setText("***"+accountNumber.substring(accountNumber.length()-5, accountNumber.length()));
                            tvTotalBalance.setText("VND " + formatMoney(account.getBalance()));
                            tvInterestRate.setText((account.getInterestRate() * 100) + "%");
                            long profit = (long) (account.getBalance() * account.getInterestRate() / 12);
                            tvLastProfit.setText(formatMoney(profit));
                        }
                    }
                    else {
                        Log.w(LOG_TAG, "API Success but body is null.");
                        Toast.makeText(SavingsDetailsActivity.this, "Default account not found", Toast.LENGTH_SHORT).show();
                        changeScreen(SeeAllAccountActivity.class);
                        LoginManager.clearUser();
                    }
                }
                else {
                    Log.e(LOG_TAG, "API Error. Code: " + response.code() + ", Msg: " + response.message());
                    Toast.makeText(SavingsDetailsActivity.this, "Client not Response", Toast.LENGTH_SHORT).show();
                    changeScreen(SeeAllAccountActivity.class);
                    LoginManager.clearUser();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Account>> call, Throwable t) {
                Log.e(LOG_TAG, "Network failure: " + t.getMessage());
                Toast.makeText(SavingsDetailsActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                changeScreen(SeeAllAccountActivity.class);
                LoginManager.clearUser();
            }
        });
    }

    private void findAccountHistories(String accountNumber) {

    }

    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        startActivity(intent);
        finish();
    }

}