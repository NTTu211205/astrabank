package com.example.astrabank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.adapters.ReceiptAdapter;
import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Loan;
import com.example.astrabank.models.LoanReceipt;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoanActivity extends AppCompatActivity {
    private final String LOG_TAG = "AdminLoanActivity";
    private ImageButton btnBack, btnAdd;
    private TextView tvAccountNumber, tvCompleteStatus, tvPrincipal, tvRate, tvTerm, tvCreatedAt, tvAddress, tvNoLoan;
    private CardView cardView;
    private RecyclerView rvReceipt;
    Loan loan;
    String accountNumber;
    double rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_loan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String loanId = intent.getStringExtra("loanId");
        accountNumber = intent.getStringExtra("accountNumber");
        rate = intent.getDoubleExtra("rate", 0.0);

        initView();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(AdminMortgageActivity.class);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loan == null || loan.isComplete()) {
                    Intent intent = new Intent(AdminLoanActivity.this, AdminCreateLoanActivity.class);
                    intent.putExtra("accountNumber", accountNumber);
                    intent.putExtra("rate", rate);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(AdminLoanActivity.this, "You have incomplete loan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        callApiFindLoan(loanId);

        callApiLoadReceipt(loanId);
    }

    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        startActivity(intent);
        finish();
    }

    private void initView() {
        // Header Buttons
        btnBack = findViewById(R.id.btn_back);
        btnAdd = findViewById(R.id.btn_add);

        // Loan Information TextViews
        tvAccountNumber = findViewById(R.id.tv_account_number);
        tvCompleteStatus = findViewById(R.id.tv_complete_status);
        tvPrincipal = findViewById(R.id.tv_principal);
        tvRate = findViewById(R.id.tv_rate);
        tvTerm = findViewById(R.id.tv_term);
        tvCreatedAt = findViewById(R.id.tv_created_at);
        tvAddress = findViewById(R.id.tv_address);
        tvNoLoan = findViewById(R.id.tvNoLoan);
        cardView = findViewById(R.id.cardView);

        // RecyclerView
        rvReceipt = findViewById(R.id.rvReceipt);
    }

    private void callApiFindLoan(String loanId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Loan>> call = apiService.findLoan(loanId);

        call.enqueue(new Callback<ApiResponse<Loan>>() {
            @Override
            public void onResponse(Call<ApiResponse<Loan>> call, Response<ApiResponse<Loan>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Loan> apiResponse = response.body();

                    if (apiResponse != null && apiResponse.getResult() != null) {
                        loan = apiResponse.getResult();
                        tvAccountNumber.setText(loan.getAccountNumber());
                        if (loan.isComplete()) {
                            tvCompleteStatus.setText("COMPLETE");
                            tvCompleteStatus.setTextColor(Color.GREEN);
                        }
                        else {
                            tvCompleteStatus.setText("INCOMPLETE");
                            tvCompleteStatus.setTextColor(Color.RED);
                        }
                        tvPrincipal.setText(formatMoney(loan.getOriginalPrincipal()) + " VND");
                        tvRate.setText(loan.getInterestRate() + " %");
                        String date = loan.getCreatedAt().toString();
                        tvCreatedAt.setText(date.substring(0, date.length() - 15));
                        tvAddress.setText(loan.getAddress());
                        tvTerm.setText(loan.getTerm() + " Month");
                    }
                    else {
                        tvNoLoan.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.GONE);
                        Log.d(LOG_TAG, "Error from server, api response is null");
                        Toast.makeText(AdminLoanActivity.this, "Loan not found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    tvNoLoan.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.GONE);
                    Log.d(LOG_TAG, "Loan not found");
                    Toast.makeText(AdminLoanActivity.this, "Loan not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Loan>> call, Throwable t) {
                tvNoLoan.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.GONE);
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AdminLoanActivity.this, "Internet disconnected, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callApiLoadReceipt(String loanId){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<LoanReceipt>>> call = apiService.findReceipt(loanId);

        call.enqueue(new Callback<ApiResponse<List<LoanReceipt>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<LoanReceipt>>> call, Response<ApiResponse<List<LoanReceipt>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<LoanReceipt>> apiResponse = response.body();

                    if (apiResponse !=null && apiResponse.getResult() != null) {
                        List<LoanReceipt> receipts = apiResponse.getResult();
                        receipts.sort(Comparator.comparingInt(LoanReceipt::getPeriod));
                        setUpRecyclerView(receipts);
                    }
                    else {
                        Log.d(LOG_TAG, "Api response is null");
                        Toast.makeText(AdminLoanActivity.this, "Receipt not found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(LOG_TAG, "Receipt not found");
                    Toast.makeText(AdminLoanActivity.this, "Receipt not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<LoanReceipt>>> call, Throwable t) {
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AdminLoanActivity.this, "Internet disconnected, try again", Toast.LENGTH_SHORT).show();
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

    private void setUpRecyclerView(List<LoanReceipt> receipts) {
        ReceiptAdapter adapter = new ReceiptAdapter(this, receipts);

        rvReceipt.setAdapter(adapter);
        rvReceipt.setLayoutManager(new LinearLayoutManager(this));
    }

}