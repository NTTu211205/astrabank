package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TransactionActivity extends AppCompatActivity {
    Button btnMakePayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnMakePayment = findViewById(R.id.btn_confirm_payment);
        btnMakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMakePayment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TransactionActivity.this, TransactionSuccessActivity.class);

                        intent.putExtra("IS_BILL_PAYMENT", false);

                        intent.putExtra("AMOUNT", "500,000"); // Lấy từ et_amount.getText().toString()
                        intent.putExtra("CONTENT", "Nguyen Van A chuyen tien"); // Lấy từ et_content
                        intent.putExtra("TRANSACTION_CODE", "FT123456789");
                        intent.putExtra("DATE_TIME", "17 Dec, 2025 14:30");

                        intent.putExtra("RECEIVER_NAME", "TRAN CAO PHONG");
                        intent.putExtra("RECEIVER_ACC", "86686886688668");
                        intent.putExtra("BANK_NAME", "Techcombank");

                        startActivity(intent);
                    }
                });
            }
        });
    }
}