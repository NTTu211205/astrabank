package com.example.astrabank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.request.MortgageAccountRequest;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Account;
import com.example.astrabank.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddMortgageActivity extends AppCompatActivity {
    private final String LOG_TAG = "AdminAddMortgageActivity";
    EditText etInterestRate, etLimit;
    Spinner spUsers;
    Button btSave;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_mortgage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAddMortgageActivity.this, AdminMortgageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        etLimit = findViewById(R.id.etLimit);
        etInterestRate = findViewById(R.id.etInterestRate);
        spUsers = findViewById(R.id.spUser);
        btSave = findViewById(R.id.btn_save);

        callApiGetUserNoMortgage();

        spUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user = (User) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = user.getUserID();
                long limit = Long.parseLong(etLimit.getText().toString());
                double interestRate = Double.parseDouble(etInterestRate.getText().toString());

                callApiCreateMortgageAccount(userId, interestRate, limit);
            }
        });
    }

    private void callApiGetUserNoMortgage() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<User>>> call = apiService.getCustomerNoMortgageAccount();

        call.enqueue(new Callback<ApiResponse<List<User>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<User>>> call, Response<ApiResponse<List<User>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<User>> apiResponse = response.body();

                    if (apiResponse != null && apiResponse.getResult() != null) {
                        List<User> users = apiResponse.getResult();

                        setupUserSpinner(AdminAddMortgageActivity.this, spUsers, users);
                    }
                    else {
                        Log.d(LOG_TAG, "list user not found");
                        Toast.makeText(AdminAddMortgageActivity.this, "List user accounts not found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(LOG_TAG, "list user not found");
                    Toast.makeText(AdminAddMortgageActivity.this, "List user accounts not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<User>>> call, Throwable t) {
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AdminAddMortgageActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callApiCreateMortgageAccount(String userId, double rate, long limit){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Account>> call = apiService.createMortgageAccount(new MortgageAccountRequest(userId, limit, rate));

        call.enqueue(new Callback<ApiResponse<Account>>() {
            @Override
            public void onResponse(Call<ApiResponse<Account>> call, Response<ApiResponse<Account>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Account> apiResponse = response.body();

                    if (apiResponse != null && apiResponse.getResult() != null) {
                        Intent intent = new Intent(AdminAddMortgageActivity.this, AdminMortgageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Log.d(LOG_TAG, "Create failed");
                        Toast.makeText(AdminAddMortgageActivity.this, "Created failed, try again", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(LOG_TAG, "Create failed");
                    Toast.makeText(AdminAddMortgageActivity.this, "Created failed, try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Account>> call, Throwable t) {
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(AdminAddMortgageActivity.this, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUserSpinner(Context context, Spinner spinner, List<User> userList) {

        ArrayAdapter<User> adapter = new ArrayAdapter<User>(context, android.R.layout.simple_spinner_item, userList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView label = (TextView) super.getView(position, convertView, parent);

                User user = getItem(position);


                label.setText(user.getFullName() + " - " + user.getNationalID());

                return label;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView label = (TextView) super.getDropDownView(position, convertView, parent);

                User user = getItem(position);
                label.setText(user.getFullName() + " (" + user.getNationalID() + ")");

                label.setPadding(30, 30, 30, 30); // Padding cho dễ bấm
                return label;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }
}