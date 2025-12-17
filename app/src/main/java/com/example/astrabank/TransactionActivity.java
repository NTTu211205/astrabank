package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.astrabank.models.Account;
import com.example.astrabank.utils.LoginManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionActivity extends AppCompatActivity {
    private final String LOG_TAG = "TransactionActivity";
    Button btnMakePayment;
    String desAccountNumber, desAccountName, desBankSymbol;
    TextView tvReceiverName, tvReceiverAccount, tvBalance;
    EditText etContent, etAmount;
    Spinner snAccountType;
    Account selectedAccount;
    long amount;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvReceiverName = findViewById(R.id.tv_recipient_name);
        tvReceiverAccount = findViewById(R.id.tv_recipient_account);
        etContent = findViewById(R.id.et_content);
        etAmount = findViewById(R.id.et_amount);
        snAccountType = findViewById(R.id.sn_account_type);
        tvBalance = findViewById(R.id.tv_balance_amount);
        snAccountType = findViewById(R.id.sn_account_type);

        setupSpinnerListener();

        getAccountData(LoginManager.getInstance().getUser().getUserID());

        Intent intent = getIntent();
        desAccountNumber = intent.getStringExtra("accountNumber");
        desAccountName = intent.getStringExtra("accountName");
        desBankSymbol = intent.getStringExtra("desBankSymbol");

        tvReceiverName.setText(desAccountName);
        tvReceiverAccount.setText(desAccountNumber);
        etContent.setText(LoginManager.getInstance().getUser().getFullName() + " CHUYEN KHOAN");

        btnMakePayment = findViewById(R.id.btn_confirm_payment);
        btnMakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMakePayment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        amount = Long.parseLong(etAmount.getText().toString());
                        content = etContent.getText().toString();
                        changeNextScreen(FinishTransactionActivity.class);
                    }
                });
            }
        });
    }

    private void changeNextScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        intent.putExtra("desAccountNumber", desAccountNumber);
        intent.putExtra("receiverName", desAccountName);
        intent.putExtra("desBankSymbol", desBankSymbol);
        intent.putExtra("sourceAccountNumber", selectedAccount.getAccountNumber());
        intent.putExtra("senderName", LoginManager.getInstance().getUser().getFullName());
        intent.putExtra("sourceBankSymbol", "ATB");
        intent.putExtra("amount", amount);
        intent.putExtra("content", content);
        startActivity(intent);
    }

    private String formatMoney(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Bắt buộc dùng dấu chấm
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount);
    }


    private void getAccountData(String userID) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<Account>>>  call = apiService.getAllMyAccount(userID);

        call.enqueue(new Callback<ApiResponse<List<Account>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Account>>> call, Response<ApiResponse<List<Account>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<Account>> apiResponse = response.body();

                    if (apiResponse != null) {
                        List<Account> accounts = apiResponse.getResult();

                        if (!accounts.isEmpty()) {
                            fillSpinnerData(accounts);
                        }
                        else {
                            Toast.makeText(TransactionActivity.this, "Không tìm được tài khoản phù hợp", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "Can not load account data");
                        }
                    }
                    else {
                        Toast.makeText(TransactionActivity.this, "Không tìm được tài khoản phù hợp", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "Can not load account data");
                    }
                }
                else {
                    Toast.makeText(TransactionActivity.this, "Máy chủ không phản hồi", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Error from server");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Account>>> call, Throwable t) {
                Toast.makeText(TransactionActivity.this, "Kiểm tra kết nối internet", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Internet disconnect");
            }
        });
    }


    private void fillSpinnerData(List<Account> accountList) {
        ArrayAdapter<Account> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                accountList
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snAccountType.setAdapter(adapter);
        snAccountType.setSelection(0);

    }

    private void setupSpinnerListener() {
        snAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy đối tượng Account tại vị trí được chọn
                // Vì ta truyền List<Account> vào Adapter, nên ở đây ép kiểu về (Account) được
                selectedAccount = (Account) parent.getItemAtPosition(position);

                tvBalance.setText("VND   " + formatMoney(selectedAccount.getBalance()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì
            }
        });
    }
}