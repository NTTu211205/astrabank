package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DiscoverProductsActivity extends AppCompatActivity {
    ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_discover_products);

        btn_back = findViewById(R.id.btn_back);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupNavigation(R.id.card_sinh_loi, AutoEarningActivity.class);
        setupNavigation(R.id.card_benefit, IncentiveActivity.class);
        setupNavigation(R.id.card_chi_tieu, SpendingActivity.class);
        setupNavigation(R.id.card_saving, SavingActivity.class);
        setupNavigation(R.id.card_invest, InvestmentActivity.class);
        setupNavigation(R.id.card_insurance, InsuranceActivity.class);
    }
    private void setupNavigation(int cardId, Class<?> destinationActivity) {
        View card = findViewById(cardId);
        if (card != null) {
            card.setOnClickListener(v -> {
                Intent intent = new Intent(DiscoverProductsActivity.this, destinationActivity);
                startActivity(intent);
            });
        }
    }
}