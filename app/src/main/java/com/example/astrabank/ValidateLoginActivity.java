package com.example.astrabank;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.request.EmailLoginRequest;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.User;
import com.example.astrabank.utils.LoginManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidateLoginActivity extends AppCompatActivity {
    private final String LOG_TAG = "ValidateLoginActivity";
    String email;
    private List<EditText> otpEditTexts;

    AppCompatButton btSignIn;
    String enteredOtp, name;

    TextView tvName;
    private ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_validate_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");

        otpEditTexts = new ArrayList<>();
        otpEditTexts.add(findViewById(R.id.et_otp_1));
        otpEditTexts.add(findViewById(R.id.et_otp_2));
        otpEditTexts.add(findViewById(R.id.et_otp_3));
        otpEditTexts.add(findViewById(R.id.et_otp_4));
        otpEditTexts.add(findViewById(R.id.et_otp_5));
        otpEditTexts.add(findViewById(R.id.et_otp_6));
        btSignIn = findViewById(R.id.btSignIn);
        tvName = findViewById(R.id.tvName);

        tvName.setText(name);

        showKeyboard();

        setupOtpListeners();

        otpEditTexts.get(0).requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(otpEditTexts.get(0), InputMethodManager.SHOW_IMPLICIT);
        }

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btSignIn.setText("●   ●   ●");
                btSignIn.setEnabled(false);
                if (enteredOtp.isEmpty() || enteredOtp.length() < 6) {
                    btSignIn.setText("SIGN IN");
                    btSignIn.setEnabled(true);
                    Toast.makeText(ValidateLoginActivity.this, "PIN is not valid", Toast.LENGTH_SHORT).show();
                }
                else {
                    callApiLogin(email, enteredOtp);
                }
            }
        });

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void callApiLogin(String email, String pin) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<User>> call = apiService.login(new EmailLoginRequest(email, pin));

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<User> apiResponse = response.body();

                    if (apiResponse != null){
                        User user = apiResponse.getResult();

                        if (user != null) {
                            LoginManager.getInstance().setUser(user);
                            changeScreen(LoggedInActivity.class);
                        }
                        else {
                            Toast.makeText(ValidateLoginActivity.this, "Pin code is wrong, try again", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "User not found");
                            btSignIn.setText("SIGN IN");
                            btSignIn.setEnabled(true);
                        }
                    }
                    else {
                        Toast.makeText(ValidateLoginActivity.this, "Pin code is wrong, try again", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "User not found");
                        btSignIn.setText("SIGN IN");
                        btSignIn.setEnabled(true);
                    }
                }
                else {
                    Toast.makeText(ValidateLoginActivity.this, "Error form server, try again", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Error form server, try again");
                    btSignIn.setText("SIGN IN");
                    btSignIn.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                Toast.makeText(ValidateLoginActivity.this, "Error form server, try again", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Error form server, try again");
                btSignIn.setText("SIGN IN");
                btSignIn.setEnabled(true);
            }
        });
    }

    private void showKeyboard() {
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
                            enteredOtp = otpBuilder.toString();
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

    private void changeScreen(Class<?> newScreen) {
        Intent intent = new Intent(this, newScreen);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}