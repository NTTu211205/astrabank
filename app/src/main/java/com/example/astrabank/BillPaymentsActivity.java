package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class BillPaymentsActivity extends AppCompatActivity {

    private EditText etContent;
    private EditText etAmount;
    private String transactionCode;
    private String generatedContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_payments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sv_content_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etContent = findViewById(R.id.et_content);
        etAmount = findViewById(R.id.et_amount);
        ImageView ivBack = findViewById(R.id.iv_back);
        Button btnConfirm = findViewById(R.id.btn_confirm_payment);

        String serviceName = getIntent().getStringExtra("SERVICE_NAME");
        if (serviceName == null) serviceName = "Service";

        String amount = getIntent().getStringExtra("AMOUNT");
        if (amount != null) {
            etAmount.setText(amount);
        }

        setupAutoContent(serviceName);

        ivBack.setOnClickListener(v -> finish());

        btnConfirm.setOnClickListener(v -> {
            Intent intent = new Intent(BillPaymentsActivity.this, TransactionSuccessActivity.class);

            intent.putExtra("IS_BILL_PAYMENT", true);

            intent.putExtra("AMOUNT", etAmount.getText().toString());
            intent.putExtra("CONTENT", etContent.getText().toString());
            intent.putExtra("TRANSACTION_CODE", transactionCode);

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy 'at' HH:mm", Locale.getDefault());
            intent.putExtra("DATE_TIME", sdf.format(new Date()));

            startActivity(intent);
            finish();
        });
    }

    private void setupAutoContent(String serviceName) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.US);
        String currentDate = sdf.format(new Date());

        char firstLetter = serviceName.charAt(0);
        Random random = new Random();
        int randomNum = 100000 + random.nextInt(900000); // Đảm bảo luôn 6 số
        transactionCode = String.valueOf(firstLetter).toUpperCase() + randomNum;

        generatedContent = "Payment for " + serviceName + " " + currentDate + " Ref: " + transactionCode;

        etContent.setText(generatedContent);
    }
}