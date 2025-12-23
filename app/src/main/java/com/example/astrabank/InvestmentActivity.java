package com.example.astrabank;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InvestmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_showcase);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());


        TextView tvPageTitle = findViewById(R.id.tv_page_title);
        tvPageTitle.setText("Investment");

        ImageView ivBanner = findViewById(R.id.iv_product_banner);
        ivBanner.setImageResource(R.drawable.investment);

        TextView tvProductName = findViewById(R.id.tv_product_name);
        tvProductName.setText("Growth Stock Fund");

        TextView tvProductDesc = findViewById(R.id.tv_product_desc);
        tvProductDesc.setText("Invest in top 30 companies with high growth potential. Managed by professional experts.");

        Button btnAction = findViewById(R.id.btn_action);
        btnAction.setText("Start Investing");
    }
}