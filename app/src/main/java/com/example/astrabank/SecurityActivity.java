package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.astrabank.api.request.ChangePINRequest;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.utils.LoginManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecurityActivity extends AppCompatActivity {
    private final String LOG_TAG = "SecurityActivity";

    private ImageButton btnBack;
    private TextInputEditText etCurrentPin, etNewPin, etConfirmPin;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_security);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        etCurrentPin = findViewById(R.id.et_current_pin);
        etNewPin = findViewById(R.id.et_new_pin);
        etConfirmPin = findViewById(R.id.et_confirm_pin);
        btnUpdate = findViewById(R.id.btn_update_pin);
    }

    private void setupEvents() {
        // Nút Back
        btnBack.setOnClickListener(v -> finish());

        // Nút Update PIN
        btnUpdate.setOnClickListener(v -> handleUpdatePin());
    }

    private void handleUpdatePin() {
        String currentPin = etCurrentPin.getText().toString();
        String newPin = etNewPin.getText().toString();
        String confirmPin = etConfirmPin.getText().toString();

        if (currentPin.isEmpty() || currentPin.length() < 6) {
            etCurrentPin.setError("Please enter valid current PIN");
            return;
        }

        if (newPin.length() != 6) {
            etNewPin.setError("PIN must be exactly 6 digits");
            return;
        }

        if (!newPin.equals(confirmPin)) {
            etConfirmPin.setError("PIN confirmation does not match");
            return;
        }

        ChangePINRequest changePINRequest = new ChangePINRequest(
                LoginManager.getInstance().getUser().getUserID(),
                currentPin,
                newPin,
                confirmPin
        );

        callApiUpdate(changePINRequest);
    }

    private void callApiUpdate(ChangePINRequest changePINRequest) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Boolean>> call = apiService.changePin(changePINRequest);

        call.enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Boolean> apiResponse = response.body();

                    if (apiResponse != null) {
                        Boolean result = apiResponse.getResult();

                        if (result) {
                            Log.d(LOG_TAG, "Update Success");
                            Toast.makeText(SecurityActivity.this, "Update Success", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Log.d(LOG_TAG, "Update Failed");
                            Toast.makeText(SecurityActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Log.d(LOG_TAG, "Update PIN error");
                        Toast.makeText(SecurityActivity.this, "Update PIN error", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    try {
                        String errorBodyString = response.errorBody().string();                        JSONObject jsonObject = new JSONObject(errorBodyString);
                        String message = jsonObject.getString("message");
                        Log.d(LOG_TAG, message);
                        Toast.makeText(SecurityActivity.this, message, Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Log.d(LOG_TAG, "Error undefined");
                        Toast.makeText(SecurityActivity.this, "Error undefined", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Log.d(LOG_TAG, "internet disconnected");
                Toast.makeText(SecurityActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}