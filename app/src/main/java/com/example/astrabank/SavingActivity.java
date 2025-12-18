package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Account;
import com.example.astrabank.utils.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavingActivity extends AppCompatActivity {
    private final String LOG_TAG = "SavingActivity";
    EditText initBalance;
    Button btOpen;
    Long balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_calculator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        initBalance = findViewById(R.id.etInitBalance);
        btOpen = findViewById(R.id.btOpen);
        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                balance = Long.parseLong(initBalance.getText().toString());

                if (LoginManager.getInstance().getAccount().getBalance() < balance) {
                    Toast.makeText(SavingActivity.this, "Balance is not enough", Toast.LENGTH_SHORT).show();
                    return;
                }

                changeScreen(FinishTransactionActivity.class);
            }
        });

        findSavingAccount();
    }




    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        intent.putExtra("requestSavingAccount", "create");
        intent.putExtra("savingAccountBalance", balance);
        startActivity(intent);
    }

    private void findSavingAccount() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Account>> call = apiService.getDefaultAccount(
                LoginManager.getInstance().getUser().getUserID(),
                "SAVING"
        );

        call.enqueue(new Callback<ApiResponse<Account>>() {
            @Override
            public void onResponse(Call<ApiResponse<Account>> call, Response<ApiResponse<Account>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Account> apiResponse = response.body();

                    if (apiResponse != null) {
                        Account account = apiResponse.getResult();

                        if (account != null) {
                            btOpen.setEnabled(false);
                            btOpen.setText("Account exist");
                        }
                    }
                    else {
                        Log.d(LOG_TAG, "Error from server");
                    }
                }
                else {
                    Log.d(LOG_TAG, "Error in processing checking");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Account>> call, Throwable t) {
                Log.d(LOG_TAG, "CHECKING ACCOUNT EXIST:Internet disconnect");
            }
        });
    }

}