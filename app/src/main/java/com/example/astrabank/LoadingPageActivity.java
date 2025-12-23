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

        bottomBlock.setAlpha(0f);
        bottomBlock.setTranslationY(100f);

        logoBlock.post(() -> {
            View parentView = (View) logoBlock.getParent();
            int parentHeight = parentView.getHeight();

            float currentY = logoBlock.getY();

            float startY = parentHeight - currentY;

            logoBlock.setTranslationY(startY);

            logoBlock.animate()
                    .translationY(0f)
                    .setDuration(1000)
                    .setInterpolator(new DecelerateInterpolator())
                    .withEndAction(() -> {
                        bottomBlock.animate()
                                .alpha(1f)
                                .translationY(0f)
                                .setDuration(500)
                                .start();
                    })
                    .start();
        });

        btSignUp.setOnClickListener(v -> changeScreen(InputPhoneNumberActivity.class));
        btSignIn.setOnClickListener(v -> changeScreen(LoginActivity.class));
    }


    private void changeScreen(Class<?> newScreen) {
        Intent intent = new Intent(this, newScreen);
        startActivity(intent);
    }
}