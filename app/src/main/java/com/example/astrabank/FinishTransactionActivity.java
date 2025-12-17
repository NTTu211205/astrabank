package com.example.astrabank;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.astrabank.api.request.TransactionRequest;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.constant.TransactionType;
import com.example.astrabank.models.Transaction;
import com.example.astrabank.utils.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinishTransactionActivity extends AppCompatActivity {
    private String desAccountNumber;
    private String receiverName;
    private String desBankSymbol;
    private String sourceAccountNumber;
    private String senderName;
    private String sourceBankSymbol;
    private String content;
    private long amount;
    AppCompatButton tvConfirm;

    private EditText[] otpEditTexts = new EditText[6];
    private final String LOG_TAG = "FinishTransactionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finish_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if (intent != null) {
            desAccountNumber = intent.getStringExtra("desAccountNumber");
            receiverName = intent.getStringExtra("receiverName");
            desBankSymbol = intent.getStringExtra("desBankSymbol");
            sourceAccountNumber = intent.getStringExtra("sourceAccountNumber");
            senderName = intent.getStringExtra("senderName");
            sourceBankSymbol = intent.getStringExtra("sourceBankSymbol");
            content = intent.getStringExtra("content");
            amount = intent.getLongExtra("amount", 0L);
        }

        otpEditTexts[0] = findViewById(R.id.et_otp_1);
        otpEditTexts[1] = findViewById(R.id.et_otp_2);
        otpEditTexts[2] = findViewById(R.id.et_otp_3);
        otpEditTexts[3] = findViewById(R.id.et_otp_4);
        otpEditTexts[4] = findViewById(R.id.et_otp_5);
        otpEditTexts[5] = findViewById(R.id.et_otp_6);
        tvConfirm = findViewById(R.id.tv_confirm);

        for (EditText editText : otpEditTexts) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        setupOtpInputs();

        otpEditTexts[0].requestFocus();

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = getOtpCode();
                tvConfirm.setText("●   ●   ●");
                tvConfirm.setEnabled(false);
                checkPin(pin);
            }
        });
    }

    private void checkPin(String pin) {
        Log.d(LOG_TAG, pin + " " + LoginManager.getInstance().getUser().getUserID());
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Boolean>> call = apiService.validateTransaction(LoginManager.getInstance().getUser().getUserID(), pin);

        call.enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Boolean> apiResponse = response.body();

                    if (apiResponse != null) {
                        Boolean check = apiResponse.getResult();

                        if (check == null) {
                            tvConfirm.setText("Confirm");
                            tvConfirm.setEnabled(true);
                            Toast.makeText(FinishTransactionActivity.this, "Lỗi trong quá trình kiểm tra mã pin", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "CHECK_PIN:processing validate error, result is null");
                        }
                        else if (check) {
                            Log.d(LOG_TAG, "CHECK_PIN:transaction pin is correct");
                            // progress transaction
                            progressInBankTransaction();

                        }
                        else {
                            tvConfirm.setText("Confirm");
                            tvConfirm.setEnabled(true);
                            Toast.makeText(FinishTransactionActivity.this, "Mã pin không đúng, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "CHECK_PIN:transaction pin is wrong");
                        }
                    }
                    else {
                        tvConfirm.setText("Confirm");
                        tvConfirm.setEnabled(true);
                        Toast.makeText(FinishTransactionActivity.this, "Không tìm được tài khoản phù hợp", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "CHECK_PIN:Can not load account data");
                    }
                }
                else {
                    tvConfirm.setText("Confirm");
                    tvConfirm.setEnabled(true);
                    Toast.makeText(FinishTransactionActivity.this, "Máy chủ không phản hồi", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "CHECK_PIN:Error from server");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                tvConfirm.setText("Confirm");
                tvConfirm.setEnabled(true);
                Toast.makeText(FinishTransactionActivity.this, "Kiểm tra kết nối internet", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "CHECK_PIN:Internet disconnect");
            }
        });
    }

    private void progressInBankTransaction(){
        TransactionRequest transactionRequest = new TransactionRequest(
                sourceAccountNumber,
                sourceBankSymbol,
                desAccountNumber,
                desBankSymbol,
                amount,
                TransactionType.TRANSFER,
                content,
                senderName,
                receiverName
        );

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Transaction>> call;
        if (desBankSymbol.equals("ATB")) {
            call = apiService.progressTransfer(transactionRequest);
        }
        else {
            call = apiService.sendTransaction(transactionRequest);
        }

        call.enqueue(new Callback<ApiResponse<Transaction>>() {
            @Override
            public void onResponse(Call<ApiResponse<Transaction>> call, Response<ApiResponse<Transaction>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Transaction> apiResponse = response.body();

                    if (apiResponse != null) {
                        Transaction transaction = apiResponse.getResult();

                        if (transaction != null) {
                            Log.d(LOG_TAG, "PROGRESS_TRANSACTION:Transaction success");
                            changeNextScreen(
                                    TransactionSuccessActivity.class,
                                    transaction.getAmount(),
                                    transaction.getDestinationAcc(),
                                    transaction.getReceiverName(),
                                    transaction.getBankDesSymbol(),
                                    transaction.getDescription(),
                                    transaction.getCreatedAt(),
                                    transaction.getTransactionId()
                                    );
                        }
                        else {
                            tvConfirm.setText("Confirm");
                            tvConfirm.setEnabled(true);
                            Toast.makeText(FinishTransactionActivity.this, "Giao dịch không thành công", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "PROGRESS_TRANSACTION:Transaction failed");
                        }
                    }
                    else {
                        tvConfirm.setText("Confirm");
                        tvConfirm.setEnabled(true);
                        Toast.makeText(FinishTransactionActivity.this, "Lỗi trong quá trình xử lí giao dịch", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "PROGRESS_TRANSACTIONTransaction processing error");
                    }
                }
                else {
                    tvConfirm.setText("Confirm");
                    tvConfirm.setEnabled(true);
                    Toast.makeText(FinishTransactionActivity.this, "Máy chủ không phản hồi", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "PROGRESS_TRANSACTIONError from server");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Transaction>> call, Throwable t) {
                tvConfirm.setText("Confirm");
                tvConfirm.setEnabled(true);
                Toast.makeText(FinishTransactionActivity.this, "Kiểm tra kết nối internet", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "PROGRESS_TRANSACTIONInternet disconnect");
            }
        });
    }

    private void changeNextScreen(Class<?> nextScreen, long amount, String receiverAccountNumber, String receiverName,
                                  String desBankSymbol, String content, String date, String id) {
        Intent intent = new Intent(this, nextScreen);
        intent.putExtra("amount", amount);
        intent.putExtra("receiverAccountNumber", receiverAccountNumber);
        intent.putExtra("receiverName", receiverName);
        intent.putExtra("desBankSymbol", desBankSymbol);
        intent.putExtra("content", content);
        intent.putExtra("date", date);
        intent.putExtra("transactionId", id);
        startActivity(intent);
        finish();
    }

    private void setupOtpInputs() {
        for (int i = 0; i < otpEditTexts.length; i++) {
            final int index = i;

            otpEditTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpEditTexts.length - 1) {
                        otpEditTexts[index + 1].requestFocus(); // Chuyển focus sang ô kế tiếp
                    }
                    else if (s.length() == 1 && index == otpEditTexts.length - 1) {
                        hideKeyboard();

                        otpEditTexts[index].clearFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            otpEditTexts[i].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                        if (otpEditTexts[index].getText().toString().isEmpty() && index > 0) {
                            otpEditTexts[index - 1].requestFocus(); // Chuyển focus về ô trước
                            otpEditTexts[index - 1].setSelection(otpEditTexts[index - 1].getText().length()); // Đặt con trỏ vào cuối
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private String getOtpCode() {
        StringBuilder code = new StringBuilder();
        for (EditText et : otpEditTexts) {
            code.append(et.getText().toString());
        }
        return code.toString();
    }
}