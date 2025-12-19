package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SelectRecipientActivity extends AppCompatActivity {

    LinearLayout llANewReceiver;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_recipient);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        llANewReceiver = findViewById(R.id.ll_add_new_recipient);
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(v -> finish());
        llANewReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(TransactionToNewPersonActivity.class);
            }
        });
    }

    private void changeScreen(Class<?> nextScreen) {
        Intent intent = new Intent(this, nextScreen);
        startActivity(intent);
    }
}