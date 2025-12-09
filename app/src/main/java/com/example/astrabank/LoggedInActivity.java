package com.example.astrabank;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton; // <-- Thêm import
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat; // <-- Thêm import
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout; // <-- Thêm import

import com.example.astrabank.utils.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class LoggedInActivity extends AppCompatActivity {
    LinearLayout ll_middle,ll_products, ll_account_and_card;
    private boolean visible = true;
    ImageView iv_move_money, img_toggle_visibility, ivHeadClose, btnPhoneCall;
    LinearLayout ll_move_money;
    TextView tv_balance_masked, tvName;
    NavigationView navigationView;
    View navHeader, navFooter;
    AppCompatButton btSignOut;

    // --- Biến mới cho Menu (Side View) ---
    private DrawerLayout drawerLayout;
    private ImageButton ivMenuToggle; // Nút menu bo tròn
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logged_in); // Tải file XML (đã chứa DrawerLayout)

        img_toggle_visibility = findViewById(R.id.img_toggle_visibility);
        iv_move_money = findViewById(R.id.iv_move_money);
        tv_balance_masked = findViewById(R.id.tv_balance_masked);
        navigationView = findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        ll_account_and_card = findViewById(R.id.ll_accountAndCard);
        ll_move_money = findViewById(R.id.ll_moveMoney);
//        navFooter = navigationView.findViewById(R.id.footer_root);
        ivHeadClose = navHeader.findViewById(R.id.iv_header_close);
        btSignOut = navHeader.findViewById(R.id.btn_logout);
        btnPhoneCall = navHeader.findViewById(R.id.btn_phone_call);
//        btSignOut = navFooter.findViewById(R.id.btn_dang_xuat);
        tvName = navHeader.findViewById(R.id.tv_header_name);
        ll_products = findViewById(R.id.ll_discoverProducts);

        mAuth = FirebaseAuth.getInstance();

        tvName.setText("Hello,\n" + LoginManager.getInstance().getUser().getFullName());



        ivHeadClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

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
        btnPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0392593911"));
                startActivity(intent);
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
        ll_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, DiscoverProductsActivity.class);
                startActivity(intent);
            }
        });
        ll_account_and_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, AccountAndCardActivity.class);
                startActivity(intent);
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

    private void signOut() {
        mAuth.signOut();
        changeScreen(LoadingPageActivity.class);
    }

    private void changeScreen(Class<?> newScreen) {
        Intent intent = new Intent(this, newScreen);
        startActivity(intent);
        finish();
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