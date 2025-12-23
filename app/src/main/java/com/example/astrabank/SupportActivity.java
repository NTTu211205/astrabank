package com.example.astrabank;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        copyHandler(R.id.btn_copy_it_phone, R.id.tv_it_phone, "Số điện thoại IT");
        copyHandler(R.id.btn_copy_it_email, R.id.tv_it_email, "Email IT");
        copyHandler(R.id.btn_copy_error_phone, R.id.tv_error_phone, "Số điện thoại xử lý lỗi");
        copyHandler(R.id.btn_copy_error_email, R.id.tv_error_email, "Email xử lý lỗi");
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void copyHandler(int btnId, int textId, String label) {
        ImageButton button = findViewById(btnId);
        TextView textView = findViewById(textId);

        button.setOnClickListener(v -> {
            ClipboardManager clipboard =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clip = ClipData.newPlainText(label, textView.getText().toString());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "Copied " + label, Toast.LENGTH_SHORT).show();
        });
    }
}
