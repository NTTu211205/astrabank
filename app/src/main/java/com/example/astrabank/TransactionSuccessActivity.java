package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Bank;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionSuccessActivity extends AppCompatActivity {
    private final String LOG_TAG = "TransactionSuccessActivity";

    private TextView tvAmount;
    private TextView tvTitleAmount;

    private TextView tvMessageContent;
    private TextView tvTransactionIdContent;
    private TextView tvDateContent;

    // Phần thông tin người nhận
    private TextView tvReceiverLabel;
    private TextView tvReceiverName;
    private TextView tvReceiverAccount;

    // Phần thông tin ngân hàng
    private TextView tvBankLabel;
    private TextView tvBankName;

    // Nút bấm
    private Button btnHoanThanh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_transfer_success), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();

        tvAmount = findViewById(R.id.tv_amount);
        tvTitleAmount = findViewById(R.id.tv_title_amount);

        tvMessageContent = findViewById(R.id.tv_message_content);
        tvTransactionIdContent = findViewById(R.id.tv_transaction_id_content);
        tvDateContent = findViewById(R.id.tv_date_content);

        tvReceiverLabel = findViewById(R.id.tv_receiver_label);
        tvReceiverName = findViewById(R.id.tv_receiver_name);
        tvReceiverAccount = findViewById(R.id.tv_receiver_account);

        tvBankLabel = findViewById(R.id.tv_bank_label);
        tvBankName = findViewById(R.id.tv_bank_name);

        btnHoanThanh = findViewById(R.id.btn_hoan_thanh);

        // --- Xử lý sự kiện (Ví dụ) ---
        btnHoanThanh.setOnClickListener(v -> {
            changeScreen(LoggedInActivity.class);
        });

        long amount = intent.getLongExtra("amount", 0);
        String receiverName = intent.getStringExtra("receiverName");
        String bankSymbol = intent.getStringExtra("desBankSymbol"); // Key là desBankSymbol
        String content = intent.getStringExtra("content");
        String receiverAccountNumber = intent.getStringExtra("receiverAccountNumber");
        String date = intent.getStringExtra("date");
        String transId = intent.getStringExtra("transactionId");

        String moneyString = formatMoney(amount);
        tvAmount.setText(moneyString + " VND");
        tvReceiverName.setText(receiverName);
        tvReceiverAccount.setText(receiverAccountNumber);
        tvBankName.setText(bankSymbol);
        tvMessageContent.setText(content);
        tvDateContent.setText(date.substring(0, date.length() - 8).replace("T", " "));
        tvTransactionIdContent.setText(transId);

        getBank(bankSymbol);
    }

    private String formatMoney(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Bắt buộc dùng dấu chấm
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount);
    }

    private void getBank(String bankSymbol) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Bank>> call = apiService.getBank(bankSymbol);

        call.enqueue(new Callback<ApiResponse<Bank>>() {
            @Override
            public void onResponse(Call<ApiResponse<Bank>> call, Response<ApiResponse<Bank>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Bank> apiResponse = response.body();

                    if (apiResponse != null) {
                        Bank bank = apiResponse.getResult();

                        if (bank != null) {
                            tvBankName.setText(bank.toString());
                        }
                        else {
                            Log.d(LOG_TAG, "Data not found");
                        }
                    }
                    else {
                        Log.d(LOG_TAG, "Processing error");
                    }
                }
                else {
                    Log.d(LOG_TAG, "Error from server");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Bank>> call, Throwable t) {
                Log.d(LOG_TAG, "Internet disconnect");
            }
        });
    }

    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        startActivity(intent);
        finish();
    }
}