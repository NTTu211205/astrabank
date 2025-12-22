package com.example.astrabank.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astrabank.AdminAddCustomerActivity;
import com.example.astrabank.AdminEditCustomerActivity;
import com.example.astrabank.R;
import com.example.astrabank.api.ApiClient;
import com.example.astrabank.api.ApiService;
import com.example.astrabank.api.response.ApiResponse;
import com.example.astrabank.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCustomerAdapter extends  RecyclerView.Adapter<AdminCustomerAdapter.CustomerViewHolder> {
    private final String LOG_TAG = "AdminCustomerActivity";
    List<User> users;

    public AdminCustomerAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        User user = users.get(position);

        holder.tvCustomerName.setText(user.getFullName());
        holder.tvCustomerId.setText(user.getUserID());
        if (user.getStatus()) {
            holder.tvCustomerStatus.setText("Active");
        }
        else {
            holder.tvCustomerStatus.setText("Non-Active");
        }
        holder.tvNationalId.setText(user.getNationalID());

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getStatus()) {
                    holder.btDelete.setText("Non-Active");
                    callApiUpdateUser(user.getUserID(), false , v.getContext(), position);

                }
                else {
                    holder.btDelete.setText("Active");
                    callApiUpdateUser(user.getUserID(), true , v.getContext(), position);
                }
            }
        });

        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AdminEditCustomerActivity.class);
                intent.putExtra("userId", user.getUserID());
                v.getContext().startActivity(intent);
                if (v.getContext() instanceof Activity) {
                    ((Activity) v.getContext()).finish();
                }            }
        });
    }

    private void callApiUpdateUser(String userId, Boolean status, Context c, int position) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Boolean>> call = apiService.updateStatus(userId, status);

        call.enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if(response.isSuccessful()) {
                    ApiResponse<Boolean> apiResponse = response.body();

                    if (apiResponse != null) {
                        Boolean isSuccess = apiResponse.getResult();

                        if (isSuccess) {
                            users.get(position).setStatus(status);
                            notifyDataSetChanged();
                        }
                        else {
                            Log.d(LOG_TAG, "result false or null");
                            Toast.makeText(c, "update k thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Log.d(LOG_TAG, "apiResponse is null");
                        Toast.makeText(c, "Error from server, try again", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(LOG_TAG, "Error from server");
                    Toast.makeText(c, "Error from server, try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Log.d(LOG_TAG, "Internet disconnected");
                Toast.makeText(c, "Internet disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder{
        TextView tvCustomerName, tvCustomerId, tvNationalId, tvCustomerStatus;
        Button btEdit, btDelete;
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tv_cus_name);
            tvCustomerStatus = itemView.findViewById(R.id.tv_cus_status);
            tvCustomerId = itemView.findViewById(R.id.tvUserId);
            tvNationalId = itemView.findViewById(R.id.tvNationalId);
            btEdit = itemView.findViewById(R.id.btn_edit);
            btDelete = itemView.findViewById(R.id.btn_del);
        }
    }
}
