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

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.request.LoginPhoneRequest;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.User;
import com.example.astrabank.utils.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInOTPCodeActivity extends AppCompatActivity {

    private EditText etOTP;
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

        setContentView(R.layout.activity_enter_otpcode);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etOTP = findViewById(R.id.et_phone_number);
        btnVerify = findViewById(R.id.btnLogin);

        mVerificationId = getIntent().getStringExtra("verificationId");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etOTP.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    etOTP.setError("Please enter all 6 OTP codes.");
                    return;
                }
                if (mVerificationId != null) {
                    btnVerify.setEnabled(false);
                    btnVerify.setText("●   ●   ●");
                    verifyOTP(code);
                } else {
                    Toast.makeText(LogInOTPCodeActivity.this, "Authentication error (Lost ID)", Toast.LENGTH_SHORT).show();
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

                        if (user != null) {
                            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                @Override
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {
                                        String uid = task.getResult().getToken();

                                        if (!uid.isEmpty()) {
                                            signIn(uid);
                                        }
                                        else {
                                            Toast.makeText(LogInOTPCodeActivity.this, "User ID not found", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "uid not found");
                                            btnVerify.setEnabled(true);
                                            btnVerify.setText("LOGIN");
                                        }
                                    }
                                    else {
                                        btnVerify.setEnabled(true);
                                        btnVerify.setText("LOGIN");
                                        Toast.makeText(LogInOTPCodeActivity.this, "Error when retrieving data", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Error getting document", task.getException());
                                    }
                                }
                            });
                        }
                        else {
                            btnVerify.setEnabled(true);
                            btnVerify.setText("LOGIN");
                            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Document does not exist for UID: " + user.getUid());
                        }
                    } else {
                        btnVerify.setEnabled(true);
                        btnVerify.setText("LOGIN");
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() != null) {
                            Toast.makeText(this, "The OTP code is incorrect or has expired.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    btnVerify.setEnabled(true);
                    btnVerify.setText("LOGIN");
                    Log.e(SIGN_IN_TAG, "Error", e);
                    Toast.makeText(LogInOTPCodeActivity.this, "System error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void signIn(String uid) {
        LoginPhoneRequest loginPhoneRequest = new LoginPhoneRequest(uid);
        Log.d("Token", uid);


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<User>> call = apiService.loginWithPhone(loginPhoneRequest);
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<User> apiResponse = response.body();

                    if (apiResponse != null) {
                        if (apiResponse.getResult() != null) {
                            User user = apiResponse.getResult();

                            if (user != null) {
                                LoginManager.getInstance().setUser(user);

                                if (user.getRole().equals("OFFICER")) {
                                    changeScreen(AdminDashboardActivity.class);
                                }
                                else {
                                    changeScreen(LoggedInActivity.class);
                                }
                            }
                            else {
                                Toast.makeText(LogInOTPCodeActivity.this, "User not found, try again", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "user not found");
                            }
                        }
                    }
                    else {
                        btnVerify.setEnabled(true);
                        btnVerify.setText("LOGIN");
                        Log.w(TAG, "API Success but response body is null.");
                        Toast.makeText(LogInOTPCodeActivity.this, "Login failed because... \n" +
                                " User data found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    btnVerify.setEnabled(true);
                    btnVerify.setText("LOGIN");
                    Log.e(TAG, "API Error. Code: " + response.code() + ", Msg: " + response.message());
                    Toast.makeText(LogInOTPCodeActivity.this, "The server is not responding.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                btnVerify.setEnabled(true);
                btnVerify.setText("LOGIN");
                Log.e(TAG, "Network failure: " + t.getMessage());
                Toast.makeText(LogInOTPCodeActivity.this, "Network connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeScreen(Class<?> newScreen) {
        Intent intent = new Intent(this, newScreen);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}