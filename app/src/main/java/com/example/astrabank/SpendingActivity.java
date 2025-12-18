package com.example.astrabank;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SpendingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_showcase); // Sử dụng layout chung
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Custom nội dung cho Spending
        ((TextView) findViewById(R.id.tv_page_title)).setText("Spending Products");
        ((TextView) findViewById(R.id.tv_product_name)).setText("Astra Platinum Card");
        ((Button) findViewById(R.id.btn_action)).setOnClickListener(v -> {
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}
