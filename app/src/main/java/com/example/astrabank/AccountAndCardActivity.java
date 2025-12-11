package com.example.astrabank; //
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AccountAndCardActivity extends AppCompatActivity {

    // Khai báo các biến View
    private ImageButton btnBack, btnFavorite, btnToggleEye, btnCopyAccount;
    private TextView tvUserName,tvNumCard, tvBalance, tvSetNickname, tvPhoneNumber, tvAccountNumber;
    private Button btnViewMoreHistory;

    // Biến trạng thái để kiểm tra đang ẩn hay hiện số dư
    private boolean isHidden = false;

    // Lưu trữ giá trị gốc để khi hiện lại không bị mất

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_and_card);
        initViews();
        setupEvents();
    }

    private void initViews() {
        // Nút bấm
        btnBack = findViewById(R.id.btnBack);
//        btnFavorite = findViewById(R.id.btnFavorite);
        btnToggleEye = findViewById(R.id.btnToggleEye);
        btnCopyAccount = findViewById(R.id.btnCopyAccount);
        btnViewMoreHistory = findViewById(R.id.btnViewMoreHistory);
        tvNumCard = findViewById(R.id.tv_numcard);

        // Text hiển thị
        tvUserName = findViewById(R.id.tvUserName);
        tvBalance = findViewById(R.id.tvBalance);
        tvSetNickname = findViewById(R.id.tvSetNickname);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvAccountNumber = findViewById(R.id.tvAccountNumber);
    }

    private void setupEvents() {
        // --- Xử lý nút Quay lại ---
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng màn hình hiện tại
                finish();
            }
        });

        // --- Xử lý nút Yêu thích ---
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountAndCardActivity.this, "Đã thêm vào danh sách yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });

        // --- Xử lý nút Mắt (Ẩn/Hiện thông tin) ---
        btnToggleEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleInformationVisibility();
            }
        });

        // --- Xử lý nút Copy số tài khoản ---
        btnCopyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(tvAccountNumber.getText().toString());
            }
        });

        // --- Xử lý nút Xem thêm lịch sử ---
        btnViewMoreHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountAndCardActivity.this, "Chức năng đang phát triển...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm logic để ẩn/hiện số dư và số tài khoản
    private void toggleInformationVisibility() {
        isHidden = !isHidden; // Đảo ngược trạng thái

        if (isHidden) {
            tvNumCard.setText("1234 1234 1234 7613");
            btnToggleEye.setAlpha(0.5f);
        } else {
            tvNumCard.setText(".... 7613");
            btnToggleEye.setAlpha(1.0f);
        }
    }

    private void copyToClipboard(String textToCopy) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Account Number", textToCopy);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Đã sao chép: " + textToCopy, Toast.LENGTH_SHORT).show();
    }
}