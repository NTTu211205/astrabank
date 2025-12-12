package com.example.astrabank.TESTACCOUNT;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<PaymentRecord> paymentList;

    public PaymentAdapter(List<PaymentRecord> paymentList) {
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
        PaymentRecord payment = paymentList.get(position);
        holder.tvTitle.setText(payment.getTitle());
        holder.tvDate.setText(payment.getDate());
        // Format amount: showing minus sign explicitly if desired
        holder.tvAmount.setText(String.format("- $ %,.2f", payment.getAmount()));
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
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