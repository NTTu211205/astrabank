package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton; // <-- Thêm import
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat; // <-- Thêm import
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout; // <-- Thêm import

public class LoggedInActivity extends AppCompatActivity {
    LinearLayout ll_middle;
    private boolean visible = true;
    ImageView iv_move_money, img_toggle_visibility;
    LinearLayout ll_move_money;
    TextView tv_balance_masked;

    // --- Biến mới cho Menu (Side View) ---
    private DrawerLayout drawerLayout;
    private ImageButton ivMenuToggle; // Nút menu bo tròn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logged_in); // Tải file XML (đã chứa DrawerLayout)

        // --- Code cũ của bạn (Giữ nguyên) ---
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

        // --- Code mới để xử lý Menu (Side View) ---
        drawerLayout = findViewById(R.id.drawer_layout);
        ivMenuToggle = findViewById(R.id.iv_menu_toggle);

        ivMenuToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nếu menu đang đóng, mở nó ra từ bên trái
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        // --- Sửa code EdgeToEdge của bạn ---
        // Áp dụng padding cho layout GỐC (DrawerLayout) thay vì ScrollView (main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // --- Code cũ của bạn (Giữ nguyên) ---
    private void showTransferOptionsBottomSheet() {
        TransferOptionsBottomSheet bottomSheet = new TransferOptionsBottomSheet();
        bottomSheet.show(getSupportFragmentManager(), "TransferOptionsTag");
    }

    // --- Code mới: Xử lý nút Back của điện thoại ---
    // (Để đóng menu nếu nó đang mở)
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}