package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InputNewPhoneNumberActivity extends AppCompatActivity {
    Button bt0, bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9;
    ImageButton btBackSpace, btCheck;
    EditText etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_new_phone_number);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bt0 = findViewById(R.id.btn0);
        bt1 = findViewById(R.id.btn1);
        bt2 = findViewById(R.id.btn2);
        bt3 = findViewById(R.id.btn3);
        bt4 = findViewById(R.id.btn4);
        bt5 = findViewById(R.id.btn5);
        bt6 = findViewById(R.id.btn6);
        bt7 = findViewById(R.id.btn7);
        bt8 = findViewById(R.id.btn8);
        bt9 = findViewById(R.id.btn9);
        btBackSpace = findViewById(R.id.btnBackSpace);
        btCheck = findViewById(R.id.btnCheck);
        etPhoneNumber = findViewById(R.id.et_phone_number);

        setClickListeners();
    }

    public void click(View v) {
        int id = v.getId();

        if (id == R.id.btnBackSpace) {
            handleBackspace();
        } else if (id == R.id.btnCheck) {
            handleCheck();
        } else {
            Button numberButton = (Button) v;
            etPhoneNumber.append(numberButton.getText().toString());
        }
    }

    private void setClickListeners() {
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
        bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
        bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
        btBackSpace.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click(v);
                    }
                }
        );
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
    }

    private void handleBackspace() {
        String currentText = etPhoneNumber.getText().toString();
        if (!currentText.isEmpty()) {
            etPhoneNumber.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    private void handleCheck() {
        String phoneNumber = etPhoneNumber.getText().toString();
        if (phoneNumber.length() != 10) {
            Toast.makeText(this, "Số điện thoại phải có 10 số", Toast.LENGTH_SHORT).show();
            return;
        }
        changeScreen(SentOTPCodeActivity.class, phoneNumber);
    }

    private void changeScreen(Class<?> newScreen, String phoneNumber) {
        Intent intent = new Intent(this, newScreen);
        intent.putExtra("phone", phoneNumber);
        startActivity(intent);
        Log.d("Phone number: ", phoneNumber);
        finish();
    }
}