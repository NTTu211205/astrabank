package com.example.astrabank; // <--- Sửa lại tên package của bạn

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.adapters.BankAdapter;
import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.AccountResponse;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Bank;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionToNewPersonActivity extends AppCompatActivity {
    private final String LOG_TAG = "TransactionToNewPersonActivity";

    private LinearLayout layoutBankSelection;
    private LinearLayout layoutAccountInput;
    private LinearLayout headerSelectedBank; 

    private ImageView btnClose;

    private RecyclerView rvBankList;
    private BankAdapter bankAdapter;
    private List<Bank> listBanks;

    // Input Screen Views
    private TextView tvSelectedBankName;
    private ImageView ivSelectedBankLogo;
    private EditText etAccountNumber;

    AppCompatButton btContinue;

    String accountNumber;
    Bank selectedBank;
    AccountResponse account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_to_new_person);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        createMockData();

        setupEvents();

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountNumber = etAccountNumber.getText().toString();
                btContinue.setText("●   ●   ●");
                btContinue.setEnabled(false);

                if (selectedBank != null && !accountNumber.isEmpty()) {
                    // check stk
                    if (selectedBank.getBankSymbol().equals("ATB")) {
                        checkAccountNumber(accountNumber);
                    }
                    else {
                        checkAccountNumber(accountNumber, selectedBank.getBankSymbol());
                    }
                }
                else {
                    btContinue.setText("Continue");
                    btContinue.setEnabled(true);
                }
            }
        });
    }

    private void checkAccountNumber(String accountNumber, String bankSymbol) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<AccountResponse>> call = apiService.findAccount(accountNumber, bankSymbol);

        call.enqueue(new Callback<ApiResponse<AccountResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AccountResponse>> call, Response<ApiResponse<AccountResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<AccountResponse> apiResponse = response.body();

                    if (apiResponse != null) {
                        AccountResponse accountResponse = apiResponse.getResult();

                        if (accountResponse != null) {
                            account = accountResponse;
                            changeNextScreen(TransactionActivity.class);
                        }
                        else {
                            Toast.makeText(TransactionToNewPersonActivity.this, "Account not exist", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "Account not exist");
                            btContinue.setText("Continue");
                            btContinue.setEnabled(true);
                        }
                    }
                    else {
                        Toast.makeText(TransactionToNewPersonActivity.this, "Data failed to load.", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "Can not load data");
                        btContinue.setText("Continue");
                        btContinue.setEnabled(true);
                    }
                }
                else {
                    Toast.makeText(TransactionToNewPersonActivity.this, "Account not found", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Error from server");
                    btContinue.setText("Continue");
                    btContinue.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AccountResponse>> call, Throwable t) {
                Toast.makeText(TransactionToNewPersonActivity.this, "Network connection error", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Internet disconnect");
                btContinue.setText("Continue");
                btContinue.setEnabled(true);
            }
        });
    }

    private void checkAccountNumber(String accountNumber) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<AccountResponse>> call = apiService.findAccount(accountNumber);

        call.enqueue(new Callback<ApiResponse<AccountResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AccountResponse>> call, Response<ApiResponse<AccountResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<AccountResponse> apiResponse = response.body();

                    if (apiResponse != null) {
                        AccountResponse accountResponse = apiResponse.getResult();

                        if (accountResponse != null) {
                            // chuyền màn hình, show account info
                            account = accountResponse;
                            changeNextScreen(TransactionActivity.class);
                        }
                        else {
                            Toast.makeText(TransactionToNewPersonActivity.this, "The account does not exist.", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "Account not exist");
                            btContinue.setText("Continue");
                            btContinue.setEnabled(true);
                        }
                    }
                    else {
                        Toast.makeText(TransactionToNewPersonActivity.this, "Data failed to load.", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "Can not load data");
                        btContinue.setText("Continue");
                        btContinue.setEnabled(true);
                    }
                }
                else {
                    Toast.makeText(TransactionToNewPersonActivity.this, "Account not found", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Error from server");
                    btContinue.setText("Continue");
                    btContinue.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AccountResponse>> call, Throwable t) {
                Toast.makeText(TransactionToNewPersonActivity.this, "Network connection error", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Internet disconnect");
                btContinue.setText("Continue");
                btContinue.setEnabled(true);
            }
        });
    }

    private void initViews() {
        layoutBankSelection = findViewById(R.id.layout_bank_selection);
        layoutAccountInput = findViewById(R.id.layout_account_input);
        headerSelectedBank = findViewById(R.id.header_selected_bank);

        btnClose = findViewById(R.id.btn_close);
        rvBankList = findViewById(R.id.rv_bank_list);

        tvSelectedBankName = findViewById(R.id.tv_selected_bank_name);
        ivSelectedBankLogo = findViewById(R.id.iv_selected_bank_logo);
        etAccountNumber = findViewById(R.id.et_account_number);

        btContinue = findViewById(R.id.bt_continue);
    }

    private void createMockData() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<Bank>>> call = apiService.getAllBank();

        call.enqueue(new Callback<ApiResponse<List<Bank>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Bank>>> call, Response<ApiResponse<List<Bank>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<Bank>> apiResponse = response.body();

                    if (apiResponse != null) {
                        List<Bank> banks = apiResponse.getResult();

                        if (banks != null) {
                            listBanks = banks;
                            setupRecyclerView();
                        }
                    }
                    else {
                        Log.w(LOG_TAG, "API Success but body is null.");
                        Toast.makeText(TransactionToNewPersonActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        changePreScreen(LoggedInActivity.class);
                    }
                }
                else {
                    Log.e(LOG_TAG, "API Error. Code: " + response.code() + ", Msg: " + response.message());
                    Toast.makeText(TransactionToNewPersonActivity.this, "The server is unresponsive.", Toast.LENGTH_SHORT).show();
                    changePreScreen(LoggedInActivity.class);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Bank>>> call, Throwable t) {
                Log.e(LOG_TAG, "Network failure: " + t.getMessage());
                Toast.makeText(TransactionToNewPersonActivity.this, "Network connection error", Toast.LENGTH_SHORT).show();
                changePreScreen(LoggedInActivity.class);
            }
        });
    }

    private void changePreScreen(Class<?> preScreen) {
        Intent intent = new Intent(this, preScreen);
        startActivity(intent);
        finish();
    }

    private void changeNextScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        intent.putExtra("desBankSymbol", selectedBank.getBankSymbol());
        intent.putExtra("accountNumber", account.getAccountNumber());
        intent.putExtra("accountName", account.getAccountName());
        startActivity(intent);
        finish();
    }


    private void setupRecyclerView() {
        // Khi click vào 1 item ngân hàng trong List
        bankAdapter = new BankAdapter(listBanks, new BankAdapter.OnBankClickListener() {
            @Override
            public void onBankClick(Bank bank) {
                selectedBank = bank;
                tvSelectedBankName.setText(bank.getBankSymbol());

                if (bank.getLogoResId() != 0) {
                    ivSelectedBankLogo.setImageResource(bank.getLogoResId());
                } else {
                    ivSelectedBankLogo.setImageResource(getLogoDrawableBySymbol(bank.getBankSymbol()));
                }

                etAccountNumber.setText("");
                switchView(true);
                etAccountNumber.requestFocus();
            }
        });

        rvBankList.setLayoutManager(new LinearLayoutManager(this));
        rvBankList.setAdapter(bankAdapter);
    }
    private int getLogoDrawableBySymbol(String bankSymbol) {
        if (bankSymbol == null) return R.drawable.ic_account;

        switch (bankSymbol.toUpperCase()) {
            case "ATB": return R.drawable.ic_logo;
            case "HKH": return R.drawable.ic_hkh;
            default: return R.drawable.ic_account;
        }
    }
    private void setupEvents() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutAccountInput.getVisibility() == View.VISIBLE) {
                    switchView(false);
                    hideKeyboard();
                } else {
                    finish();
                }
            }
        });

        headerSelectedBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại màn hình chọn Bank
                switchView(false);
                hideKeyboard();
            }
        });
    }

    private void switchView(boolean isInputMode) {
        if (isInputMode) {
            layoutBankSelection.setVisibility(View.GONE);
            layoutAccountInput.setVisibility(View.VISIBLE);
        } else {
            layoutAccountInput.setVisibility(View.GONE);
            layoutBankSelection.setVisibility(View.VISIBLE);
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}