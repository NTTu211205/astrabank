package com.example.astrabank;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.request.UpdateUserRequest;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.User;
import com.example.astrabank.utils.LoginManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPersonalInformationActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextInputEditText etFullName, etDob, etCccd, etEmail, etPhoneNumber, etAddress, etCompanyName;
    private AutoCompleteTextView actvOccupation, actvIncome;
    private Button btnSubmit;
    private final String LOG_TAG = "EditPersonalInformationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_personal_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        setupDataAndListeners();
        fillUserData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnSubmit = findViewById(R.id.btn_submit);

        etFullName = findViewById(R.id.et_full_name);
        etDob = findViewById(R.id.et_dob);
        etCccd = findViewById(R.id.et_cccd);
        etEmail = findViewById(R.id.et_email);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etAddress = findViewById(R.id.et_address);
        etCompanyName = findViewById(R.id.et_company_name);

        actvOccupation = findViewById(R.id.actv_occupation);
        actvIncome = findViewById(R.id.actv_income);
    }

    private void setupDataAndListeners() {
        btnBack.setOnClickListener(v -> finish());

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // --- Xử lý nút Submit ---
        btnSubmit.setOnClickListener(v -> {
            submitForm();
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format ngày tháng: dd/MM/yyyy
                    String dateString = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etDob.setText(dateString);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void fillUserData() {
        User user = LoginManager.getInstance().getUser();

        if (user != null) {
            // Các trường String cơ bản
            etFullName.setText(user.getFullName());
            etDob.setText(user.getDateOfBirth());
            etCccd.setText(user.getNationalID());
            etEmail.setText(user.getEmail());
            etPhoneNumber.setText(user.getPhone());
            etAddress.setText(user.getAddress());
            etCompanyName.setText(user.getCompanyName());

            if (user.getOccupation() != null) {
                actvOccupation.setText(user.getOccupation(), false);
            }

            if (user.getAverageSalary() != null) {
                String salaryStr = String.valueOf(user.getAverageSalary());
                actvIncome.setText(salaryStr, false);
            }
        }
    }

    private void submitForm() {
        String fullName = etFullName.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String cccd = etCccd.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String company = etCompanyName.getText().toString().trim();
        String occupation = actvOccupation.getText().toString().trim();
        String incomeString = actvIncome.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Vui lòng nhập họ và tên");
            etFullName.requestFocus();
            return;
        }

        // Kiểm tra Ngày sinh
        if (TextUtils.isEmpty(dob)) {
            etDob.setError("Vui lòng chọn ngày sinh");
            return;
        }

        // Kiểm tra CCCD (Phải đủ 12 số)
        if (TextUtils.isEmpty(cccd) || cccd.length() != 12) {
            etCccd.setError("CCCD/CMND phải bao gồm đúng 12 chữ số");
            etCccd.requestFocus();
            return;
        }

        // Kiểm tra Email (Phải đúng định dạng email@domain.com)
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone) || !phone.matches("0\\d{9}")) {
            etPhoneNumber.setError("Số điện thoại không hợp lệ (cần 10 số)");
            etPhoneNumber.requestFocus();
            return;
        }

        // Kiểm tra Địa chỉ
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Vui lòng nhập địa chỉ liên hệ");
            etAddress.requestFocus();
            return;
        }

        // Kiểm tra Nghề nghiệp (Bắt buộc chọn)
        if (TextUtils.isEmpty(occupation)) {
            actvOccupation.setError("Vui lòng chọn nghề nghiệp");
            actvOccupation.requestFocus();
            return;
        }

        UpdateUserRequest updateUserRequest = new UpdateUserRequest(
                fullName,
                dob,
                cccd,
                email,
                phone,
                address,
                occupation,
                company,
                Double.parseDouble(incomeString)
        );

        callApiUpdate(updateUserRequest);
    }

    private void callApiUpdate(UpdateUserRequest updateUserRequest) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<User>> call = apiService.updateProfile(LoginManager.getInstance().getUser().getUserID(), updateUserRequest);

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<User> apiResponse = response.body();

                    if (apiResponse != null) {
                        User user = apiResponse.getResult();

                        if (user != null) {
                            LoginManager.getInstance().setUser(user);
                            Toast.makeText(EditPersonalInformationActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                            changeScreen(PersonalInformationActivity.class);
                        }
                        else {
                            Toast.makeText(EditPersonalInformationActivity.this, "Update not successful", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "Update not successful");
                        }
                    }
                    else {
                        Toast.makeText(EditPersonalInformationActivity.this, "process update error, try again", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "Process update error");
                    }
                }
                else {
                    Toast.makeText(EditPersonalInformationActivity.this, "Update profile error", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Update error");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                Toast.makeText(EditPersonalInformationActivity.this, "Internet díconnected", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Internet disconnected");
            }
        });
    }

    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        startActivity(intent);
        finish();
    }
}