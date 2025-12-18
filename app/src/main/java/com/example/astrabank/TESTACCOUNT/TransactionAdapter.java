package com.example.astrabank.TESTACCOUNT;

import com.example.astrabank.models.Transaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deposit_history, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.tvTitle.setText(transaction.getDescription());
        String date = transaction.getCreatedAt();
        holder.tvDate.setText(date.substring(0, date.length()-8).replace("T", " "));
        holder.tvAmount.setText("VND " + formatMoney(transaction.getAmount()));
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
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvAmount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_transaction_title);
            tvDate = itemView.findViewById(R.id.tv_transaction_date);
            tvAmount = itemView.findViewById(R.id.tv_transaction_amount);
        }
    }
}