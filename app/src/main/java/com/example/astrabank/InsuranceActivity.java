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

public class InsuranceActivity extends AppCompatActivity {

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

        // Nút Back
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());


        TextView tvPageTitle = findViewById(R.id.tv_page_title);
        tvPageTitle.setText("Insurance");

        ImageView ivBanner = findViewById(R.id.iv_product_banner);
        ivBanner.setImageResource(R.drawable.insurance);

        TextView tvProductName = findViewById(R.id.tv_product_name);
        tvProductName.setText("Life Protect Gold");

        TextView tvProductDesc = findViewById(R.id.tv_product_desc);
        tvProductDesc.setText("Comprehensive protection for you and your family. Covers hospitalization and critical illness.");

        Button btnAction = findViewById(R.id.btn_action);
        btnAction.setText("Get a Quote");
    }
}