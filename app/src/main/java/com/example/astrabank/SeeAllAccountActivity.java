package com.example.astrabank;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class SeeAllAccountActivity extends AppCompatActivity {

    // 1. Khai báo các biến View
    private ImageButton btnBack;

    // Layout cha của từng thẻ (để bắt sự kiện click vào cả thẻ)
    private RelativeLayout rlChecking, rlSavings, rlMortgage;

    // Các TextView hiển thị dữ liệu
    private TextView tvCheckingNumber, tvCheckingBalance;
    private TextView tvSavingsNumber, tvSavingsBalance, tvInterestRate, tvMonthlyProfit;
    private TextView tvMortgageNumber, tvPaymentAmount, tvPaymentFrequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_account);
        initViews();
        setupData();
        setupEvents();
    }

    // 2. Ánh xạ ID từ XML sang Java
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);

        // Layouts
        rlChecking = findViewById(R.id.rlCheckingAccount);
        rlSavings = findViewById(R.id.rlSavingsAccount);
        rlMortgage = findViewById(R.id.rlMortgageAccount);

        // Checking Account Views
        tvCheckingNumber = findViewById(R.id.tvCheckingNumber);
        tvCheckingBalance = findViewById(R.id.tvCheckingBalance);

        // Savings Account Views
        tvSavingsNumber = findViewById(R.id.tvSavingsNumber);
        tvSavingsBalance = findViewById(R.id.tvSavingsBalance);
        tvInterestRate = findViewById(R.id.tvInterestRate);
        tvMonthlyProfit = findViewById(R.id.tvMonthlyProfit);

        // Mortgage Account Views
        tvMortgageNumber = findViewById(R.id.tvMortgageNumber);
        tvPaymentAmount = findViewById(R.id.tvPaymentAmount);
        tvPaymentFrequency = findViewById(R.id.tvPaymentFrequency);
    }

    private void setupData() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        // --- Checking Account ---
        tvCheckingNumber.setText("**** 1234");
        tvCheckingBalance.setText(currencyFormat.format(5450.00));

        // --- Savings Account ---
        tvSavingsNumber.setText("**** 5678");
        tvSavingsBalance.setText(currencyFormat.format(12000.00));
        tvInterestRate.setText("4.5% APY"); // Lãi suất
        tvMonthlyProfit.setText("+ " + currencyFormat.format(45.00)); // Lợi nhuận tháng

        tvMortgageNumber.setText("Loan #9988-77");

        boolean payMonthly = true;

        if (payMonthly) {
            tvPaymentAmount.setText(currencyFormat.format(1250.00));
            tvPaymentFrequency.setText("Monthly");
        } else {
            tvPaymentAmount.setText(currencyFormat.format(625.00));
            tvPaymentFrequency.setText("Bi-weekly");
        }
    }

    private void setupEvents() {
        // Nút Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity này để quay lại màn hình trước
            }
        });

        rlChecking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.item_checking_account, null);

                // 2. Tạo hộp thoại chứa view đó
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SeeAllAccountActivity.this);
                builder.setView(dialogView);
                builder.setCancelable(true);

                // 3. Hiển thị
                builder.show();
            }
        });

        rlSavings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.item_saving_account, null);

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SeeAllAccountActivity.this);
                builder.setView(dialogView);
                builder.setCancelable(true);

                // 3. Hiển thị
                builder.show();
            }
        });

        rlMortgage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Tạo view từ file xml
                View dialogView = getLayoutInflater().inflate(R.layout.item_mortage_account, null);

                // 2. Tạo hộp thoại chứa view đó
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SeeAllAccountActivity.this);
                builder.setView(dialogView);
                builder.setCancelable(true);

                // 3. Hiển thị
                builder.show();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}