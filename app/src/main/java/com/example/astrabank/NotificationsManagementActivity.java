package com.example.astrabank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationsManagementActivity extends AppCompatActivity {

    // 1. Khai báo các View và Data
    private ImageView btnBack;
    private TextView tvTabInbox, tvTabRead;
    private RecyclerView recyclerView;

    private NotificationAdapter adapter;
    private List<Notification> fullList; // Danh sách gốc
    private List<Notification> displayList; // Danh sách hiển thị sau khi lọc
    private boolean isInboxSelected = true;

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
        tvTabRead = findViewById(R.id.tv_tab_read);
        recyclerView = findViewById(R.id.rcv_details_noti);

        // 3. Khởi tạo dữ liệu và RecyclerView
        initDummyData();
        displayList = new ArrayList<>();
        adapter = new NotificationAdapter(displayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 4. Thiết lập sự kiện Click
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (tvTabInbox != null) {
            tvTabInbox.setOnClickListener(v -> {
                isInboxSelected = true;
                updateTabsUI();
            });
        }

        if (tvTabRead != null) {
            tvTabRead.setOnClickListener(v -> {
                isInboxSelected = false;
                updateTabsUI();
            });
        }

        // Cập nhật giao diện mặc định ban đầu
        updateTabsUI();
    }

    private void initDummyData() {
        fullList = new ArrayList<>();
        fullList.add(new Notification("Ưu đãi tháng 12", "Giảm 50k khi thanh toán QR qua AstraBank", false));
        fullList.add(new Notification("Biến động số dư", "Tài khoản của bạn vừa nhận +2,000,000 VNĐ", true));
        fullList.add(new Notification("Cập nhật hệ thống", "AstraBank sẽ bảo trì từ 0h - 2h sáng mai",true));
        fullList.add(new Notification("Quà tặng bạn mới", "Mở thẻ ngay nhận voucher 100k", false));
    }

    private void updateTabsUI() {
        if (isInboxSelected) {
            // Tab Hộp thư được chọn
            tvTabInbox.setBackgroundResource(R.drawable.tab_selected_left);
            tvTabInbox.setTextColor(getResources().getColor(android.R.color.black));

            tvTabRead.setBackgroundResource(R.drawable.tab_unselected_right);
            tvTabRead.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            // Tab Đã đọc được chọn
            tvTabInbox.setBackgroundResource(R.drawable.tab_unselected_right);
            tvTabInbox.setTextColor(getResources().getColor(android.R.color.black));

            tvTabRead.setBackgroundResource(R.drawable.tab_selected_right);
            tvTabRead.setTextColor(getResources().getColor(android.R.color.black));
        }
        filterData();
    }

    private void filterData() {
        displayList.clear();
        for (Notification n : fullList) {
            if (isInboxSelected) {
                // Hộp thư: Hiện tất cả
                displayList.add(n);
            } else {
                // Đã đọc: Chỉ hiện tin có isRead = true
                if (n.isRead()) displayList.add(n);
            }
        }
        adapter.notifyDataSetChanged();
    }

    // --- INNER CLASSES (Gộp Model và Adapter vào đây) ---

    // 1. Model Class
    public static class Notification {
        private String title, content;
        private boolean isRead;

        public Notification(String title, String content, boolean isRead) {
            this.title = title;
            this.content = content;
            this.isRead = isRead;
        }

        public String getTitle() { return title; }
        public String getContent() { return content; }
        public boolean isRead() { return isRead; }
    }

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