package com.example.astrabank;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AutoEarningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auto_earning);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        Button btnUpdate = findViewById(R.id.btn_action);
        EditText etLimit = findViewById(R.id.et_min_balance);
        Switch swEnable = findViewById(R.id.sw_auto_enable);

        btnUpdate.setOnClickListener(v -> {
            String limit = etLimit.getText().toString();
            boolean isEnabled = swEnable.isChecked();

            String status = isEnabled ? "Enabled" : "Disabled";
            Toast.makeText(this, "Auto Earning " + status + " with limit: " + limit, Toast.LENGTH_SHORT).show();

            finish();
        });
    }
}