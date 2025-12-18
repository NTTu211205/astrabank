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

        // 1. Ẩn khối nút bấm lúc đầu (để trong suốt)
        bottomBlock.setAlpha(0f);
        // Dịch chuyển khối nút xuống một chút để lát hiện lên cho đẹp
        bottomBlock.setTranslationY(100f);

        // 2. Xử lý Animation
        logoBlock.post(() -> {
            // Lấy chiều cao màn hình và chiều cao logo
            float screenHeight = getResources().getDisplayMetrics().heightPixels;

            // Tính toán để dịch chuyển logo xuống dưới đáy màn hình
            // Hiện tại logo đang ở giữa (screenHeight/2). Muốn nó xuống đáy thì cộng thêm screenHeight/2
            logoBlock.setTranslationY(screenHeight / 2f);

            // Animation: Logo lướt từ dưới lên vị trí chính giữa (0f)
            logoBlock.animate()
                    .translationY(0f)
                    .setDuration(1000)
                    .setInterpolator(new DecelerateInterpolator())
                    .withEndAction(() -> {
                        bottomBlock.animate()
                                .alpha(1f)
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