package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Account;
import com.example.astrabank.models.LoanReceipt;
import com.example.astrabank.utils.LoginManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MortgagePaymentActivity extends AppCompatActivity {
    private final String LOG_TAG = "MortgagePaymentActivity";
    private ImageView ivBack;
    private EditText etAmount;
    private EditText etContent;
    TextView tvAccountNumber, tvBalance;
    private Button btnConfirm;
    private String receiptId;
    private String accountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mortgage_payment);

        Intent intent = getIntent();
        receiptId = intent.getStringExtra("receiptId");

        initViews();
//        setupDefaultData();
        setupActions();

        callApiFindCheckingAccount();

        callApiFindReceipt(receiptId);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chuyen man hinh va goi thanh toan
                changeNextScreen(FinishTransactionActivity.class);
            }
        });
    }

    private void changeNextScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        intent.putExtra("fromPayMortgageReceipt", "payMortgageReceipt");
        intent.putExtra("receiptId", receiptId);
        intent.putExtra("accountNumber", accountNumber);
        startActivity(intent);
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        etAmount = findViewById(R.id.et_amount);
        etContent = findViewById(R.id.et_content);
        btnConfirm = findViewById(R.id.btn_confirm_payment);
        tvAccountNumber = findViewById(R.id.tv_account_name);
        tvBalance = findViewById(R.id.tv_balance_amount);
    }

//    private void setupDefaultData() {
//        // 1. Generate Transaction Code (MD + 12 Random Digits)
//        String transactionCode = generateTransactionCode();
//
//        // 2. Get Current Date (Month Year)
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.US);
//        String currentDate = sdf.format(new Date());
//
//        // 3. Set Default Text
//        // Format: "Payment for [Month/Year] Ref: [Code]"
//        String defaultContent = "Payment for " + currentDate + " Ref: " + transactionCode;
//        etContent.setText(defaultContent);
//    }

    private String generateTransactionCode() {
        StringBuilder sb = new StringBuilder("MD");
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10)); // Appends 0-9
        }
        return sb.toString();
    }

    private void setupActions() {
        // Back Button Logic
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close activity
            }
        });

        // Confirm Button Logic
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = etAmount.getText().toString();
                String content = etContent.getText().toString();

                if (amount.isEmpty()) {
                    Toast.makeText(MortgagePaymentActivity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Simulate Payment Success
                Toast.makeText(MortgagePaymentActivity.this,
                        "Payment of VND" + amount + " Successful!\n" + content,
                        Toast.LENGTH_LONG).show();

                finish(); // Go back to previous screen after payment
            }
        });
    }

    private void callApiFindReceipt(String receiptId){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<LoanReceipt>> call = apiService.findReceiptById(receiptId);

        call.enqueue(new Callback<ApiResponse<LoanReceipt>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoanReceipt>> call, Response<ApiResponse<LoanReceipt>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<LoanReceipt> apiResponse = response.body();

                    if (apiResponse != null) {
                        LoanReceipt loanReceipt = apiResponse.getResult();

                        if (loanReceipt != null) {
                            etAmount.setText(formatMoney(loanReceipt.getAmount()));
                            etContent.setText("THANH TOAN KHOAN VAY KI HAN " + loanReceipt.getPeriod());
                        }
                        else {
                            Log.d(LOG_TAG, "Receipt not found");
                            Toast.makeText(MortgagePaymentActivity.this, "Receipt not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    else {
                        Log.d(LOG_TAG, "Error from server");
                        Toast.makeText(MortgagePaymentActivity.this, "Error from server", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else {
                    Log.d(LOG_TAG, "Error from server");
                    Toast.makeText(MortgagePaymentActivity.this, "Error from server", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoanReceipt>> call, Throwable t) {
                Log.e(LOG_TAG, "Network failure: " + t.getMessage());
                Toast.makeText(MortgagePaymentActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void callApiFindCheckingAccount() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Account>> call = apiService.getDefaultAccount(
                LoginManager.getInstance().getUser().getUserID(),
                "CHECKING");

        call.enqueue(new Callback<ApiResponse<Account>>() {
            @Override
            public void onResponse(Call<ApiResponse<Account>> call, Response<ApiResponse<Account>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Account> apiResponse = response.body();

                    if (apiResponse != null) {
                        if (apiResponse.getResult() != null) {
                            Account account = apiResponse.getResult();
                            accountNumber = account.getAccountNumber();
                            LoginManager.getInstance().setAccount(account);

                            tvAccountNumber.setText(account.getAccountType() + "(..." + account.getAccountNumber().substring(account.getAccountNumber().length() - 5, account.getAccountNumber().length()) + ")");
                            tvBalance.setText("VND " + formatMoney(account.getBalance()));
                        }
                        else {
                            Log.w(LOG_TAG, "Checking account not found");
                            Toast.makeText(MortgagePaymentActivity.this, "Checking account not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Log.w(LOG_TAG, "API Success but body is null.");
                        Toast.makeText(MortgagePaymentActivity.this, "Account not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Log.e(LOG_TAG, "API Error. Code: " + response.code() + ", Msg: " + response.message());
                    Toast.makeText(MortgagePaymentActivity.this, "Error from server", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Account>> call, Throwable t) {
                Log.e(LOG_TAG, "Network failure: " + t.getMessage());
                Toast.makeText(MortgagePaymentActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private String formatMoney(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount);
    }
}