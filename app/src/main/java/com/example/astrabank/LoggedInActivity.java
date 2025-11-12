package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View; // Import View
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoggedInActivity extends AppCompatActivity {
    LinearLayout ll_middle;
    private boolean visible = true;
    ImageView iv_move_money, img_toggle_visibility;
    LinearLayout ll_move_money;
    TextView tv_balance_masked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logged_in);
        img_toggle_visibility = findViewById(R.id.img_toggle_visibility);
        iv_move_money = findViewById(R.id.iv_move_money);
        tv_balance_masked = findViewById(R.id.tv_balance_masked);
        iv_move_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransferOptionsBottomSheet();
            }
        });

        img_toggle_visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visible) {
                    tv_balance_masked.setText("7.236.000");
                    tv_balance_masked.setTextSize(10);
                    img_toggle_visibility.setImageResource(R.drawable.ic_show_eye);
                    visible = false;
                }
                else {
                    tv_balance_masked.setText("* * * * * *");
                    img_toggle_visibility.setImageResource(R.drawable.ic_hide_eye);
                    visible = true;
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void showTransferOptionsBottomSheet() {
        TransferOptionsBottomSheet bottomSheet = new TransferOptionsBottomSheet();
        bottomSheet.show(getSupportFragmentManager(), "TransferOptionsTag");
    }
}