package com.example.astrabank;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Notification;
import com.example.astrabank.models.Transaction;
import com.example.astrabank.utils.LoginManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsManagementActivity extends AppCompatActivity {
    private final String LOG_TAG = "NotificationsManagementActivity";

    // 1. Khai báo các View và Data
    private ImageView btnBack;
    private TextView tvTabInbox, tvTabRead;
    private RecyclerView recyclerView;

    private NotificationAdapter adapter;
    private List<Notification> fullList; // Danh sách gốc
    private List<Notification> displayList; // Danh sách hiển thị sau khi lọc
    private boolean isInboxSelected = true;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications_management);

        // Thiết lập Padding hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Ánh xạ View
        btnBack = findViewById(R.id.btn_back);
        tvTabInbox = findViewById(R.id.tv_tab_inbox);
        recyclerView = findViewById(R.id.rcv_details_noti);
        ivBack = findViewById(R.id.btn_back);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initDummyData();
        btnBack.setOnClickListener(v -> finish());
    }

    private void initDummyData() {
        fullList = new ArrayList<>();

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
                            fullList = notifications;

                            adapter = new NotificationAdapter(fullList);
//
                            recyclerView.setLayoutManager(new LinearLayoutManager(NotificationsManagementActivity.this));
                            recyclerView.setAdapter(adapter);
                        }
                        else {
                            Log.d(LOG_TAG, "No Notification");
                            Toast.makeText(NotificationsManagementActivity.this, "No Notification", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Log.d(LOG_TAG, "Error from server");
                        Toast.makeText(NotificationsManagementActivity.this, "Error from server", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(LOG_TAG, "Connect to server error");
                    Toast.makeText(NotificationsManagementActivity.this, "Connect to server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Notification>>> call, Throwable t) {
                Log.d(LOG_TAG, "CHECKING ACCOUNT EXIST:Internet disconnect");
                Toast.makeText(NotificationsManagementActivity.this, "Internet disconnect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTabsUI() {
//        if (isInboxSelected) {
//            // Tab Hộp thư được chọn
//            tvTabInbox.setBackgroundResource(R.drawable.tab_selected_left);
//            tvTabInbox.setTextColor(getResources().getColor(android.R.color.black));
//
//            tvTabRead.setBackgroundResource(R.drawable.tab_unselected_right);
//            tvTabRead.setTextColor(getResources().getColor(android.R.color.black));
//        } else {
//            // Tab Đã đọc được chọn
//            tvTabInbox.setBackgroundResource(R.drawable.tab_unselected_right);
//            tvTabInbox.setTextColor(getResources().getColor(android.R.color.black));
//
//            tvTabRead.setBackgroundResource(R.drawable.tab_selected_right);
//            tvTabRead.setTextColor(getResources().getColor(android.R.color.black));
//        }
        filterData();
    }

    private void filterData() {
        displayList.clear();
        for (Notification n : fullList) {
            if (isInboxSelected) {
                // Hộp thư: Hiện tất cả
                displayList.add(n);
            } else {
//                // Đã đọc: Chỉ hiện tin có isRead = true
//                if (n.isRead()) displayList.add(n);
            }
        }
        adapter.notifyDataSetChanged();
    }

    // --- INNER CLASSES (Gộp Model và Adapter vào đây) ---

    // 1. Model Class
//    public static class Notification {
//        private String title;
//        private String content;
//        private boolean isRead;
//
//        public Notification(String title, String content, boolean isRead) {
//            this.title = title;
//            this.content = content;
//            this.isRead = isRead;
//        }
//
//        public String getTitle() { return title; }
//        public String getContent() { return content; }
//        public boolean isRead() { return isRead; }
//    }

    // 2. Adapter Class
    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotiViewHolder> {
        private List<Notification> mList;

        public NotificationAdapter(List<Notification> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Sử dụng file item_notification.xml bạn đã có
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_card, parent, false);
            return new NotiViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NotiViewHolder holder, int position) {
            Notification item = mList.get(position);
            holder.tvTitle.setText(item.getTitle());
            holder.tvContent.setText(item.getContent());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class NotiViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvContent;
            public NotiViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_notification_title);
                tvContent = itemView.findViewById(R.id.tv_notification_body);
            }
        }
    }
}