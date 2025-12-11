package com.example.astrabank;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationsManagementActivity extends AppCompatActivity {

    // Khai báo các View chính
    private ImageView btnBack;
    private TextView tvTabInbox;
    private TextView tvTabRead;
    private RecyclerView recyclerView;

    private boolean isInboxSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_management);

        // 1. Tìm kiếm View theo ID
        btnBack = findViewById(R.id.btn_back);
        tvTabInbox = findViewById(R.id.tv_tab_inbox);
        tvTabRead = findViewById(R.id.tv_tab_read);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Đóng Activity và quay lại màn hình trước đó
                }
            });
        }
        if (tvTabInbox != null) {
            tvTabInbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectInboxTab();
                }
            });
        }

        if (tvTabRead != null) {
            tvTabRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectReadTab();
                }
            });
        }

        // Thiết lập trạng thái ban đầu khi Activity được tạo
        updateTabsUI();
    }

    private void selectInboxTab() {
        if (!isInboxSelected) {
            isInboxSelected = true;
            updateTabsUI();
        }
    }

    private void selectReadTab() {
        if (isInboxSelected) {
            isInboxSelected = false;
            updateTabsUI();
        }
    }

    private void updateTabsUI() {
        if (isInboxSelected) {
            // Tab Hộp thư (Đã chọn)
            tvTabInbox.setBackgroundResource(R.drawable.tab_selected_left);
            tvTabInbox.setTextColor(getResources().getColor(android.R.color.black));
            tvTabInbox.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Title); // Đặt đậm

            // Tab Đã đọc (Chưa chọn)
            tvTabRead.setBackgroundResource(R.drawable.tab_unselected_right);
            tvTabRead.setTextColor(getResources().getColor(R.color.white)); // Màu xám nhạt
            tvTabRead.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Subtitle); // Đặt nhạt hơn

        } else {
            // Tab Hộp thư (Chưa chọn)
            tvTabInbox.setBackgroundResource(R.drawable.tab_unselected_right); // Thường là transparent hoặc drawable bên phải
            tvTabInbox.setTextColor(getResources().getColor(R.color.white));
            tvTabInbox.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Subtitle);

            // Tab Đã đọc (Đã chọn)
            tvTabRead.setBackgroundResource(R.drawable.tab_selected_right);
            tvTabRead.setTextColor(getResources().getColor(android.R.color.black));
            tvTabRead.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);
        }
    }
}