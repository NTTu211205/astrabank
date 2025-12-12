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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInOTPCodeActivity extends AppCompatActivity {

    private EditText etOTP; // Map với et_phone_number trong XML của bạn
    private Button btnVerify;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String mVerificationId;

    private static final String TAG = "OTPActivity";
    private static final String SIGN_IN_TAG = "OTPActivity:SIGN_IN";
    private static final String GET_USER_TAG = "OTPActivity:GET_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Sử dụng layout bạn đã gửi
        setContentView(R.layout.item_enter_otpcode);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Ánh xạ View
        // LƯU Ý: Trong XML 'item_enter_otpcode.xml', ID của ô nhập liệu là et_phone_number
        etOTP = findViewById(R.id.et_phone_number);
        btnVerify = findViewById(R.id.btnLogin); // Nút Login đóng vai trò Verify

        // Nhận verificationId từ màn hình trước
        mVerificationId = getIntent().getStringExtra("verificationId");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etOTP.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    etOTP.setError("Vui lòng nhập đủ 6 số OTP");
                    return;
                }
                if (mVerificationId != null) {
                    verifyOTP(code);
                } else {
                    Toast.makeText(LogInOTPCodeActivity.this, "Lỗi xác thực (Mất ID)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyOTP(String code) {
        Log.d(SIGN_IN_TAG, "Verifying Code");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        Log.d(TAG, "signInWithCredential:success");
                        Toast.makeText(this, "Xác thực OTP thành công!", Toast.LENGTH_SHORT).show();

                        // Sau khi login Firebase thành công, lấy thông tin user từ Firestore
                        getUserInformation(user.getUid());
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() != null) {
                            Toast.makeText(this, "Mã OTP không đúng hoặc hết hạn.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(SIGN_IN_TAG, "Error", e);
                    Toast.makeText(LogInOTPCodeActivity.this, "Lỗi hệ thống: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void getUserInformation(String uid) {
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    // Lưu user vào Singleton quản lý
                    LoginManager.getInstance().setUser(user);

                    // Chuyển sang màn hình chính
                    changeScreen(LoggedInActivity.class);
                } else {
                    Log.d(GET_USER_TAG, "User not exist in Firestore");
                    Toast.makeText(LogInOTPCodeActivity.this, "Tài khoản chưa được đăng ký trong hệ thống.", Toast.LENGTH_LONG).show();
                    // Ở đây có thể điều hướng sang màn hình Đăng ký mới nếu cần
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(GET_USER_TAG, "Load data error");
                Toast.makeText(LogInOTPCodeActivity.this, "Lỗi tải dữ liệu người dùng.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeScreen(Class<?> newScreen) {
        Intent intent = new Intent(this, newScreen);
        // Xóa back stack để user không back lại màn hình nhập OTP được
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}