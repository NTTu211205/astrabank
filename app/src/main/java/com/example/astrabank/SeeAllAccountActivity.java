package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Account;
import com.example.astrabank.utils.LoginManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllAccountActivity extends AppCompatActivity {
    private final String LOG_TAG = "SeeAllAccountActivity";

    // 1. Khai báo các biến View
    private ImageButton btnBack;
    private Button btnSeeCheckingAcc, btnSeeSavingAcc, btnSeeMortgageAcc;


    // Layout cha của từng thẻ (để bắt sự kiện click vào cả thẻ)
    private RelativeLayout rlChecking, rlSavings, rlMortgage;

    // Các TextView hiển thị dữ liệu
    private TextView tvCheckingNumber, tvCheckingBalance;
    private TextView tvSavingsNumber, tvSavingsBalance, tvInterestRate, tvMonthlyProfit;
    private TextView tvMortgageNumber, tvPaymentAmount, tvPaymentFrequency;

    private String savingAccountNumber;
    private String mortgageAccountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_all_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        setupData();
        setupEvents();
    }

    // 2. Ánh xạ ID từ XML sang Java
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);

        // Layouts
        rlChecking = findViewById(R.id.rlCheckingAccount);
        rlSavings = findViewById(R.id.rlSavingsAccount);
        rlMortgage = findViewById(R.id.rlMortgageAccount);

        // Checking Account Views
        tvCheckingNumber = findViewById(R.id.tvCheckingNumber);
        tvCheckingBalance = findViewById(R.id.tvCheckingBalance);

        // Savings Account Views
        tvSavingsNumber = findViewById(R.id.tvSavingsNumber);
        tvSavingsBalance = findViewById(R.id.tvSavingsBalance);
        tvInterestRate = findViewById(R.id.tvInterestRate);
        tvMonthlyProfit = findViewById(R.id.tvMonthlyProfit);

        // Mortgage Account Views
        tvMortgageNumber = findViewById(R.id.tvMortgageNumber);
        tvPaymentAmount = findViewById(R.id.tvPaymentAmount);
        tvPaymentFrequency = findViewById(R.id.tvPaymentFrequency);

        btnSeeCheckingAcc = findViewById(R.id.btnSeeCheckingAcc);
        btnSeeSavingAcc = findViewById(R.id.btnSeeSavingAcc);
        btnSeeMortgageAcc = findViewById(R.id.btnSeeMortgageAcc);
    }

    private void setupData() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        // --- Checking Account ---
        String checkAccountNumber = LoginManager.getInstance().getAccount().getAccountNumber();
        tvCheckingNumber.setText("****" + checkAccountNumber.substring(checkAccountNumber.length() - 5, checkAccountNumber.length()));
        findDefaultAccount(LoginManager.getInstance().getUser().getUserID(), "CHECKING");

        // --- Savings Account ---
        findSavingAccount(LoginManager.getInstance().getUser().getUserID(), "SAVING");
        tvMonthlyProfit.setText("+ " + currencyFormat.format(45.00)); // Lợi nhuận tháng


        findMortgageAccount(LoginManager.getInstance().getUser().getUserID(), "MORTGAGE");
//        tvMortgageNumber.setText("Loan #9988-77");

        boolean payMonthly = true;

//        if (payMonthly) {
//            tvPaymentAmount.setText(currencyFormat.format(1250.00));
//            tvPaymentFrequency.setText("Monthly");
//        } else {
//            tvPaymentAmount.setText(currencyFormat.format(625.00));
//            tvPaymentFrequency.setText("Bi-weekly");
//        }
    }

    private String formatMoney(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Bắt buộc dùng dấu chấm
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount);
    }

    private void findSavingAccount(String userID, String checking) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Account>> call = apiService.getDefaultAccount(userID, checking);

        call.enqueue(new Callback<ApiResponse<Account>>() {
            @Override
            public void onResponse(Call<ApiResponse<Account>> call, Response<ApiResponse<Account>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Account> apiResponse = response.body();

                    if (apiResponse != null) {
                        if (apiResponse.getResult() != null) {
                            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
                            Account account = apiResponse.getResult();
                            savingAccountNumber = account.getAccountNumber();
                            tvSavingsNumber.setText("*****" + savingAccountNumber.substring(savingAccountNumber.length()-5, savingAccountNumber.length()));
                            tvSavingsBalance.setText(formatMoney(account.getBalance()));
                            tvInterestRate.setText(String.valueOf(account.getInterestRate() * 100) + " %"); // Lãi suất
                            long profit = (long) (account.getBalance() * account.getInterestRate() / 12);
                            tvMonthlyProfit.setText(formatMoney(profit));
                            rlSavings.setVisibility(View.VISIBLE);
                            btnSeeSavingAcc.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        rlSavings.setVisibility(View.GONE);
                        btnSeeSavingAcc.setVisibility(View.GONE);
                    }
                }
                else {
                    Log.e(LOG_TAG, "API Error. Code: " + response.code() + ", Msg: " + response.message());
                    Toast.makeText(SeeAllAccountActivity.this, "Máy chủ không phản hồi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Account>> call, Throwable t) {
                Log.e(LOG_TAG, "Network failure: " + t.getMessage());
                Toast.makeText(SeeAllAccountActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findMortgageAccount(String userID, String checking) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Account>> call = apiService.getDefaultAccount(userID, checking);

        call.enqueue(new Callback<ApiResponse<Account>>() {
            @Override
            public void onResponse(Call<ApiResponse<Account>> call, Response<ApiResponse<Account>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Account> apiResponse = response.body();

                    if (apiResponse != null) {
                        if (apiResponse.getResult() != null) {
                            mortgageAccountNumber = apiResponse.getResult().getAccountNumber();
                            tvMortgageNumber.setText(apiResponse.getResult().getAccountNumber());
                            tvPaymentAmount.setText(apiResponse.getResult().getInterestRate() + " %");
                            rlMortgage.setVisibility(View.VISIBLE);
                            btnSeeMortgageAcc.setVisibility(View.VISIBLE);
                        }
                        else {
                            rlMortgage.setVisibility(View.GONE);
                            btnSeeMortgageAcc.setVisibility(View.GONE);
                        }
                    }
                    else {
                        rlMortgage.setVisibility(View.GONE);
                        btnSeeMortgageAcc.setVisibility(View.GONE);
                    }
                }
                else {
                    rlMortgage.setVisibility(View.GONE);
                    btnSeeMortgageAcc.setVisibility(View.GONE);
                    Log.e(LOG_TAG, "API Error. Code: " + response.code() + ", Msg: " + response.message());
                    Toast.makeText(SeeAllAccountActivity.this, "Máy chủ không phản hồi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Account>> call, Throwable t) {
                rlMortgage.setVisibility(View.GONE);
                btnSeeMortgageAcc.setVisibility(View.GONE);
                Log.e(LOG_TAG, "Network failure: " + t.getMessage());
                Toast.makeText(SeeAllAccountActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findDefaultAccount(String userID, String checking) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Account>> call = apiService.getDefaultAccount(userID, checking);

        call.enqueue(new Callback<ApiResponse<Account>>() {
            @Override
            public void onResponse(Call<ApiResponse<Account>> call, Response<ApiResponse<Account>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Account> apiResponse = response.body();

                    if (apiResponse != null) {
                        if (apiResponse.getResult() != null) {
                            Account account = apiResponse.getResult();
                            tvCheckingBalance.setText(formatMoney(account.getBalance()));
                        }
                    }
                    else {
                        Log.w(LOG_TAG, "API Success but body is null.");
                        Toast.makeText(SeeAllAccountActivity.this, "Không tìm thấy tài khoản mặc định", Toast.LENGTH_SHORT).show();
                        changeScreen(LoggedInActivity.class);
                        LoginManager.clearUser();
                    }
                }
                else {
                    Log.e(LOG_TAG, "API Error. Code: " + response.code() + ", Msg: " + response.message());
                    Toast.makeText(SeeAllAccountActivity.this, "Máy chủ không phản hồi", Toast.LENGTH_SHORT).show();
                    changeScreen(LoadingPageActivity.class);
                    LoginManager.clearUser();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Account>> call, Throwable t) {
                Log.e(LOG_TAG, "Network failure: " + t.getMessage());
                Toast.makeText(SeeAllAccountActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                changeScreen(LoadingPageActivity.class);
                LoginManager.clearUser();
            }
        });
    }

    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        startActivity(intent);
        finish();
    }

    private void setupEvents() {
        // Nút Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity này để quay lại màn hình trước
            }
        });

//        rlChecking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View dialogView = getLayoutInflater().inflate(R.layout.item_checking_account, null);
//
//                // 2. Tạo hộp thoại chứa view đó
//                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SeeAllAccountActivity.this);
//                builder.setView(dialogView);
//                builder.setCancelable(true);
//
//                // 3. Hiển thị
//                builder.show();
//            }
//        });

//        rlSavings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View dialogView = getLayoutInflater().inflate(R.layout.item_saving_account, null);
//
//                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SeeAllAccountActivity.this);
//                builder.setView(dialogView);
//                builder.setCancelable(true);
//
//                // 3. Hiển thị
//                builder.show();
//            }
//        });

//        rlMortgage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 1. Tạo view từ file xml
//                View dialogView = getLayoutInflater().inflate(R.layout.item_mortage_account, null);
//
//                // 2. Tạo hộp thoại chứa view đó
//                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SeeAllAccountActivity.this);
//                builder.setView(dialogView);
//                builder.setCancelable(true);
//
//                // 3. Hiển thị
//                builder.show();
//            }
//        });
        btnSeeCheckingAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeAllAccountActivity.this, AccountAndCardActivity.class);
                startActivity(intent);
            }
        });
        btnSeeSavingAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeAllAccountActivity.this, SavingsDetailsActivity.class);
                startActivity(intent);
            }
        });
        btnSeeMortgageAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeAllAccountActivity.this, MortgageDetailsActivity.class);
                intent.putExtra("accountNumber", mortgageAccountNumber);
                startActivity(intent);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}