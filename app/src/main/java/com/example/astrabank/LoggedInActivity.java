package com.example.astrabank;

import static androidx.camera.core.CameraXThreads.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton; // <-- Thêm import
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat; // <-- Thêm import
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout; // <-- Thêm import

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Account;
import com.example.astrabank.utils.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoggedInActivity extends AppCompatActivity {
    private final String LOG_TAG = "LoggedInActivity";
    LinearLayout ll_middle,ll_products, ll_account_and_card;
    RelativeLayout rl_maps;
    private boolean visible = true;
    ImageView iv_move_money, img_toggle_visibility, ivHeadClose, btnPhoneCall, btnNotifications;
    LinearLayout ll_move_money;
    TextView tv_balance_masked, tvName;
    NavigationView navigationView, navMenuInner;
    View navHeader, navFooter;
    AppCompatButton btSignOut;

    // --- Biến mới cho Menu (Side View) ---
    private DrawerLayout drawerLayout;
    private ImageButton ivMenuToggle; // Nút menu bo tròn
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration balanceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logged_in); // Tải file XML (đã chứa DrawerLayout)

        img_toggle_visibility = findViewById(R.id.img_toggle_visibility);
        iv_move_money = findViewById(R.id.iv_move_money);
        tv_balance_masked = findViewById(R.id.tv_balance_masked);
        navigationView = findViewById(R.id.nav_view);
        ll_account_and_card = findViewById(R.id.ll_accountAndCard);
        ll_move_money = findViewById(R.id.ll_moveMoney);
        ivHeadClose = findViewById(R.id.iv_header_close);
        btSignOut = findViewById(R.id.btn_logout);
        btnPhoneCall = findViewById(R.id.btn_phone_call);
        tvName = findViewById(R.id.tv_header_name);
        ll_products = findViewById(R.id.ll_discoverProducts);
        rl_maps =  findViewById(R.id.rl_maps);
        mAuth = FirebaseAuth.getInstance();

        findDefaultAccount(LoginManager.getInstance().getUser().getUserID(), "CHECKING");

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.nav_notifications) {
                startActivity(new Intent(LoggedInActivity.this, NotificationsManagementActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            else if (id == R.id.nav_edit){
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
            else if (id == R.id.nav_discover) {
                Intent intent = new Intent(LoggedInActivity.this, DiscoverProductsActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            else if (id == R.id.nav_setting) {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
            else if (id == R.id.nav_all_account) {
                Intent intent = new Intent(LoggedInActivity.this, SeeAllAccountActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            return false;
        });

        tvName.setText("Hello,\n" + LoginManager.getInstance().getUser().getFullName());

        btnPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, NotificationsManagementActivity.class);
                startActivity(intent);
            }
        });
        rl_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, GetBankBranchActivity.class);
                startActivity(intent);
            }
        });
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
                    startListeningToBalance(LoginManager.getInstance().getAccount().getAccountNumber());
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void findDefaultAccount(String userID, String checking) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Account>> call = apiService.getDefaultAccount(userID, checking);

        call.enqueue(new Callback<ApiResponse<Account>>() {
            @Override
            public void onResponse(Call<ApiResponse<Account>> call, Response<ApiResponse<Account>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Account> apiResponse = response.body();

                    if (apiResponse != null) {
                        if (apiResponse.getResult() != null) {
                            Account account = apiResponse.getResult();
                            LoginManager.getInstance().setAccount(account);
                        }
                    }
                    else {
                        Log.w(LOG_TAG, "API Success but body is null.");
                        Toast.makeText(LoggedInActivity.this, "Không tìm thấy tài khoản mặc định", Toast.LENGTH_SHORT).show();
                        changeScreen(LoadingPageActivity.class);
                        LoginManager.clearUser();
                    }
                }
                else {
                    Log.e(LOG_TAG, "API Error. Code: " + response.code() + ", Msg: " + response.message());
                    Toast.makeText(LoggedInActivity.this, "Máy chủ không phản hồi", Toast.LENGTH_SHORT).show();
                    changeScreen(LoadingPageActivity.class);
                    LoginManager.clearUser();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Account>> call, Throwable t) {
                Log.e(LOG_TAG, "Network failure: " + t.getMessage());
                Toast.makeText(LoggedInActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                changeScreen(LoadingPageActivity.class);
                LoginManager.clearUser();
            }
        });
    }

    private void startListeningToBalance(String accountNumber) {
        DocumentReference docRef = db.collection("accounts").document(accountNumber);

        balanceListener = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("HomeActivity", "Lỗi nghe số dư", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Long balance = snapshot.getLong("balance");

                    if (balance != null) {
                        String formattedMoney = formatMoney(balance);

                        tv_balance_masked.setText(formattedMoney);

                        Log.d("HomeActivity", "Số dư vừa thay đổi: " + formattedMoney);
                    }
                } else {
                    Log.d("HomeActivity", "Tài khoản không tồn tại");
                }
            }
        });
    }

    private String formatMoney(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Bắt buộc dùng dấu chấm
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount);
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

    private void showTransferOptionsBottomSheet() {
        TransferOptionsBottomSheet bottomSheet = new TransferOptionsBottomSheet();
        bottomSheet.show(getSupportFragmentManager(), "TransferOptionsTag");
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}