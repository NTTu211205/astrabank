package com.example.astrabank; //
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.adapters.TransactionHistoryAdapter;
import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Transaction;
import com.example.astrabank.utils.LoginManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountAndCardActivity extends AppCompatActivity {
    private final String LOG_TAG = "AccountAndCardActivity";

    // Khai báo các biến View
    private ImageButton btnBack, btnFavorite, btnToggleEye, btnCopyAccount;
    private TextView tvUserName,tvNumCard, tvBalance, tvSetNickname, tvPhoneNumber, tvAccountNumber;
    private Button btnViewMoreHistory;

    RecyclerView rvTransactionHistory;

    private boolean isHidden = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_and_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        setupEvents();
    }

    private void initViews() {
        // Nút bấm
        btnBack = findViewById(R.id.btnBack);
//        btnFavorite = findViewById(R.id.btnFavorite);
        btnToggleEye = findViewById(R.id.btnToggleEye);
        btnCopyAccount = findViewById(R.id.btnCopyAccount);
        btnViewMoreHistory = findViewById(R.id.btnViewMoreHistory);
        tvNumCard = findViewById(R.id.tv_numcard);

        // Text hiển thị
        tvUserName = findViewById(R.id.tvUserName);
        tvBalance = findViewById(R.id.tvBalance);
        tvSetNickname = findViewById(R.id.tvSetNickname);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvAccountNumber = findViewById(R.id.tvAccountNumber);

        rvTransactionHistory = findViewById(R.id.rl_transaction_history);

        String accountNumber = LoginManager.getInstance().getAccount().getAccountNumber();
        String fullName = LoginManager.getInstance().getUser().getFullName();
        tvNumCard.setText("..." + accountNumber.substring(accountNumber.length() - 4));
        tvAccountNumber.setText(accountNumber);
        tvSetNickname.setText(fullName);
        tvUserName.setText(fullName);
        tvBalance.setText(formatMoney(LoginManager.getInstance().getAccount().getBalance()));
        tvPhoneNumber.setText(LoginManager.getInstance().getUser().getPhone());

        callApiFindTransactionHistory(LoginManager.getInstance().getAccount().getAccountNumber());
    }

    private String formatMoney(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnToggleEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleInformationVisibility();
            }
        });

        btnCopyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(tvAccountNumber.getText().toString());
            }
        });

        btnViewMoreHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountAndCardActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleInformationVisibility() {
        isHidden = !isHidden;
        String accountNumber = LoginManager.getInstance().getAccount().getAccountNumber();

        if (isHidden) {
            tvNumCard.setText(accountNumber);
            btnToggleEye.setAlpha(0.5f);
        } else {
            tvNumCard.setText("...." + accountNumber.substring(accountNumber.length() - 5));
            btnToggleEye.setAlpha(1.0f);
        }
    }

    private void copyToClipboard(String textToCopy) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Account Number", textToCopy);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Copied: " + textToCopy, Toast.LENGTH_SHORT).show();
    }

    private void callApiFindTransactionHistory(String accountNumber) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<Transaction>>> call = apiService.getHistories(LoginManager.getInstance().getAccount().getAccountNumber());

        call.enqueue(new Callback<ApiResponse<List<Transaction>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Transaction>>> call, Response<ApiResponse<List<Transaction>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<Transaction>> apiResponse = response.body();

                    if (apiResponse != null && apiResponse.getResult() != null) {
                        TransactionHistoryAdapter transactionHistoryAdapter = new TransactionHistoryAdapter(
                                apiResponse.getResult(),
                                LoginManager.getInstance().getAccount().getAccountNumber(),
                                AccountAndCardActivity.this);

                        rvTransactionHistory.setLayoutManager(new LinearLayoutManager(AccountAndCardActivity.this));
                        rvTransactionHistory.setAdapter(transactionHistoryAdapter);
                    }
                    else {
                        Log.d(LOG_TAG, "Api response or result is null");
                        Toast.makeText(AccountAndCardActivity.this, "Transaction histories not found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(LOG_TAG, "Error from server");
                    Toast.makeText(AccountAndCardActivity.this, "Transaction histories not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Transaction>>> call, Throwable t) {
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AccountAndCardActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}