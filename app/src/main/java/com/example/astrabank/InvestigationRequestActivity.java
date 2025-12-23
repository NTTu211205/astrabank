package com.example.astrabank;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.astrabank.utils.LoginManager;
import com.google.android.material.textfield.TextInputEditText;

public class InvestigationRequestActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private AutoCompleteTextView actvReason;
    private TextInputEditText etTransRef, etDescription, etEmail, etPhone;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_investigation_request);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupReasonDropdown();
        fillUserData();
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        actvReason = findViewById(R.id.actv_reason);
        etTransRef = findViewById(R.id.et_trans_ref);
        etDescription = findViewById(R.id.et_description);
        etEmail = findViewById(R.id.et_contact_email);
        etPhone = findViewById(R.id.et_contact_phone);
        btnSubmit = findViewById(R.id.btn_submit_request);
    }

    private void setupReasonDropdown() {
        String[] reasons = new String[]{
                "Transfer to wrong account",
                "Duplicate transaction",
                "Amount discrepancy",
                "Beneficiary not received funds",
                "Fraudulent transaction",
                "System error",
                "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, reasons);
        actvReason.setAdapter(adapter);


    }

    private void fillUserData() {

        if (LoginManager.getInstance().getUser() != null) {
            etPhone.setText(LoginManager.getInstance().getUser().getPhone());
            etEmail.setText("customer@astrabank.com"); // Demo
        }
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            String refNo = etTransRef.getText().toString().trim();
            String reason = actvReason.getText().toString();
            String desc = etDescription.getText().toString();

            if (refNo.isEmpty()) {
                etTransRef.setError("Transaction Reference is required");
                return;
            }

            if (reason.isEmpty()) {
                actvReason.setError("Please select a reason");
                return;
            }
            Toast.makeText(this, "Request submitted successfully! We will contact you shortly.", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}