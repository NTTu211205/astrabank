package com.example.astrabank; // Thay bằng package của bạn

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast; // Dùng để hiển thị thông báo tạm thời

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.astrabank.constant.AccountType;
import com.example.astrabank.controller.AccountController;
import com.example.astrabank.controller.UserController;
import com.example.astrabank.utils.BCryptService;
import com.example.astrabank.utils.CallBack;
import com.example.astrabank.utils.LoginManager;

import java.util.ArrayList;
import java.util.List;

public class OwnOTPActivity extends AppCompatActivity {
    private final String LOG_TAG = "OwnOTPActivity";
    private ImageButton btnBack;
    private AppCompatButton btSignIn;
    private List<EditText> otpEditTexts;
    private String uid;
    private String name;
    private String dateOfBirth;
    private String nationalID;
    private String email;
    private String phone;
    private String address;
    private String occupation;
    private String companyName;
    private double averageSalary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_own_otp);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        this.uid = intent.getStringExtra("uid");
        this.name = intent.getStringExtra("name");
        this.dateOfBirth = intent.getStringExtra("dateOfBirth");
        this.nationalID = intent.getStringExtra("nationalID");
        this.email = intent.getStringExtra("email");
        this.phone = intent.getStringExtra("phone");
        this.address = intent.getStringExtra("address");
        this.occupation = intent.getStringExtra("occupation");
        this.companyName = intent.getStringExtra("companyName");

        // Đối với Double, luôn cung cấp giá trị mặc định
        this.averageSalary = intent.getDoubleExtra("averageSalary", 0.0);

        otpEditTexts = new ArrayList<>();
        otpEditTexts.add(findViewById(R.id.et_otp_1));
        otpEditTexts.add(findViewById(R.id.et_otp_2));
        otpEditTexts.add(findViewById(R.id.et_otp_3));
        otpEditTexts.add(findViewById(R.id.et_otp_4));
        otpEditTexts.add(findViewById(R.id.et_otp_5));
        otpEditTexts.add(findViewById(R.id.et_otp_6));

        showKeyboard();

        setupOtpListeners();

        otpEditTexts.get(0).requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(otpEditTexts.get(0), InputMethodManager.SHOW_IMPLICIT);
        }

    }

    private void setupOtpListeners() {
        for (int i = 0; i < otpEditTexts.size(); i++) {
            final int currentIndex = i;
            final EditText currentEditText = otpEditTexts.get(currentIndex);

            currentEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1) {
                        if (currentIndex < otpEditTexts.size() - 1) {
                            otpEditTexts.get(currentIndex + 1).requestFocus();
                        } else {
                            hideKeyboard();

                            StringBuilder otpBuilder = new StringBuilder();
                            for (EditText editText : otpEditTexts) {
                                otpBuilder.append(editText.getText().toString());
                            }
                            String enteredOtp = otpBuilder.toString();

                            showEventNotificationDialog(enteredOtp);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Không cần làm gì ở đây
                }
            });

            currentEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (currentEditText.getText().length() == 0 && currentIndex > 0) {
                            otpEditTexts.get(currentIndex - 1).requestFocus();
                            otpEditTexts.get(currentIndex - 1).setText("");
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private void clearOtpFields() {
        for (EditText editText : otpEditTexts) {
            editText.setText("");
        }
        otpEditTexts.get(0).requestFocus(); // Đặt focus lại vào ô đầu tiên
        showKeyboard();
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(otpEditTexts.get(0), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void showEventNotificationDialog(String transactionOTP) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("TẠO MÃ GIAO DỊCH");
        builder.setMessage("Đây chính là mã giao dịch bạn dùng để chuyển tiên \n" +
                "Bạn có chắc đây là mã giao dịch của bạn không ??? \n"
                + transactionOTP);

        builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Log.d(LOG_TAG, "Select continue");
                dialog.dismiss();
                saveUserToDB(uid, name, dateOfBirth, nationalID,
                        email, phone, address,
                        occupation, companyName, averageSalary,
                        transactionOTP);
                saveAccountToDB(uid, AccountType.CHECKING);
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Log.d(LOG_TAG, "Select cancel");
                dialog.dismiss();
            }
        });

        builder.setCancelable(true);

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d(LOG_TAG, "Dialog đã bị HỦY (Cancel) - Có thể do click ngoài hoặc nút Back.");
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveUserToDB(String userID, String fullName, String dateOfBirth,
                        String nationalID, String email, String phone, String address,
                        String occupation, String companyName, Double averageSalary,
                        String transactionPIN) {

        String hashTransactionOTP = BCryptService.hashPassword(transactionPIN);
        if (hashTransactionOTP == null) {
            Toast.makeText(this, "Tạo mã giao dịch lỗi", Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Hashing transaction OTP error");
            return;
        }
    }

    private void saveAccountToDB(String uid, AccountType accountType) {
        AccountController accountController = new AccountController(this);
        accountController.createAccount(uid, accountType);

    }

    private void changeScreen(Class<?> newScreen) {
        Intent intent = new Intent(this, newScreen);
        startActivity(intent);
        finish();
    }
}