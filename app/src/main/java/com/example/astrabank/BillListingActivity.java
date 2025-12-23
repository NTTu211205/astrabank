package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.adapters.BillHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class BillListingActivity extends AppCompatActivity {

    private String serviceName = "Service";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_listing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent().hasExtra("SERVICE_NAME")) {
            serviceName = getIntent().getStringExtra("SERVICE_NAME");
        }

        TextView tvScreenTitle = findViewById(R.id.tvScreenTitle);
        tvScreenTitle.setText(serviceName + " Bill");

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnMakePayment = findViewById(R.id.btn_make_payment);
        btnMakePayment.setOnClickListener(v -> {
            Intent intent = new Intent(BillListingActivity.this, BillPaymentsActivity.class);
            intent.putExtra("SERVICE_NAME", serviceName);
            TextView tvDueAmount = findViewById(R.id.tv_due_amount);
            String amountString = tvDueAmount.getText().toString().replace("$", "").replace(",", "").trim();
            intent.putExtra("AMOUNT", amountString);
            startActivity(intent);
        });

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView rvHistory = findViewById(R.id.rv_payment_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        // Tạo dữ liệu giả lập dựa trên tên dịch vụ
        List<BillHistoryAdapter.BillHistoryItem> historyList = new ArrayList<>();

        // Giả lập 3 tháng gần nhất
        historyList.add(new BillHistoryAdapter.BillHistoryItem("Payment for " + serviceName + " Nov", "Nov 25, 2025", "- $1,250.00"));
        historyList.add(new BillHistoryAdapter.BillHistoryItem("Payment for " + serviceName + " Oct", "Oct 25, 2025", "- $1,200.00"));
        historyList.add(new BillHistoryAdapter.BillHistoryItem("Payment for " + serviceName + " Sep", "Sep 25, 2025", "- $1,150.00"));

        BillHistoryAdapter adapter = new BillHistoryAdapter(historyList);
        rvHistory.setAdapter(adapter);
    }
}