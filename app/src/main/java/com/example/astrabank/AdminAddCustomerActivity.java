package com.example.astrabank;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AdminAddCustomerActivity extends AppCompatActivity {

    private TextInputEditText etName, etDob, etCccd, etPhone, etEmail, etAddress, etDeposit;
    private AutoCompleteTextView spStatus;
    private Button btnCancel, btnCreate;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_customer);

        initViews();
        setupDropdown();
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);

        etName = findViewById(R.id.et_kyc_name);
        etDob = findViewById(R.id.et_dob);
        etCccd = findViewById(R.id.et_kyc_cccd);
        etPhone = findViewById(R.id.et_kyc_phone);
        etEmail = findViewById(R.id.et_kyc_email);
        etAddress = findViewById(R.id.et_kyc_address);
        etDeposit = findViewById(R.id.et_kyc_deposit);

        spStatus = findViewById(R.id.sp_kyc_status);

        btnCancel = findViewById(R.id.btn_cancel_kyc);
        btnCreate = findViewById(R.id.btn_create_kyc);
    }

    private void setupDropdown() {
        // Cài đặt danh sách cho Dropdown Status
        String[] statusOptions = {"Active", "Locked", "Pending"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statusOptions);
        spStatus.setAdapter(adapter);
    }

    private void setupEvents() {
        // Nút Back & Cancel giống nhau: Đóng màn hình
        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        // Nút Create Account
        btnCreate.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String phone = etPhone.getText().toString();
            String dob = etDob.getText().toString();
            String depositStr = etDeposit.getText().toString();
            String status = spStatus.getText().toString();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Name and Phone are required!", Toast.LENGTH_SHORT).show();
                return;
            }

        });
    }
}