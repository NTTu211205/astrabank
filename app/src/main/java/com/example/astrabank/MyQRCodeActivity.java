package com.example.astrabank;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide; // Import thư viện Glide
import com.example.astrabank.models.Account;
import com.example.astrabank.utils.LoginManager;

public class MyQRCodeActivity extends AppCompatActivity {

    private ImageView ivQrCode;
    private TextView tvName, tvAccountNum;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_code);

        initViews();
        generateVietQRCode();
    }

    private void initViews() {
        ivQrCode = findViewById(R.id.iv_qr_code);
        tvName = findViewById(R.id.tv_account_name);
        tvAccountNum = findViewById(R.id.tv_account_number);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());
    }

    private void generateVietQRCode() {

        Account currentAccount = LoginManager.getInstance().getAccount();

        if (currentAccount == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }

        String accountNumber = currentAccount.getAccountNumber();
        String accountName = LoginManager.getInstance().getUser().getFullName();

        tvName.setText(accountName);
        tvAccountNum.setText(accountNumber);

        String bankId = "970407";

        String template = "compact2";
        String qrUrl = "https://img.vietqr.io/image/" + bankId + "-" + accountNumber + "-" + template + ".png";

        Glide.with(this)
                .load(qrUrl)
                .placeholder(android.R.drawable.stat_sys_download)
                .error(android.R.drawable.stat_notify_error)
                .into(ivQrCode);
    }
}