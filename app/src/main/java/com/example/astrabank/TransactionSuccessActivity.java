package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TransactionSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_transfer_success), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các View chung
        TextView tvAmount = findViewById(R.id.tv_amount);
        TextView tvMessageContent = findViewById(R.id.tv_message_content);
        TextView tvTransactionIdContent = findViewById(R.id.tv_transaction_id_content);
        TextView tvDateContent = findViewById(R.id.tv_date_content);
        TextView tvTitleAmount = findViewById(R.id.tv_title_amount);
        Button btnHoanThanh = findViewById(R.id.btn_hoan_thanh);

        // Ánh xạ các View thông tin người nhận (sẽ ẩn/hiện)
        TextView tvReceiverLabel = findViewById(R.id.tv_receiver_label);
        TextView tvReceiverName = findViewById(R.id.tv_receiver_name);
        TextView tvReceiverAccount = findViewById(R.id.tv_receiver_account);
        TextView tvBankLabel = findViewById(R.id.tv_bank_label);
        TextView tvBankName = findViewById(R.id.tv_bank_name);

        // Lấy Intent
        Intent intent = getIntent();

        boolean isBillPayment = intent.getBooleanExtra("IS_BILL_PAYMENT", false);

        String amount = intent.getStringExtra("AMOUNT");
        String content = intent.getStringExtra("CONTENT");
        String transactionCode = intent.getStringExtra("TRANSACTION_CODE");
        String dateTime = intent.getStringExtra("DATE_TIME");

        if (amount != null) tvAmount.setText(amount + " VND");
        if (content != null) tvMessageContent.setText(content);
        if (transactionCode != null) tvTransactionIdContent.setText(transactionCode);
        if (dateTime != null) tvDateContent.setText(dateTime);

        if (isBillPayment) {
            // === TRƯỜNG HỢP THANH TOÁN HÓA ĐƠN ===
            tvTitleAmount.setText("Thanh toán thành công");

            // ẨN thông tin người nhận
            tvReceiverLabel.setVisibility(View.GONE);
            tvReceiverName.setVisibility(View.GONE);
            tvReceiverAccount.setVisibility(View.GONE);
            tvBankLabel.setVisibility(View.GONE);
            tvBankName.setVisibility(View.GONE);

        } else {
            // === TRƯỜNG HỢP CHUYỂN TIỀN THƯỜNG ===
            tvTitleAmount.setText("Chuyển thành công");

            // HIỆN thông tin người nhận
            tvReceiverLabel.setVisibility(View.VISIBLE);
            tvReceiverName.setVisibility(View.VISIBLE);
            tvReceiverAccount.setVisibility(View.VISIBLE);
            tvBankLabel.setVisibility(View.VISIBLE);
            tvBankName.setVisibility(View.VISIBLE);

            // Điền thông tin người nhận (Lấy từ Intent chuyển tiền thường)
            String receiverName = intent.getStringExtra("RECEIVER_NAME");
            String receiverAcc = intent.getStringExtra("RECEIVER_ACC");
            String bankName = intent.getStringExtra("BANK_NAME");

            if(receiverName != null) tvReceiverName.setText(receiverName);
            if(receiverAcc != null) tvReceiverAccount.setText(receiverAcc);
            if(bankName != null) tvBankName.setText(bankName);
        }

        btnHoanThanh.setOnClickListener(v -> {
            finish();
        });
    }
}