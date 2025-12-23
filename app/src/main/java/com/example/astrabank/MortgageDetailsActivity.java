package com.example.astrabank;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import com.example.astrabank.adapters.PaymentAdapter;
import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.AccountResponse;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Account;
import com.example.astrabank.models.Loan;
import com.example.astrabank.models.LoanReceipt;
import com.example.astrabank.models.PaymentRecord;
import com.example.astrabank.utils.LoginManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MortgageDetailsActivity extends AppCompatActivity {
    private final String LOG_TAG = "MortgageDetailsActivity";

    private RecyclerView rvHistory;
    private PaymentAdapter adapter;
    private List<PaymentRecord> paymentList;
    ImageButton btnBack;

    private TextView tvDueAmount, tvDueDate, tvRecommend;
    private TextView tvPropertyAddress, tvOutstandingBalance, tvRate, tvTerm;
    private Button btnMakePayment;
    private LinearLayout llMortgageInfor;
    private RecyclerView rvPaymentHistory;

    private String receiptId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mortgage_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String accountNumber = intent.getStringExtra("accountNumber");

        callApiFindAccount(accountNumber);

        initViews();
        setupActions();
    }

    private void callApiFindAccount(String accountNumber) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Account>> call = apiService.findMortgageAccount(accountNumber);

        call.enqueue(new Callback<ApiResponse<Account>>() {
            @Override
            public void onResponse(Call<ApiResponse<Account>> call, Response<ApiResponse<Account>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Account> apiResponse = response.body();

                    if (apiResponse != null) {
                        if (apiResponse.getResult() != null) {
                            Account account = apiResponse.getResult();

                            if (account != null) {
                                if (account.getLoan()) {
                                    callApiFindLoan(account.getPresentLoanId());
                                }
                                else {
                                    // show so tien vay toi da
                                    btnMakePayment.setText("Create Loan");
                                    tvDueAmount.setText("VND " + formatMoney(account.getBalance()));
                                    llMortgageInfor.setVisibility(View.GONE);
                                    rvPaymentHistory.setVisibility(View.GONE);
                                }
                            }
                            else {
                                Log.d(LOG_TAG, "Account not found");
                                Toast.makeText(MortgageDetailsActivity.this, "Account not found", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                        else {
                            Log.d(LOG_TAG, "Account not found");
                            Toast.makeText(MortgageDetailsActivity.this, "Account not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Log.w(LOG_TAG, "API Success but body is null.");
                        Toast.makeText(MortgageDetailsActivity.this, "Không tìm thấy tài khoản mặc định", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Log.e(LOG_TAG, "API Error. Code: " + response.code() + ", Msg: " + response.message());
                    Toast.makeText(MortgageDetailsActivity.this, "Máy chủ không phản hồi", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Account>> call, Throwable t) {
                Log.e(LOG_TAG, "Network failure: " + t.getMessage());
                Toast.makeText(MortgageDetailsActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void callApiFindLoan(String loanId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Loan>> call = apiService.findLoan(loanId);

        call.enqueue(new Callback<ApiResponse<Loan>>() {
            @Override
            public void onResponse(Call<ApiResponse<Loan>> call, Response<ApiResponse<Loan>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Loan> apiResponse = response.body();
                    if (apiResponse != null) {
                        Loan loan = apiResponse.getResult();

                        if (loan != null) {
                            tvPropertyAddress.setText(loan.getAddress());
                            tvOutstandingBalance.setText("VND " + formatMoney(loan.getOriginalPrincipal()));
                            tvRate.setText("" + loan.getInterestRate());
                            tvTerm.setText(loan.getTerm() + " Month");
                            callApiFindReceipt(loan.getLoanId());
                        }
                        else {
                            Toast.makeText(MortgageDetailsActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "Not found");
                        }
                    }
                }
                else {
                    Toast.makeText(MortgageDetailsActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Call api error");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Loan>> call, Throwable t) {
                Toast.makeText(MortgageDetailsActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Internet disconnected");
            }
        });
    }

    private void callApiFindReceipt(String loanId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<LoanReceipt>>> call = apiService.findReceipt(loanId);

        call.enqueue(new Callback<ApiResponse<List<LoanReceipt>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<LoanReceipt>>> call, Response<ApiResponse<List<LoanReceipt>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<LoanReceipt>> apiResponse = response.body();
                    if (apiResponse != null) {
                        List<LoanReceipt> receipts = apiResponse.getResult();

                        if (receipts != null) {
                            LoanReceipt loanReceipt = findCurrentMonthReceiptOldSchool(receipts);
                            if (loanReceipt != null) {
                                tvDueAmount.setText("VND " + formatMoney(loanReceipt.getAmount()));
                                String date = loanReceipt.getFinalDate().toString();
                                tvDueDate.setText(date.substring(0, date.length() - 15));

                                if (loanReceipt.isPaid()) {
                                    btnMakePayment.setEnabled(false);
                                    btnMakePayment.setText("Receipt was paid");
                                }
                                else {
                                    receiptId = loanReceipt.getReceiptId();
                                    btnMakePayment.setEnabled(true);
                                    btnMakePayment.setText("Payment");
                                }
                            }
                            else {
                                tvDueDate.setText("Not found");
                                tvDueAmount.setText("VND 0");
                                btnMakePayment.setEnabled(false);
                                btnMakePayment.setText("This month receipt not found");
                            }

                            List<LoanReceipt> paidReceipts = receipts.stream()
                                    .filter(receipt -> receipt.isPaid())
                                    .collect(Collectors.toList());


                            setupRecyclerView(paidReceipts);
                        }
                        else {
                            Log.d(LOG_TAG, "Receipt not found");
                        }
                    }
                }
                else {
                    Toast.makeText(MortgageDetailsActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Call api error");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<LoanReceipt>>> call, Throwable t) {
                Toast.makeText(MortgageDetailsActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Internet disconnected");
            }
        });
    }

    private void initViews() {
        tvRate = findViewById(R.id.tv_loan_rate);
        tvRecommend = findViewById(R.id.tvRecommend);
        rvPaymentHistory = findViewById(R.id.rv_payment_history);
        llMortgageInfor = findViewById(R.id.llMortgageInfor);
        rvHistory = findViewById(R.id.rv_payment_history);
        tvDueAmount = findViewById(R.id.tv_due_amount);
        tvDueDate = findViewById(R.id.tv_due_date);
        tvPropertyAddress = findViewById(R.id.tv_property_address);
        tvOutstandingBalance = findViewById(R.id.tv_outstanding_balance);
        btnMakePayment = findViewById(R.id.btn_make_payment);
        btnBack = findViewById(R.id.btnBack);
        tvTerm = findViewById(R.id.tvTerm);
    }

    private String formatMoney(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Bắt buộc dùng dấu chấm
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount);
    }

    private void setupActions() {
        btnMakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(MortgagePaymentActivity.class, receiptId);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupRecyclerView(List<LoanReceipt> receipts) {
        adapter = new PaymentAdapter(receipts);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(adapter);
    }

    public LoanReceipt findCurrentMonthReceiptOldSchool(List<LoanReceipt> receipts) {
        LocalDate now = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = LocalDate.now();
        }
        int currentMonth = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentMonth = now.getMonthValue();
        }
        int currentYear = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentYear = now.getYear();
        }

        for (LoanReceipt receipt : receipts) {
            if (receipt.getFinalDate() != null) {
                LocalDate deadline = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deadline = receipt.getFinalDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (deadline.getMonthValue() == currentMonth && deadline.getYear() == currentYear) {
                        return receipt;
                    }
                }
            }
        }
        return null;
    }

    private void changeScreen(Class<?> nextScreen, String message) {
        Intent intent = new Intent(this, nextScreen);
        intent.putExtra("receiptId", message);
        startActivity(intent);
    }
}