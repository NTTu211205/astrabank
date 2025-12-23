package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private RelativeLayout rlPersonalInfo, rlSecurity, rlInvestigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupActions();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        rlPersonalInfo = findViewById(R.id.rl_personal_info);
        rlSecurity = findViewById(R.id.rl_security);
        rlInvestigation = findViewById(R.id.rl_investigation);
    }

    private void setupActions() {
        btnBack.setOnClickListener(v -> finish());

        rlPersonalInfo.setOnClickListener(v -> {
            Intent intent = new Intent(this, PersonalInformationActivity.class);
            startActivity(intent);

        });

        rlSecurity.setOnClickListener(v -> {
            Intent intent = new Intent(this, SecurityActivity.class);
            startActivity(intent);
        });

        rlInvestigation.setOnClickListener(v -> {
            Intent intent = new Intent(this, InvestigationRequestActivity.class);
            startActivity(intent);
        });
    }
}