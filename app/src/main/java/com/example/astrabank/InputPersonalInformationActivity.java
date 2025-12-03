package com.example.astrabank;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.astrabank.models.User;
import com.example.astrabank.utils.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class InputPersonalInformationActivity extends AppCompatActivity {
    private final String LOG_TAG = "InputPersonalInformationActivity";
     TextInputLayout tlFullName;
     TextInputLayout tlDob;
     TextInputLayout tlNationalId;
     TextInputLayout tlEmail;
     TextInputLayout tlPhone;
     TextInputLayout tlAddress;
     TextInputLayout tlOccupation;
     TextInputLayout tlCompanyName;
     TextInputLayout tlIncome;
    TextInputEditText etName, etDateOfBirth, etNationalID, etEmail, etPhone, etAddress;
    TextInputEditText etCompanyName;
    AutoCompleteTextView etOccupation, etAverageSalary;
    Button btSave;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_personal_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        etName = findViewById(R.id.et_full_name);
        etDateOfBirth = findViewById(R.id.et_dob);
        etNationalID = findViewById(R.id.et_cccd);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone_number);
        etAddress = findViewById(R.id.et_address);
        etOccupation  = findViewById(R.id.actv_occupation);
        etCompanyName = findViewById(R.id.et_company_name);
        etAverageSalary = findViewById(R.id.actv_income);
        btSave = findViewById(R.id.btn_submit);
        tlFullName = findViewById(R.id.til_full_name);
        tlDob = findViewById(R.id.til_dob);
        tlNationalId = findViewById(R.id.til_national_id);
        tlEmail = findViewById(R.id.til_email);
        tlPhone = findViewById(R.id.til_phone);
        tlAddress = findViewById(R.id.til_address);
        tlOccupation = findViewById(R.id.til_occupation);
        tlCompanyName = findViewById(R.id.til_company_name);
        tlIncome = findViewById(R.id.til_income);

        Intent intent = getIntent();
        userId = intent.getStringExtra("message");
        if (intent == null) {
            Toast.makeText(this, "ERROR, TRY AGAIN", Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "UID NOT FOUND");
            changePreScreen(LoadingPageActivity.class);
        }

        etDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    String name = etName.getText().toString();
                    String dateOfBirth = etDateOfBirth.getText().toString();
                    String nationalID = etNationalID.getText().toString();
                    String email = etEmail.getText().toString();
                    String phone = etPhone.getText().toString();
                    String address = etAddress.getText().toString();
                    String occupation = etOccupation.getText().toString();
                    String companyName = etCompanyName.getText().toString();
                    Double averageSalary = Double.parseDouble(etAverageSalary.getText().toString());
                    Log.d(LOG_TAG, "validate data success");

                    changeNextScreen(OwnOTPActivity.class);
                }

                Log.d(LOG_TAG, "Validate data fail");
            }
        });
    }

    private void resetErrors() {
        tlFullName.setError(null);
        tlDob.setError(null);
        tlNationalId.setError(null);
        tlEmail.setError(null);
        tlPhone.setError(null);
        tlAddress.setError(null);
        tlOccupation.setError(null);
        tlCompanyName.setError(null);
        tlIncome.setError(null);
        tlFullName.setErrorEnabled(false);
        // ... Cần gọi setErrorEnabled(false) cho tất cả các TL khác
    }

    private boolean validateInputs() {
        Log.d(LOG_TAG, "Validate data");

        boolean isValid = true;

        // 1. Reset tất cả lỗi trước khi kiểm tra lại
        resetErrors();

        // 2. Thực hiện kiểm tra chi tiết theo thứ tự (isValid &= ...)

        // Kiểm tra Tên (Bắt buộc + Format)
        isValid &= checkAndShowError(etName, tlFullName, "Full Name", "^[\\p{L} .'-]+$");

        // Kiểm tra Ngày sinh (Chỉ bắt buộc không trống)
        isValid &= checkAndShowError(etDateOfBirth, tlDob, "Date of Birth");

        // Kiểm tra CCCD (Bắt buộc + 12 số)
        isValid &= checkAndShowError(etNationalID, tlNationalId, "National ID", "^\\d{12}$");

        // Kiểm tra Email (Bắt buộc + Format)
        isValid &= checkAndShowError(etEmail, tlEmail, "Email Address", "^[A-Za-z0-9._%+-]+@gmail\\.com$");

        // Kiểm tra Số điện thoại (Bắt buộc + 10 số)
        isValid &= checkAndShowError(etPhone, tlPhone, "Phone Number", "^\\d{10}$");

        // Kiểm tra các trường chỉ bắt buộc không trống
        isValid &= checkAndShowError(etAddress, tlAddress, "Address");
        isValid &= checkAndShowError(etOccupation, tlOccupation, "Occupation");
        isValid &= checkAndShowError(etCompanyName, tlCompanyName, "Company Name");
        isValid &= checkAndShowError(etAverageSalary, tlIncome, "Average Salary");

        return isValid;
    }

    private boolean checkAndShowError(TextView inputField, TextInputLayout layout, String fieldName) {
        return checkAndShowError(inputField, layout, fieldName, null); // Gọi hàm overload 2 với regex=null
    }

    private boolean checkAndShowError(TextView inputField, TextInputLayout layout, String fieldName, String regexPattern) {
        String input = inputField.getText().toString().trim();

        if (input.isEmpty()) {
            String errorMessage = "Please enter your " + fieldName;
            if (layout != null) layout.setError(errorMessage);
            layout.requestFocus();
            return false;
        }

        // Nếu có pattern regex được cung cấp, kiểm tra định dạng
        if (regexPattern != null && !input.matches(regexPattern)) {
            String formatError;

            // Tùy chỉnh thông báo lỗi định dạng
            switch (fieldName) {
                case "Full Name":
                    formatError = "Name cannot contain numbers or special characters.";
                    break;
                case "Phone Number":
                    formatError = "Phone number must be exactly 10 digits.";
                    break;
                case "National ID":
                    formatError = "ID must be exactly 12 digits.";
                    break;
                case "Email Address":
                    formatError = "Email must be a valid address ending with @gmail.com.";
                    break;
                default:
                    formatError = "Invalid format for " + fieldName + ".";
            }

            if (layout != null) layout.setError(formatError);
            layout.requestFocus();
            return false;
        }

        // Dữ liệu hợp lệ: Xóa lỗi (đã gọi trong resetErrors(), nhưng gọi lại để an toàn)
        if (layout != null) {
            layout.setError(null);
            layout.setErrorEnabled(false);
        }
        return true;
    }

    private void changeNextScreen(Class<?> newScreen) {
        Intent intent = new Intent(this, newScreen);

        intent.putExtra("uid", userId);
        intent.putExtra("name", etName.getText().toString());
        intent.putExtra("dateOfBirth", etDateOfBirth.getText().toString());
        intent.putExtra( "nationalID", etNationalID.getText().toString());
        intent.putExtra("email" , etEmail.getText().toString());
        intent.putExtra( "phone" , etPhone.getText().toString());
        intent.putExtra( "address" , etAddress.getText().toString());
        intent.putExtra( "occupation" , etOccupation.getText().toString());
        intent.putExtra( "companyName" , etCompanyName.getText().toString());
        intent.putExtra( "averageSalary" , Double.parseDouble(etAverageSalary.getText().toString()));

        startActivity(intent);
    }

    private void changePreScreen(Class<?> newScreen) {
        Intent intent = new Intent(this, newScreen);
        startActivity(intent);
        finish();
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
                        etDateOfBirth.setText(dateString);
                    }
                },
                year, month, day);

        // Hiển thị dialog
        datePickerDialog.show();
    }
}