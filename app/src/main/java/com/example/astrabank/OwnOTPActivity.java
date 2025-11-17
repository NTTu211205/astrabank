package com.example.astrabank; // Thay bằng package của bạn

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast; // Dùng để hiển thị thông báo tạm thời

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class OwnOTPActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private List<EditText> otpEditTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_own_otp);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish()); // Quay lại màn hình trước đó

        // Khởi tạo danh sách các ô OTP
        otpEditTexts = new ArrayList<>();
        otpEditTexts.add(findViewById(R.id.et_otp_1));
        otpEditTexts.add(findViewById(R.id.et_otp_2));
        otpEditTexts.add(findViewById(R.id.et_otp_3));
        otpEditTexts.add(findViewById(R.id.et_otp_4));
        otpEditTexts.add(findViewById(R.id.et_otp_5));
        otpEditTexts.add(findViewById(R.id.et_otp_6));

        setupOtpListeners(); // Thiết lập lắng nghe cho các ô OTP

        // Tự động mở bàn phím khi màn hình được tạo
        otpEditTexts.get(0).requestFocus(); // Focus vào ô đầu tiên
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(otpEditTexts.get(0), InputMethodManager.SHOW_IMPLICIT);
        }

        // Xử lý sự kiện click cho "Quên mã mở khóa?"
        findViewById(R.id.tv_forgot_otp).setOnClickListener(v -> {
            Toast.makeText(OwnOTPActivity.this, "Bạn đã nhấn Quên mã mở khóa", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupOtpListeners() {
        for (int i = 0; i < otpEditTexts.size(); i++) {
            final int currentIndex = i;
            final EditText currentEditText = otpEditTexts.get(currentIndex);

            currentEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Không cần làm gì ở đây
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Nếu có ký tự được nhập
                    if (s.length() == 1) {
                        // Di chuyển focus sang ô tiếp theo (nếu có)
                        if (currentIndex < otpEditTexts.size() - 1) {
                            otpEditTexts.get(currentIndex + 1).requestFocus();
                        } else {
                            // Nếu đã nhập hết các ô, đóng bàn phím và kiểm tra mã
                            hideKeyboard();
                            checkOtp();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Không cần làm gì ở đây
                }
            });

            currentEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // Xử lý nút Backspace
                    if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                        // Nếu ô hiện tại rỗng, di chuyển focus về ô trước đó
                        if (currentEditText.getText().length() == 0 && currentIndex > 0) {
                            otpEditTexts.get(currentIndex - 1).requestFocus();
                            otpEditTexts.get(currentIndex - 1).setText(""); // Xóa ký tự ở ô trước đó
                            return true; // Đã xử lý sự kiện
                        }
                    }
                    return false; // Không xử lý sự kiện, để hệ thống xử lý tiếp
                }
            });
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private void checkOtp() {
        StringBuilder otpBuilder = new StringBuilder();
        for (EditText editText : otpEditTexts) {
            otpBuilder.append(editText.getText().toString());
        }
        String enteredOtp = otpBuilder.toString();

        // TODO: Thực hiện kiểm tra mã OTP ở đây
        // Ví dụ:
        if (enteredOtp.length() == otpEditTexts.size()) {
            Toast.makeText(this, "Mã OTP đã nhập: " + enteredOtp, Toast.LENGTH_SHORT).show();
            if (enteredOtp.equals("123456")) { // Thay "123456" bằng mã OTP đúng
                Toast.makeText(this, "Mã OTP chính xác!", Toast.LENGTH_SHORT).show();
                // TODO: Chuyển sang màn hình chính hoặc màn hình tiếp theo
                // Intent intent = new Intent(OwnOTPActivity.this, LoggedInActivity.class);
                // startActivity(intent);
                // finish();
            } else {
                Toast.makeText(this, "Mã OTP không đúng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                // TODO: Xóa các ô OTP hoặc thực hiện hành động lỗi
                clearOtpFields();
            }
        }
    }

    private void clearOtpFields() {
        for (EditText editText : otpEditTexts) {
            editText.setText("");
        }
        otpEditTexts.get(0).requestFocus(); // Đặt focus lại vào ô đầu tiên
        showKeyboard();
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(otpEditTexts.get(0), InputMethodManager.SHOW_IMPLICIT);
        }
    }
}