package com.example.astrabank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.request.LoanRequest;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Loan;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCreateLoanActivity extends AppCompatActivity {
    private final String LOG_TAG = "AdminCreateLoanActivity";
    private TextInputEditText etAccountNumber, etName, etAmount, etTerm, etInterestRate, etAddress;
    private Button btnConfirm;
    String accountNumber;
    double interestRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_create_loan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        accountNumber = intent.getStringExtra("accountNumber");
        interestRate = intent.getDoubleExtra("rate", 0.0);

        if (interestRate == 0.0) {
            changeScreen(AdminDashboardActivity.class);
        }

        initView();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnConfirm.setText("●   ●   ●");
                btnConfirm.setEnabled(false);
                handleSubmit();
            }
        });

    }

    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        startActivity(intent);
        finish();
    }

    private void handleSubmit() {
        try {
            String name = etName.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            long amount = Long.parseLong(etAmount.getText().toString().replace(",", "").trim());
            int term = Integer.parseInt(etTerm.getText().toString().trim());

            LoanRequest request = new LoanRequest(
                    accountNumber,
                    term,
                    interestRate,
                    address,
                    amount,
                    name);

            Toast.makeText(this, accountNumber + " " + term + " " + interestRate + " " + address + " " + amount + " " + name, Toast.LENGTH_SHORT).show();

            callApiCreateLoan(request);

        } catch (NumberFormatException e) {
            btnConfirm.setEnabled(true);
            btnConfirm.setText("Confirm Loan");
            Toast.makeText(this, "Please check numeric fields!", Toast.LENGTH_SHORT).show();
        }
    }

    private void callApiCreateLoan(LoanRequest request) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Loan>> call = apiService.createLoan(request);

        call.enqueue(new Callback<ApiResponse<Loan>>() {
            @Override
            public void onResponse(Call<ApiResponse<Loan>> call, Response<ApiResponse<Loan>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Loan> apiResponse = response.body();

                    if (apiResponse != null && apiResponse.getResult() != null) {
                        Intent intent = new Intent(AdminCreateLoanActivity.this, AdminLoanActivity.class);
                        intent.putExtra("loanId", apiResponse.getResult().getLoanId());
                        btnConfirm.setEnabled(true);
                        btnConfirm.setText("Confirm Loan");
                        startActivity(intent);
                        finish();
                    }
                    else {
                        btnConfirm.setEnabled(true);
                        btnConfirm.setText("Confirm Loan");
                        Log.d(LOG_TAG, "Error from server, api response is null");
                        Toast.makeText(AdminCreateLoanActivity.this, "Loan not found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setText("Confirm Loan");
                    Log.d(LOG_TAG, "Loan not found");
                    Toast.makeText(AdminCreateLoanActivity.this, "Loan not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Loan>> call, Throwable t) {
                btnConfirm.setEnabled(true);
                btnConfirm.setText("Confirm Loan");
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AdminCreateLoanActivity.this, "Internet disconnected, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        etName = findViewById(R.id.et_name);
        etAmount = findViewById(R.id.et_amount);
        etTerm = findViewById(R.id.et_term);
        etAddress = findViewById(R.id.et_address);

        btnConfirm = findViewById(R.id.btn_confirm);
    }
}