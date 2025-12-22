package com.example.astrabank;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.request.AdminCreateCustomerRequest;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.User;
import com.example.astrabank.utils.LoginManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddCustomerActivity extends AppCompatActivity {
    private final String LOG_TAG = "AdminAddCustomerActivity";

    private TextInputEditText etTransactionPIN, etName, etDob, etCccd, etPhone, etEmail, etAddress, etDeposit, etOccupation, etAverageSalary, etCompanyName;
    private AutoCompleteTextView spStatus;
    private Button btnCancel, btnCreate;
    private ImageButton btnBack;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_customer);

        Intent intent = getIntent();
        uid = intent.getStringExtra("message");
        if (uid == null ) {
            changeScreen(AdminCustomerActivity.class);
        }

        initViews();
//        setupDropdown();
        setupEvents();

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        // Lấy ngày tháng hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // selectedMonth bắt đầu từ 0 (0 = tháng 1), nên ta cộng 1
                        String dateString = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;

                        // Hiển thị ngày đã chọn lên TextView
                        etDob.setText(dateString);
                    }
                },
                year, month, day);

        // Hiển thị dialog
        datePickerDialog.show();
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

//        spStatus = findViewById(R.id.sp_kyc_status);
        etCompanyName = findViewById(R.id.etCompanyName);
        etOccupation = findViewById(R.id.etOccupation);
        etAverageSalary = findViewById(R.id.etAverageSalary);

        etTransactionPIN = findViewById(R.id.etTransactionPIN);

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
            btnCreate.setText("●   ●   ●");
            btnCreate.setEnabled(false);
            String name = etName.getText().toString();
            String nationalId = etCccd.getText().toString();
            String dob = etDob.getText().toString();

            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();
            String address = etAddress.getText().toString();

            long deposit = Long.parseLong(etDeposit.getText().toString());

            String occupation = etOccupation.getText().toString();
            Double averageSalary = Double.parseDouble(etAverageSalary.getText().toString());
            String companyName = etCompanyName.getText().toString();

            String pin = etTransactionPIN.getText().toString();

            AdminCreateCustomerRequest request = new AdminCreateCustomerRequest(
                    uid,
                    name,
                    dob,
                    nationalId,
                    email,
                    phone,
                    address,
                    occupation,
                    companyName,
                    averageSalary,
                    pin,
                    LoginManager.getInstance().getUser().getUserID(),
                    LoginManager.getInstance().getUser().getUserID(),
                    deposit
            );

            callApiCreateCustomer(request);
        });
    }

    private void callApiCreateCustomer(AdminCreateCustomerRequest request) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<User>> call = apiService.createForAdmin(request);

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<User> apiResponse = response.body();

                    if(apiResponse != null) {
                        User user = apiResponse.getResult();

                        if (user != null) {
                            btnCreate.setEnabled(true);
                            btnCreate.setText("Create Account");
                            Toast.makeText(AdminAddCustomerActivity.this, "Create user success", Toast.LENGTH_SHORT).show();
                            changeScreen(AdminCustomerActivity.class);
                        }
                        else {
                            Log.d(LOG_TAG, "User is null");
                            Toast.makeText(AdminAddCustomerActivity.this, "Error from server, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        btnCreate.setEnabled(true);
                        btnCreate.setText("Create Account");
                        Log.d(LOG_TAG, "ApiResponse is null");
                        Toast.makeText(AdminAddCustomerActivity.this, "Error from server, try again", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    btnCreate.setEnabled(true);
                    btnCreate.setText("Create Account");
                    Log.d(LOG_TAG, "Error from server, response is  null");
                    Toast.makeText(AdminAddCustomerActivity.this, "Error from server, try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                btnCreate.setEnabled(true);
                btnCreate.setText("Create Account");
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AdminAddCustomerActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        startActivity(intent);
        finish();
    }
}