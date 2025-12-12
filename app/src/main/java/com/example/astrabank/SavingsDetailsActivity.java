package com.example.astrabank;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.TESTACCOUNT.Transaction;
import com.example.astrabank.TESTACCOUNT.TransactionAdapter;

import java.util.ArrayList;
import java.util.List;

public class SavingsDetailsActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList;
    ImageButton btnBack;
    // Header Views
    private TextView tvTotalBalance;
    private TextView tvInterestRate;
    private TextView tvLastProfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_saving_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Initialize Views
        initViews();

        // 2. Set Header Data (Mock data based on your image)
        setupHeaderData();

        // 3. Setup RecyclerView for History
        setupRecyclerView();
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        rvHistory = findViewById(R.id.rv_transaction_history);
        tvTotalBalance = findViewById(R.id.tv_total_balance);
        tvInterestRate = findViewById(R.id.tv_interest_rate);
        tvLastProfit = findViewById(R.id.tv_last_profit);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupHeaderData() {
        // In a real app, you would pass these via Intent or fetch from API
        tvTotalBalance.setText("$ 12,000.00");
        tvInterestRate.setText("4.5% APY");
        tvLastProfit.setText("+ $ 45.00");
    }

    private void setupRecyclerView() {
        transactionList = new ArrayList<>();

        // Mock Data: Deposit History
        transactionList.add(new Transaction("Monthly Deposit", "Dec 01, 2025", 1000.00));
        transactionList.add(new Transaction("Interest Payment", "Nov 30, 2025", 45.00));
        transactionList.add(new Transaction("Mobile Transfer", "Nov 15, 2025", 500.00));
        transactionList.add(new Transaction("Monthly Deposit", "Nov 01, 2025", 1000.00));
        transactionList.add(new Transaction("Cash Deposit", "Oct 20, 2025", 200.00));
        transactionList.add(new Transaction("Interest Payment", "Oct 31, 2025", 42.50));

        adapter = new TransactionAdapter(transactionList);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(adapter);
    }
}