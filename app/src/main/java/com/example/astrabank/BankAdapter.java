package com.example.astrabank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.BankViewHolder> {

    private List<BankModel> bankList;
    private OnBankClickListener listener; // Interface để xử lý click

    // Interface để giao tiếp với Activity
    public interface OnBankClickListener {
        void onBankClick(BankModel bank);
    }

    // Constructor nhận vào dữ liệu và listener
    public BankAdapter(List<BankModel> bankList, OnBankClickListener listener) {
        this.bankList = bankList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Gọi file layout item_bank.xml bạn đã tạo trước đó
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_bank_list, parent, false);
        return new BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankViewHolder holder, int position) {
        BankModel bank = bankList.get(position);

        // Gán dữ liệu lên View
        holder.tvShortName.setText(bank.getShortName());
        holder.tvFullName.setText(bank.getFullName());
        holder.ivLogo.setImageResource(bank.getLogoResId());

        // Bắt sự kiện Click vào item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBankClick(bank); // Truyền ngân hàng được chọn về Activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    // ViewHolder ánh xạ các View trong item_bank.xml
    public static class BankViewHolder extends RecyclerView.ViewHolder {
        TextView tvShortName, tvFullName;
        ImageView ivLogo;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            // Các ID này phải khớp với file item_bank.xml
            tvShortName = itemView.findViewById(R.id.tv_bank_short_name);
            tvFullName = itemView.findViewById(R.id.tv_bank_full_name);
            ivLogo = itemView.findViewById(R.id.iv_bank_logo);
        }
    }
}