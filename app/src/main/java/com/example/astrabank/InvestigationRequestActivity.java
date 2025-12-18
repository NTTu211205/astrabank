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

        // Xử lý Edge-to-Edge
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
        // Danh sách các lý do tra soát phổ biến
        String[] reasons = new String[]{
                "Transfer to wrong account",       // Chuyển nhầm tài khoản
                "Duplicate transaction",           // Giao dịch bị trùng lặp
                "Amount discrepancy",              // Sai lệch số tiền
                "Beneficiary not received funds",  // Người thụ hưởng chưa nhận được tiền
                "Fraudulent transaction",          // Giao dịch gian lận/lừa đảo
                "System error",                    // Lỗi hệ thống
                "Other"                            // Khác
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, reasons);
        actvReason.setAdapter(adapter);

        // Mặc định chọn cái đầu tiên nếu muốn, hoặc để trống
        // actvReason.setText(reasons[0], false);
    }

    private void fillUserData() {

        if (LoginManager.getInstance().getUser() != null) {
            etPhone.setText(LoginManager.getInstance().getUser().getPhone());
            etEmail.setText("customer@astrabank.com"); // Demo
        }
    }

    private void setupEvents() {
        // Nút Back
        btnBack.setOnClickListener(v -> finish());

        // Nút Submit
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

            // TODO: Gửi dữ liệu lên Server/Firebase tại đây
            // ...

            // Thông báo thành công và đóng màn hình
            Toast.makeText(this, "Request submitted successfully! We will contact you shortly.", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}