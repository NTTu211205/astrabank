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
import com.example.astrabank.models.Transaction;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.TransactionHistoryViewHolder> {
    List<Transaction> transactions;
    String checkingAccountNumber;
    Context context;

    public TransactionHistoryAdapter(List<Transaction> transactions, String checkingAccountNumber, Context context) {
        this.transactions = transactions;
        this.checkingAccountNumber = checkingAccountNumber;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_history, parent, false);
        return new TransactionHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        if (transaction.getSourceAcc().equals(checkingAccountNumber)) {
            holder.tvTitle.setText("Transfer money");
            holder.tvAmount.setText("- " + formatMoney(transaction.getAmount()) + "VND");
            holder.tvAmount.setTextColor(Color.parseColor("#FF0000"));
        }
        else {
            holder.tvTitle.setText("Receive money");
            holder.tvAmount.setText("+ " + formatMoney(transaction.getAmount()) + "VND");
            holder.tvAmount.setTextColor(Color.parseColor("#008000"));
        }

        String date = transaction.getCreatedAt();
        holder.tvDate.setText(date.substring(0, date.length() - 8).replace("T", " "));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    private String formatMoney(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount);
    }


    public static class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvAmount;
        public TransactionHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}
