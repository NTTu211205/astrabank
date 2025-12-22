package com.example.astrabank.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.R;
import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.Account;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MortgageAdapter extends RecyclerView.Adapter<MortgageAdapter.MortgageViewHolder> {
    private final String LOG_TAG = "MortgageAdapter";
    List<Account> accounts;
    Context context;
    OnItemClick click;

    public interface OnItemClick{
        void onItemClick(Account account);
    }

    public MortgageAdapter(List<Account> accounts, Context context, OnItemClick click) {
        this.accounts = accounts;
        this.context = context;
        this.click = click;
    }

    @NonNull
    @Override
    public MortgageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_customer, parent, false);
        return new MortgageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MortgageViewHolder holder, int position) {
        Account account = accounts.get(position);

        holder.tvAccountNumber.setText(account.getAccountNumber());
        holder.tvUserId.setText(account.getUserId());
        holder.tvInterestRate.setText(account.getInterestRate() + "% ");
        if (account.getAccountStatus()) {
            holder.tvStatus.setText("Active");
        }
        else {
            holder.tvStatus.setText("Non-Active");
        }

        if (account.getAccountStatus()) {
            holder.btStatus.setText("Non-Active");
        }
        else {
            holder.btStatus.setText("Active");
        }

        holder.btEdit.setVisibility(View.GONE);

        holder.btStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.getAccountStatus()) {
                    Toast.makeText(context, account.getAccountNumber(), Toast.LENGTH_SHORT).show();

                    callApiUpdatedStatusAccount(account.getAccountNumber(), false, position);
                }
                else {
                    callApiUpdatedStatusAccount(account.getAccountNumber(), true, position);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null) {
                    click.onItemClick(account);
                }
            }
        });
    }

    private void callApiUpdatedStatusAccount(String accountNumber, Boolean status, int position) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Boolean>> call = apiService.updateAccountStatus(accountNumber, status);

        call.enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Boolean> apiResponse = response.body();

                    if (apiResponse != null && apiResponse.getResult() != null) {
                        accounts.get(position).setAccountStatus(apiResponse.getResult());
                        notifyDataSetChanged();
                    }
                    else {
                        Log.d(LOG_TAG, "Response is null");
                        Toast.makeText(context, "Updated Fail, try again, account can have incomplete loan", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(LOG_TAG, "Updated failed");
                    Toast.makeText(context, "Updated Fail, try again, account can have incomplete loan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(context, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public static class MortgageViewHolder extends RecyclerView.ViewHolder{
        TextView tvAccountNumber, tvUserId, tvStatus, tvInterestRate;
        Button btStatus, btEdit;
        public MortgageViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAccountNumber = itemView.findViewById(R.id.tv_cus_name);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvInterestRate = itemView.findViewById(R.id.tvNationalId);
            tvStatus = itemView.findViewById(R.id.tv_cus_status);
            btStatus = itemView.findViewById(R.id.btn_del);
            btEdit = itemView.findViewById(R.id.btn_edit);
        }
    }
}
