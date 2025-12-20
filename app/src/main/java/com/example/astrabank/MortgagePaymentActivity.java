package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MortgagePaymentActivity extends AppCompatActivity {

    private ImageView ivBack;
    private EditText etAmount;
    private EditText etContent;
    private Button btnConfirm;

    private String receiptId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mortgage_payment);

        Intent intent = getIntent();
        receiptId = intent.getStringExtra("receiptId");

        initViews();
        setupDefaultData();
        setupActions();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        etAmount = findViewById(R.id.et_amount);
        etContent = findViewById(R.id.et_content);
        btnConfirm = findViewById(R.id.btn_confirm_payment);
    }

    private void setupDefaultData() {
        // 1. Generate Transaction Code (MD + 12 Random Digits)
        String transactionCode = generateTransactionCode();

        // 2. Get Current Date (Month Year)
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.US);
        String currentDate = sdf.format(new Date());

        // 3. Set Default Text
        // Format: "Payment for [Month/Year] Ref: [Code]"
        String defaultContent = "Payment for " + currentDate + " Ref: " + transactionCode;
        etContent.setText(defaultContent);
    }

    private String generateTransactionCode() {
        StringBuilder sb = new StringBuilder("MD");
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10)); // Appends 0-9
        }
        return sb.toString();
    }

    private void setupActions() {
        // Back Button Logic
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close activity
            }
        });

        // Confirm Button Logic
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = etAmount.getText().toString();
                String content = etContent.getText().toString();

                if (amount.isEmpty()) {
                    Toast.makeText(MortgagePaymentActivity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Simulate Payment Success
                Toast.makeText(MortgagePaymentActivity.this,
                        "Payment of VND" + amount + " Successful!\n" + content,
                        Toast.LENGTH_LONG).show();

                finish(); // Go back to previous screen after payment
            }
        });
    }
}