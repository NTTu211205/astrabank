package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText etPhoneNumber, etPassword;
    Button btLogin;
    int number = 0;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private static final String TAG = "PhoneAuthActivity";
    private static final String SIGN_IN_TAG = "PhoneAuthActivity:SIGN_IN";
    private static final String GET_USER_INFORMATION_TAG = "PhoneAuthActivity:GET_USER_INFORMATION";


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
        etPassword = findViewById(R.id.et_password);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        createCallBackFunction();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number == 0) {
                    Log.d(SIGN_IN_TAG, "REQUEST OTP");
                    String phone = etPhoneNumber.getText().toString();
                    phone = "+84" + phone.substring(1, phone.length());
                    sendOTP(phone);
                }
                else {
                    Log.d(SIGN_IN_TAG, "SIGN IN PROGRESS");
                    String otp = etPassword.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void createCallBackFunction() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(LoginActivity.this, "Gửi mã thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                number = 1;
                etPassword.setVisibility(View.VISIBLE);
                etPassword.requestFocus();
                Toast.makeText(LoginActivity.this, "Đã gửi mã, vui lòng kiểm tra tin nhắn.", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void sendOTP(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Số điện thoại cần gửi
                        .setTimeout(60L, TimeUnit.SECONDS) // Thời gian chờ
                        .setActivity(this)                 // Activity hiện tại
                        .setCallbacks(mCallbacks)          // "Tai nghe" đã tạo ở trên
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Log.d(SIGN_IN_TAG, "PROGRESSING VALIDATING");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        Log.d(TAG, "signInWithCredential:success");
                        Toast.makeText(this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();

                        // get user information
                        getUserInformation(user.getUid());
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(this, "Xác thực thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(SIGN_IN_TAG, "Internet disconection");
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Không có kết nối internet, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getUserInformation(String uid) {
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);

                    LoginManager.getInstance().setUser(user);
                    changeScreen(LoggedInActivity.class);
                }
                else {
                    Log.d(GET_USER_INFORMATION_TAG, "User not exist, cannot load data");
                    Toast.makeText(LoginActivity.this, "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(GET_USER_INFORMATION_TAG, "Load data error");
                Toast.makeText(LoginActivity.this, "Lỗi tải dữ liệu, vui lòng thử lại", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void changeScreen(Class<?> newScreen) {
        Intent intent = new Intent(this, newScreen);
        startActivity(intent);
        finish();
    }

}