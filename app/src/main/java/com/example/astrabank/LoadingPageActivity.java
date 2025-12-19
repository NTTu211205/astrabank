package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        Button btSignUp = findViewById(R.id.btnSignUp);
        Button btSignIn = findViewById(R.id.btnSignIn);
        LinearLayout logoBlock = findViewById(R.id.logo_block);
        LinearLayout bottomBlock = findViewById(R.id.bottom_block);

        // 1. Ẩn nút bottom trước
        bottomBlock.setAlpha(0f);
        bottomBlock.setTranslationY(100f);

        // 2. Xử lý Animation Logo
        logoBlock.post(() -> {
            // Lấy chiều cao thực tế của View cha (màn hình khả dụng)
            View parentView = (View) logoBlock.getParent();
            int parentHeight = parentView.getHeight();

            // Lấy toạ độ Y hiện tại của Logo (lúc này đang ở chính giữa màn hình do XML set)
            float currentY = logoBlock.getY();

            // Tính toán khoảng cách cần dịch chuyển để logo chui xuống đáy màn hình
            // Công thức: (Độ cao màn hình) - (Vị trí hiện tại)
            float startY = parentHeight - currentY;

            // Đặt vị trí bắt đầu ở dưới đáy màn hình (hoặc lệch xuống một chút)
            logoBlock.setTranslationY(startY);

            // Bắt đầu Animation: Trả về 0f (tức là vị trí chính giữa màn hình đã set trong XML)
            logoBlock.animate()
                    .translationY(0f)
                    .setDuration(1000)
                    .setInterpolator(new DecelerateInterpolator())
                    .withEndAction(() -> {
                        // Hiện các nút bấm lên
                        bottomBlock.animate()
                                .alpha(1f)
                                .translationY(0f) // Trả nút về vị trí chuẩn luôn
                                .setDuration(500)
                                .start();
                    })
                    .start();
        });

        // 3. Sự kiện Click
        btSignUp.setOnClickListener(v -> changeScreen(InputPhoneNumberActivity.class));
        btSignIn.setOnClickListener(v -> changeScreen(LoginActivity.class));
    }


    private void changeScreen(Class<?> newScreen) {
        Intent intent = new Intent(this, newScreen);
        startActivity(intent);
        finish();
    }
}