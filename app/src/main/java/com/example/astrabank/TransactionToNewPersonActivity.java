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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.adapters.BankAdapter;
import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Bank;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionToNewPersonActivity extends AppCompatActivity {
    private final String LOG_TAG = "TransactionToNewPersonActivity";

    // --- Khai báo Views ---
    // Containers
    private LinearLayout layoutBankSelection;
    private LinearLayout layoutAccountInput;
    private LinearLayout headerSelectedBank; 

    // Common
    private ImageView btnClose;

    // List
    private RecyclerView rvBankList;
    private BankAdapter bankAdapter;
    private List<Bank> listBanks;

    // Input Screen Views
    private TextView tvSelectedBankName;
    private ImageView ivSelectedBankLogo;
    private EditText etAccountNumber;

    AppCompatButton btContinue;

    Bank selectedBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_to_new_person);

        // 1. Ánh xạ View
        initViews();

        // 2. Tạo dữ liệu giả
        createMockData();

        // 4. Xử lý các sự kiện Click (Close, Reselect...)
        setupEvents();

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountNumber = etAccountNumber.getText().toString();

                if (selectedBank != null && !accountNumber.isEmpty()) {
                    if (selectedBank.getBankSymbol().equals("ATB")) {

                    }
                    else {

                    }
                }
            }
        });
    }

    private void initViews() {
        layoutBankSelection = findViewById(R.id.layout_bank_selection);
        layoutAccountInput = findViewById(R.id.layout_account_input);
        headerSelectedBank = findViewById(R.id.header_selected_bank); // Layout bấm để chọn lại

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
                        Toast.makeText(TransactionToNewPersonActivity.this, "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                        changePreScreen(LoggedInActivity.class);
                    }
                }
                else {
                    Log.e(LOG_TAG, "API Error. Code: " + response.code() + ", Msg: " + response.message());
                    Toast.makeText(TransactionToNewPersonActivity.this, "Máy chủ không phản hồi", Toast.LENGTH_SHORT).show();
                    changePreScreen(LoggedInActivity.class);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Bank>>> call, Throwable t) {
                Log.e(LOG_TAG, "Network failure: " + t.getMessage());
                Toast.makeText(TransactionToNewPersonActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                changePreScreen(LoggedInActivity.class);
            }
        });
    }

    private void changePreScreen(Class<?>preScreen) {
        Intent intent = new Intent(this, preScreen);
        startActivity(intent);
        finish();
    }

    private void setupRecyclerView() {
        // Khi click vào 1 item ngân hàng trong List
        bankAdapter = new BankAdapter(listBanks, new BankAdapter.OnBankClickListener() {
            @Override
            public void onBankClick(Bank bank) {
                selectedBank = bank;

                // Điền thông tin vào màn hình input
                tvSelectedBankName.setText(bank.getBankSymbol());
//                ivSelectedBankLogo.setImageResource(bank.getLogoResId());
                etAccountNumber.setText(""); // Reset ô nhập

                // Chuyển giao diện: Ẩn List -> Hiện Input
                switchView(true);

                // Focus vào ô nhập liệu
                etAccountNumber.requestFocus();
            }
        });

        rvBankList.setLayoutManager(new LinearLayoutManager(this));
        rvBankList.setAdapter(bankAdapter);
    }

    private void setupEvents() {
        // 1. Sự kiện nút Close (X)
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutAccountInput.getVisibility() == View.VISIBLE) {
                    // Nếu đang ở màn hình nhập -> Quay lại chọn Bank
                    switchView(false);
                    hideKeyboard();
                } else {
                    // Nếu đang ở màn hình chọn Bank -> Thoát Activity
                    finish();
                }
            }
        });

        // 2. Sự kiện bấm vào tên Ngân hàng để chọn lại (Reselect)
        headerSelectedBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại màn hình chọn Bank
                switchView(false);
                hideKeyboard();
            }
        });
    }

    // Hàm tiện ích để ẩn/hiện view
    // isInputMode = true (Hiện nhập liệu), false (Hiện danh sách)
    private void switchView(boolean isInputMode) {
        if (isInputMode) {
            layoutBankSelection.setVisibility(View.GONE);
            layoutAccountInput.setVisibility(View.VISIBLE);
        } else {
            layoutAccountInput.setVisibility(View.GONE);
            layoutBankSelection.setVisibility(View.VISIBLE);
        }
    }

    // Hàm ẩn bàn phím ảo cho gọn
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}