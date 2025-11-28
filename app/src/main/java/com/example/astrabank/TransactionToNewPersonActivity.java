package com.example.astrabank; // <--- Sửa lại tên package của bạn

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TransactionToNewPersonActivity extends AppCompatActivity {

    // --- Khai báo Views ---
    // Containers
    private LinearLayout layoutBankSelection;
    private LinearLayout layoutAccountInput;
    private LinearLayout headerSelectedBank; // Layout bao quanh Logo và Tên bank đã chọn

    // Common
    private ImageView btnClose;

    // List
    private RecyclerView rvBankList;
    private BankAdapter bankAdapter;
    private List<BankModel> listBanks;

    // Input Screen Views
    private TextView tvSelectedBankName;
    private ImageView ivSelectedBankLogo;
    private EditText etAccountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_to_new_person);

        // 1. Ánh xạ View
        initViews();

        // 2. Tạo dữ liệu giả
        createMockData();

        // 3. Cài đặt RecyclerView
        setupRecyclerView();

        // 4. Xử lý các sự kiện Click (Close, Reselect...)
        setupEvents();
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
    }

    private void createMockData() {
        listBanks = new ArrayList<>();
        // Thay hình ảnh bằng resource thực tế của bạn
        listBanks.add(new BankModel("Techcombank - TCB", "Vietnam Technological and Commercial Joint Stock Bank", R.drawable.ic_launcher_background));
        listBanks.add(new BankModel("MB Bank", "Military Commercial Joint Stock Bank", R.drawable.ic_launcher_background));
        listBanks.add(new BankModel("Vietcombank", "Joint Stock Commercial Bank for Foreign Trade of Vietnam", R.drawable.ic_launcher_background));
        listBanks.add(new BankModel("VietinBank", "Vietnam Joint Stock Commercial Bank for Industry and Trade", R.drawable.ic_launcher_background));
    }

    private void setupRecyclerView() {
        // Khi click vào 1 item ngân hàng trong List
        bankAdapter = new BankAdapter(listBanks, new BankAdapter.OnBankClickListener() {
            @Override
            public void onBankClick(BankModel bank) {
                // Điền thông tin vào màn hình input
                tvSelectedBankName.setText(bank.getShortName());
                ivSelectedBankLogo.setImageResource(bank.getLogoResId());
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