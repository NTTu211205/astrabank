package com.example.astrabank.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.R;

import java.util.List;

public class BillHistoryAdapter extends RecyclerView.Adapter<BillHistoryAdapter.ViewHolder> {

    // Model đơn giản ngay trong Adapter cho tiện
    public static class BillHistoryItem {
        String title;
        String date;
        String amount;

        public BillHistoryItem(String title, String date, String amount) {
            this.title = title;
            this.date = date;
            this.amount = amount;
        }
    }

    private List<BillHistoryItem> list;

    public BillHistoryAdapter(List<BillHistoryItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BillHistoryItem item = list.get(position);
        holder.tvTitle.setText(item.title);
        holder.tvDate.setText(item.date);
        holder.tvAmount.setText(item.amount);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_history_title);
            tvDate = itemView.findViewById(R.id.tv_history_date);
            tvAmount = itemView.findViewById(R.id.tv_history_amount);
        }
    }
}