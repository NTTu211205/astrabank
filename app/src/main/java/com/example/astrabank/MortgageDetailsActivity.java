package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import com.example.astrabank.adapters.PaymentAdapter;
import com.example.astrabank.models.PaymentRecord;

import java.util.ArrayList;
import java.util.List;

public class MortgageDetailsActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private PaymentAdapter adapter;
    private List<PaymentRecord> paymentList;
    ImageButton btnBack;

    // Header Views
    private TextView tvDueAmount, tvDueDate;
    private TextView tvPropertyAddress, tvOutstandingBalance;
    private Button btnMakePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mortgage_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        setupData(); // Set collateral & loan info
        setupRecyclerView(); // Set history
        setupActions(); // Handle button click
    }

    private void initViews() {
        rvHistory = findViewById(R.id.rv_payment_history);
        tvDueAmount = findViewById(R.id.tv_due_amount);
        tvDueDate = findViewById(R.id.tv_due_date);
        tvPropertyAddress = findViewById(R.id.tv_property_address);
        tvOutstandingBalance = findViewById(R.id.tv_outstanding_balance);
        btnMakePayment = findViewById(R.id.btn_make_payment);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupData() {
        // Mock Data based on typical mortgage scenarios
        tvDueAmount.setText("$ 1,250.00");
        tvDueDate.setText("Due on Dec 25, 2025");

        // Collateral Info: Usually the address of the house
        tvPropertyAddress.setText("123 Maple Ave, New York");

        // Total remaining debt
        tvOutstandingBalance.setText("$ 185,400.00");
    }

    private void setupActions() {
        btnMakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MortgageDetailsActivity.this, MortgagePaymentActivity.class);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        paymentList = new ArrayList<>();

        // Mock Data: Payment History (Past payments)
        paymentList.add(new PaymentRecord("Monthly Installment", "Nov 25, 2025", 1250.00));
        paymentList.add(new PaymentRecord("Monthly Installment", "Oct 25, 2025", 1250.00));
        paymentList.add(new PaymentRecord("Monthly Installment", "Sep 25, 2025", 1250.00));
        paymentList.add(new PaymentRecord("Extra Principal Payment", "Aug 10, 2025", 5000.00));
        paymentList.add(new PaymentRecord("Monthly Installment", "Aug 25, 2025", 1250.00));

        adapter = new PaymentAdapter(paymentList);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(adapter);
    }
}