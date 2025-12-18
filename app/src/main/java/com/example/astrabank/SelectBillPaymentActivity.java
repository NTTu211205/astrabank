package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SelectBillPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_bill_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupServiceClick(R.id.tv_electric, "Electricity");
        setupServiceClick(R.id.tv_water, "Water");
        setupServiceClick(R.id.tv_item_internet, "Internet");
        setupServiceClick(R.id.tv_mobile_top_up, "Mobile Top-up");
        setupServiceClick(R.id.tv_data, "Mobile Data");
//        setupServiceClick(R.id.tv_bus_ticket, "Bus Tickets");
//        setupServiceClick(R.id.tv_tuition, "Tuition");
//        setupServiceClick(R.id.tv_airline_tickets, "Airline Tickets");

        findViewById(R.id.img_back).setOnClickListener(v -> finish());
    }

    private void setupServiceClick(int viewId, String serviceName) {
        TextView serviceView = findViewById(viewId);
        if (serviceView != null) {
            serviceView.setOnClickListener(v -> {
                Intent intent = new Intent(SelectBillPaymentActivity.this, BillListingActivity.class);
                intent.putExtra("SERVICE_NAME", serviceName);
                startActivity(intent);
            });
        }
    }
}