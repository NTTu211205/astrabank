package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText etPhoneNumber;
    Button btLogin;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static final String TAG = "PhoneAuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btLogin = findViewById(R.id.btnLogin);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        TextView btnLoginByEmail = findViewById(R.id.tv_login_by_email);
        mAuth = FirebaseAuth.getInstance();
        createCallBackFunction();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhoneNumber.getText().toString().trim();
                if (phone.isEmpty()) {
                    etPhoneNumber.setError("Vui lòng nhập số điện thoại");
                    return;
                }
                // Xử lý đầu số 0 -> +84
                if (phone.startsWith("0")) {
                    phone = "+84" + phone.substring(1);
                }
                btLogin.setText("●   ●   ●");
                btLogin.setEnabled(false);
                sendOTP(phone);
            }
        });
        btnLoginByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginByEmailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createCallBackFunction() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // Trường hợp tự động xác thực (hiếm khi xảy ra nếu không dùng thư viện play services mới nhất hoặc sim vật lý trên máy)
                // Tuy nhiên ta vẫn nên chuyển sang màn hình nhập OTP để xử lý thống nhất hoặc auto-sign in tại đây nếu muốn.
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                btLogin.setText("GET CODE");
                btLogin.setEnabled(true);
                Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(LoginActivity.this, "Gửi mã thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                btLogin.setText("GET CODE");
                btLogin.setEnabled(true);
                // CHUYỂN MÀN HÌNH SAU KHI ĐÃ GỬI MÃ
                Intent intent = new Intent(LoginActivity.this, LogInOTPCodeActivity.class);
                intent.putExtra("verificationId", verificationId);
                // Truyền sđt qua nếu cần hiển thị lại
                // intent.putExtra("phoneNumber", etPhoneNumber.getText().toString());
                startActivity(intent);
            }
        };
    }

    private void sendOTP(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}