package com.example.astrabank.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.R;
import com.example.astrabank.models.LoanReceipt;
import com.example.astrabank.models.PaymentRecord;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<LoanReceipt> paymentList;

    public PaymentAdapter(List<LoanReceipt> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_loan_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        LoanReceipt payment = paymentList.get(position);
        holder.tvTitle.setText("Monthly payment");
        String date = payment.getUpdatedAt().toString();
        holder.tvDate.setText(date.substring(0, date.length() - 15));
        holder.tvAmount.setText("VND " + formatMoney(payment.getAmount()));
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    private String formatMoney(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount);
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvAmount;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_payment_title);
            tvDate = itemView.findViewById(R.id.tv_payment_date);
            tvAmount = itemView.findViewById(R.id.tv_payment_amount);
        }
    }
}