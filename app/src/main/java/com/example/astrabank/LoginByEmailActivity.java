package com.example.astrabank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginByEmailActivity extends AppCompatActivity {
    private final String LOG_TAG = "LoginByEmailActivity";
    EditText etEmail;
    AppCompatButton btLogin;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_by_email);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("AstraBankPrefs", Context.MODE_PRIVATE);
        String emailRemember = sharedPreferences.getString("EMAIL", null);

        etEmail = findViewById(R.id.etEmail);
        btLogin = findViewById(R.id.btnLogin);

        if (emailRemember != null) {
            etEmail.setText(emailRemember);
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(LoginByEmailActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                }
                else {
                    callApiFindUser(email);
                }
            }
        });
    }

    private void callApiFindUser(String email) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<User>> call = apiService.findUserByEmail(email);

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<User> apiResponse = response.body();

                    if (apiResponse != null) {
                        User user = apiResponse.getResult();

                        if (user != null) {
                            changeScreen(ValidateLoginActivity.class, email, user.getFullName());
                        }
                        else {
                            Toast.makeText(LoginByEmailActivity.this, "User not found, check your email", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "User not found");
                        }
                    }
                    else {
                        Toast.makeText(LoginByEmailActivity.this, "Error form server, try again", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "Error form server, try again");
                    }
                }
                else {
                    Toast.makeText(LoginByEmailActivity.this, "Error form server, try again", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Error form server, try again");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                Toast.makeText(LoginByEmailActivity.this, "Internet disconnected, try again", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Internet disconnect, try again");
            }
        });
    }

    private void changeScreen(Class<?> nextScreen, String message, String name){
        Intent intent = new Intent(this, nextScreen);
        intent.putExtra("email", message);
        intent.putExtra("name", name);
        startActivity(intent);
        finish();
    }
}