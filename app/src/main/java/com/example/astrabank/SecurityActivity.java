package com.example.astrabank;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class SecurityActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextInputEditText etCurrentPin, etNewPin, etConfirmPin;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_security);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        etCurrentPin = findViewById(R.id.et_current_pin);
        etNewPin = findViewById(R.id.et_new_pin);
        etConfirmPin = findViewById(R.id.et_confirm_pin);
        btnUpdate = findViewById(R.id.btn_update_pin);
    }

    private void setupEvents() {
        // Nút Back
        btnBack.setOnClickListener(v -> finish());

        // Nút Update PIN
        btnUpdate.setOnClickListener(v -> handleUpdatePin());
    }

    private void handleUpdatePin() {
        String currentPin = etCurrentPin.getText().toString();
        String newPin = etNewPin.getText().toString();
        String confirmPin = etConfirmPin.getText().toString();

        // 1. Validate Current PIN (Giả lập)
        // Trong thực tế, bạn cần so sánh với PIN đã lưu trong SharedPreferences hoặc Server
        if (currentPin.isEmpty() || currentPin.length() < 6) {
            etCurrentPin.setError("Please enter valid current PIN");
            return;
        }

        // 2. Validate New PIN
        if (newPin.length() != 6) {
            etNewPin.setError("PIN must be exactly 6 digits");
            return;
        }

        if (newPin.equals(currentPin)) {
            etNewPin.setError("New PIN must be different from current PIN");
            return;
        }

        // 3. Validate Confirm PIN
        if (!newPin.equals(confirmPin)) {
            etConfirmPin.setError("PIN confirmation does not match");
            return;
        }

        // TODO: Gọi API hoặc lưu PIN mới vào SharedPreferences/Firebase tại đây
        // saveNewPinToStorage(newPin);

        Toast.makeText(this, "PIN updated successfully!", Toast.LENGTH_SHORT).show();
        finish(); // Đóng màn hình sau khi đổi thành công
    }
}