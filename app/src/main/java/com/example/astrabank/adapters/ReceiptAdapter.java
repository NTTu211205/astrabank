package com.example.astrabank.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.R;
import com.example.astrabank.models.LoanReceipt;

import java.text.DecimalFormat;
import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    private Context context;
    private List<LoanReceipt> mListReceipts;

    // Constructor
    public ReceiptAdapter(Context context, List<LoanReceipt> mListReceipts) {
        this.context = context;
        this.mListReceipts = mListReceipts;
    }

    // Hàm set dữ liệu mới (nếu cần dùng sau này để refresh list)
    public void setData(List<LoanReceipt> list) {
        this.mListReceipts = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receipt, parent, false);
        return new ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        LoanReceipt receipt = mListReceipts.get(position);
        if (receipt == null) {
            return;
        }

        holder.tvPeriod.setText("Period " + receipt.getPeriod());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String formattedAmount = decimalFormat.format(receipt.getAmount());
        holder.tvAmount.setText(formattedAmount + " VND");

        String rawDate = receipt.getFinalDate().toString();
        if (rawDate != null) {
            if (rawDate.contains("at")) {
                String shortDate = rawDate.split("at")[0].trim();
                holder.tvDate.setText("Due: " + shortDate);
            } else {
                holder.tvDate.setText("Due: " + rawDate);
            }
        }

        if (receipt.isPaid()) {
            holder.tvStatus.setText("PAID");
            holder.tvStatus.setTextColor(Color.parseColor("#388E3C")); // Xanh lá đậm
        } else {
            holder.tvStatus.setText("UNPAID");
            holder.tvStatus.setTextColor(Color.parseColor("#D32F2F")); // Đỏ đậm
        }
    }

    @Override
    public int getItemCount() {
        return mListReceipts.size();
    }

    public class ReceiptViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPeriod;
        private TextView tvStatus;
        private TextView tvAmount;
        private TextView tvDate;

        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPeriod = itemView.findViewById(R.id.tv_period);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
