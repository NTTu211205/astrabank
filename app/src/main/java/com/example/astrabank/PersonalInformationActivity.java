package com.example.astrabank;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.astrabank.models.User; // Giả định bạn có model User
import com.example.astrabank.utils.LoginManager; // Giả định dùng LoginManager

public class PersonalInformationActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvFullName, tvDob, tvCccd;
    private TextView tvEmail, tvPhone, tvAddress;
    private TextView tvOccupation, tvCompany, tvIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        loadUserData();
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);

        tvFullName = findViewById(R.id.tv_full_name);
        tvDob = findViewById(R.id.tv_dob);
        tvCccd = findViewById(R.id.tv_cccd);

        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);

        tvOccupation = findViewById(R.id.tv_occupation);
        tvCompany = findViewById(R.id.tv_company);
        tvIncome = findViewById(R.id.tv_income);
    }

    private void loadUserData() {
        User user = LoginManager.getInstance().getUser();

        if (user != null) {
            tvFullName.setText(user.getFullName());
            tvPhone.setText(user.getPhone());

            tvDob.setText(user.getDateOfBirth());
            tvCccd.setText(user.getNationalID());
            tvEmail.setText(user.getEmail());
            tvAddress.setText(user.getAddress());
            tvOccupation.setText(user.getOccupation());
            tvCompany.setText(user.getCompanyName());
            tvIncome.setText(user.getAverageSalary() + "");

        }
    }

    private void setupEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}