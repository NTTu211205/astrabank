package com.example.astrabank;

import static androidx.camera.core.CameraXThreads.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar; // <--- MỚI THÊM
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView; // <--- MỚI THÊM
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Account;
import com.example.astrabank.models.Notification;
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
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoggedInActivity extends AppCompatActivity {
    RecyclerView rvTransaction;
    private final String LOG_TAG = "LoggedInActivity";
    LinearLayout ll_middle, ll_products, ll_account_and_card;
    RelativeLayout rl_maps, rlNotifCount;
    private boolean visible = true;
    ImageView iv_move_money, img_toggle_visibility, ivHeadClose, btnPhoneCall, btnNotifications;
    LinearLayout ll_move_money, llScanQR;
    TextView tv_balance_masked, tvName, tvHoTro;
    NavigationView navigationView, navMenuInner;
    View navHeader, navFooter;
    AppCompatButton btSignOut, btSeeMore, btnRewards, btnAutoEarning;


    // --- Biến mới cho Menu (Side View) ---
    private DrawerLayout drawerLayout;
    private ImageButton ivMenuToggle;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration balanceListener;

    // --- [MỚI] Biến cho phần Insights (Chi tiêu & Số dư bình quân) ---
    private CardView cardSpendingInsight, cardAvgBalance;
    private TextView btnSeeSpending, btnSeeAvgBalance;
    private ProgressBar pbMonth10, pbMonth9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logged_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ánh xạ cũ (Giữ nguyên)
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
        rl_maps = findViewById(R.id.rl_maps);
        tvHoTro = findViewById(R.id.tv_ho_tro);
        rlNotifCount = findViewById(R.id.rl_notif_count);
        llScanQR = findViewById(R.id.ll_scanQR);
        rvTransaction = findViewById(R.id.rv_transactions);
        btSeeMore = findViewById(R.id.btn_see_more_transactions);
        btnRewards = findViewById(R.id.btn_bankRewards);
        btnAutoEarning = findViewById(R.id.btn_autoEarning);

        // --- [MỚI] Ánh xạ các view mới thêm vào ---
        cardSpendingInsight = findViewById(R.id.card_spending_insight);
        cardAvgBalance = findViewById(R.id.card_avg_balance);
        btnSeeSpending = findViewById(R.id.btn_see_spending_details);
        btnSeeAvgBalance = findViewById(R.id.btn_see_balance_details);
        pbMonth10 = findViewById(R.id.pb_month_10);
        pbMonth9 = findViewById(R.id.pb_month_9);

        SharedPreferences sharedPreferences = getSharedPreferences("AstraBankPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("EMAIL", LoginManager.getInstance().getUser().getEmail());
        editor.putString("PHONE", LoginManager.getInstance().getUser().getPhone());

        editor.apply();

        mAuth = FirebaseAuth.getInstance();

        findDefaultAccount(LoginManager.getInstance().getUser().getUserID(), "CHECKING");

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.nav_notifications) {
                startActivity(new Intent(LoggedInActivity.this, NotificationsManagementActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.nav_edit) {
                Intent intent = new Intent(LoggedInActivity.this, EditPersonalInformationActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.nav_discover) {
                Intent intent = new Intent(LoggedInActivity.this, DiscoverProductsActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.nav_setting) {
                Intent intent = new Intent(LoggedInActivity.this, SettingsActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.nav_all_account) {
                Intent intent = new Intent(LoggedInActivity.this, SeeAllAccountActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            return false;
        });

        tvName.setText("Hello,\n" + LoginManager.getInstance().getUser().getFullName());

        rl_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, GetBankBranchActivity.class);
                startActivity(intent);
            }
        });
        btnRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoggedInActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });
        btnAutoEarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, AutoEarningActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });
        rlNotifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, NotificationsManagementActivity.class);
                startActivity(intent);
            }
        });

        ivHeadClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        llScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, ScanQRActivity.class);
                startActivity(intent);
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
                    tv_balance_masked.setTextSize(14);
                    img_toggle_visibility.setImageResource(R.drawable.ic_show_eye);
                    visible = false;
                } else {
                    tv_balance_masked.setText("* * * * * *");
                    tv_balance_masked.setTextSize(14);
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

        drawerLayout = findViewById(R.id.drawer_layout);
        ivMenuToggle = findViewById(R.id.iv_menu_toggle);

        ivMenuToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        tvHoTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, SupportActivity.class);
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

        View.OnClickListener spendingListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, SpendingInsightActivity.class);
                startActivity(intent);
            }
        };
        // Gán sự kiện click cho cả Card và nút Text
        if (cardSpendingInsight != null) cardSpendingInsight.setOnClickListener(spendingListener);
        if (btnSeeSpending != null) btnSeeSpending.setOnClickListener(spendingListener);

        View.OnClickListener avgBalanceListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, AverageBalanceActivity.class);
                startActivity(intent);
            }
        };
        if (cardAvgBalance != null) cardAvgBalance.setOnClickListener(avgBalanceListener);
        if (btnSeeAvgBalance != null) btnSeeAvgBalance.setOnClickListener(avgBalanceListener);

        updateSpendingChartData();
        showTransactionHistory();

        btSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreenNotFinish(NotificationsManagementActivity.class);
            }
        });
    }

    private void showTransactionHistory() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<List<Notification>>> call = apiService.getUserHistories(LoginManager.getInstance().getUser().getUserID());

        call.enqueue(new Callback<ApiResponse<List<Notification>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Notification>>> call, Response<ApiResponse<List<Notification>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<Notification>> apiResponse = response.body();

                    if (apiResponse != null) {
                        List<Notification> notifications = apiResponse.getResult();
                        if (notifications != null) {
                            if (notifications.size() > 4) {
                                setUpRecyclerView(notifications.subList(0, 3));
                            }
                            else {
                                setUpRecyclerView(notifications);
                            }

                        }
                        else {
                            Log.d(LOG_TAG, "No Notification");
                            Toast.makeText(LoggedInActivity.this, "No Notification", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Log.d(LOG_TAG, "Error from server");
                        Toast.makeText(LoggedInActivity.this, "Error from server", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(LOG_TAG, "Connect to server error");
                    Toast.makeText(LoggedInActivity.this, "Connect to server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Notification>>> call, Throwable t) {
                Log.d(LOG_TAG, "CHECKING ACCOUNT EXIST:Internet disconnect");
                Toast.makeText(LoggedInActivity.this, "Internet disconnect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecyclerView(List<Notification> notifications) {
        NotificationAdapter adapter = new NotificationAdapter(notifications);
//
        rvTransaction.setLayoutManager(new LinearLayoutManager(LoggedInActivity.this));
        rvTransaction.setAdapter(adapter);
    }

    private void updateSpendingChartData() {
        if (pbMonth10 != null) pbMonth10.setProgress(80);
        if (pbMonth9 != null) pbMonth9.setProgress(60);
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
                    } else {
                        Log.w(LOG_TAG, "API Success but body is null.");
                        Toast.makeText(LoggedInActivity.this, "Không tìm thấy tài khoản mặc định", Toast.LENGTH_SHORT).show();
                        changeScreen(LoadingPageActivity.class);
                        LoginManager.clearUser();
                    }
                } else {
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
                        LoginManager.getInstance().getAccount().setBalance(balance);
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
        symbols.setGroupingSeparator('.');
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

    private void changeScreenNotFinish(Class<?> newScreen) {
        Intent intent = new Intent(this, newScreen);
        startActivity(intent);
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

    public static class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{
        private List<Notification> notifications;

        public NotificationAdapter(List<Notification> notifications) {
            this.notifications = notifications;
        }

        @NonNull
        @Override
        public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_list_transaction, parent, false);
            return new NotificationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
            Notification notification = notifications.get(position);
            holder.tvAmount.setText(notification.getAmount());
            holder.tvDescription.setText(notification.getContent());
            holder.tvTitle.setText(notification.getTitle());


            if (notification.getAmount().contains("-")) {
                holder.imageView.setImageResource(R.drawable.ic_transaction_out);
            } else {
                holder.imageView.setImageResource(R.drawable.ic_transaction_in);
            }

        }

        @Override
        public int getItemCount() {
            return notifications.size();
        }

        public class NotificationViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDescription, tvAmount;
            ImageView imageView; // Đã khai báo nhưng chưa dùng

            public NotificationViewHolder(@NonNull View itemView) {
                super(itemView);
                this.tvTitle = itemView.findViewById(R.id.tv_transaction_title);
                this.tvDescription = itemView.findViewById(R.id.tv_transaction_description);
                this.tvAmount = itemView.findViewById(R.id.tv_transaction_amount);
                this.imageView = itemView.findViewById(R.id.img_transaction_icon);
            }
        }
    }
}