package com.example.astrabank.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.R;
import com.example.astrabank.models.Transaction;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class AdminTransactionAdapter extends RecyclerView.Adapter<AdminTransactionAdapter.TransactionViewHolder> {
    List<Transaction> transactions;

    public AdminTransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_full, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        String date = transaction.getCreatedAt();
        holder.tvTime.setText(date.substring(0, date.length()-8).replace("T", " "));
        holder.tvSourceAcc.setText(transaction.getSourceAcc());
        holder.tvDesAcc.setText(transaction.getDestinationAcc());
        holder.tvSourceName.setText(transaction.getSenderName());
        holder.tvDesName.setText(transaction.getReceiverName());
        holder.tvAmount.setText(formatMoney(transaction.getAmount()) + " VND");
    }

    private String formatMoney(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Bắt buộc dùng dấu chấm
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvAmount, tvSourceName, tvSourceAcc, tvDesName, tvDesAcc;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvAmount = itemView.findViewById(R.id.tvAmount);

            tvSourceName = itemView.findViewById(R.id.tvSourceName);
            tvSourceAcc = itemView.findViewById(R.id.tvSourceAcc);

            tvDesName = itemView.findViewById(R.id.tvDesName);
            tvDesAcc = itemView.findViewById(R.id.tvDesAcc);
        }
    }
}
