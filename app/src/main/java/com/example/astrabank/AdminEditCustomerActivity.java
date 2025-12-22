package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.astrabank.adapters.AdminCustomerAdapter;
import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.request.UpdateUserRequest;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.User;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminEditCustomerActivity extends AppCompatActivity {
    private final String LOG_TAG = "AdminEditCustomerActivity";

    private TextInputEditText etName, etDob, etCccd, etPhone, etEmail, etAddress, etOccupation, etAverageSalary, etCompanyName;
    private Button btnCancel, btnCreate;
    private ImageButton btnBack;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        uid = intent.getStringExtra("userId");


        initViews();
        callApiFindUser(uid);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(AdminCustomerActivity.class);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(AdminCustomerActivity.class);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserRequest request = new UpdateUserRequest();

                request.setFullName(etName.getText().toString().trim());
                request.setDateOfBirth(etDob.getText().toString().trim());
                request.setNationalID(etCccd.getText().toString().trim());
                request.setEmail(etEmail.getText().toString().trim());
                request.setPhone(etPhone.getText().toString().trim());
                request.setAddress(etAddress.getText().toString().trim());
                request.setCompanyName(etCompanyName.getText().toString().trim());
                request.setOccupation(etOccupation.getText().toString().trim());

                String salaryStr = etAverageSalary.getText().toString().trim();

                String cleanSalary = salaryStr.replaceAll("[,.]", "");

                try {
                    if (!cleanSalary.isEmpty()) {
                        request.setAverageSalary(Double.parseDouble(cleanSalary));
                    } else {
                        request.setAverageSalary(0.0);
                    }
                } catch (NumberFormatException e) {
                    request.setAverageSalary(0.0);
                    etAverageSalary.setError("Lương không hợp lệ");
                    return;
                }

                request.setUpdatedBy("ADMIN_CURRENT_LOGIN"); // Thay bằng biến session thực tế của bạn

                callApiEdit(uid, request);
            }
        });
    }

    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        startActivity(intent);
        finish();
    }

    private void callApiEdit(String uid, UpdateUserRequest request) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<User>> call = apiService.updateProfile(uid, request);

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<User> apiResponse = response.body();

                    if (apiResponse != null && apiResponse.getResult() != null) {
                        User user = apiResponse.getResult();

                        changeScreen(AdminCustomerActivity.class);
                    }
                    else {
                        Toast.makeText(AdminEditCustomerActivity.this, "process update error, try again", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "Process update error");
                    }
                }
                else {
                    Toast.makeText(AdminEditCustomerActivity.this, "Update profile error", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Update error");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AdminEditCustomerActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callApiFindUser(String uid) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<User>> call = apiService.findUserById(uid);

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<User> apiResponse = response.body();

                    if (apiResponse != null) {
                        User user = apiResponse.getResult();

                        if (user != null) {
                            etName.setText(user.getFullName());
                            etDob.setText(user.getDateOfBirth());
                            etCccd.setText(user.getNationalID());

                            etPhone.setText(user.getPhone());
                            etEmail.setText(user.getEmail());
                            etAddress.setText(user.getAddress());

                            etCompanyName.setText(user.getCompanyName());
                            etAverageSalary.setText(formetNumber(user.getAverageSalary()));
                            etOccupation.setText(user.getOccupation());
                        }
                        else {
                            changeScreen(AdminCustomerActivity.class);
                            Log.d(LOG_TAG, "User not found");
                            Toast.makeText(AdminEditCustomerActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        changeScreen(AdminCustomerActivity.class);
                        Log.d(LOG_TAG, "ApiResponse is null");
                        Toast.makeText(AdminEditCustomerActivity.this, "Error from server, try again", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    changeScreen(AdminCustomerActivity.class);
                    Log.d(LOG_TAG, "User not found");
                    Toast.makeText(AdminEditCustomerActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                changeScreen(AdminCustomerActivity.class);
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AdminEditCustomerActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formetNumber(Double number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);

        etName = findViewById(R.id.et_kyc_name);
        etDob = findViewById(R.id.et_dob);
        etCccd = findViewById(R.id.et_kyc_cccd);

        etPhone = findViewById(R.id.et_kyc_phone);
        etEmail = findViewById(R.id.et_kyc_email);
        etAddress = findViewById(R.id.et_kyc_address);

        etCompanyName = findViewById(R.id.etCompanyName);
        etOccupation = findViewById(R.id.etOccupation);
        etAverageSalary = findViewById(R.id.etAverageSalary);


        btnCancel = findViewById(R.id.btn_cancel_kyc);
        btnCreate = findViewById(R.id.btn_create_kyc);
    }
}