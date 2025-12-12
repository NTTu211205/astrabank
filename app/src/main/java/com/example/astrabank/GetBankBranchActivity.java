package com.example.astrabank;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class GetBankBranchActivity extends AppCompatActivity {

    private RecyclerView rvBranchList;
    private BranchAdapter adapter;
    private EditText edtSearch;
    private List<Branch> originalBranchList;

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_ALLOW_MAPS_PROMPT = "AllowMapsPrompt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_bank_branch);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvBranchList = findViewById(R.id.rvBranchList);
        edtSearch = findViewById(R.id.edtSearch);

        createData();

        adapter = new BranchAdapter(originalBranchList, this::showBottomSheet);
        rvBranchList.setLayoutManager(new LinearLayoutManager(this));
        rvBranchList.setAdapter(adapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { filter(s.toString()); }
        });

        View imgClose = findViewById(R.id.imgClose);
        if (imgClose != null) {
            imgClose.setOnClickListener(v -> finish());
        }
    }

    private void createData() {
        originalBranchList = new ArrayList<>();
        originalBranchList.add(new Branch("Astrabank Cao Thang", "39 Cao Thang, District 3, HCMC", "028 3832 1234", 10.768100, 106.680000));
        originalBranchList.add(new Branch("Astrabank Da Nang", "21 Nguyen Van Linh, Da Nang", "0236 365 7890", 16.060100, 108.216000));
        originalBranchList.add(new Branch("Astrabank Ha Noi", "108 Tran Hung Dao, Hoan Kiem District", "024 3942 5678", 21.025200, 105.842000));
        originalBranchList.add(new Branch("Astrabank Can Tho", "12 Hoa Binh, Ninh Kieu District", "0292 381 2345", 10.033333, 105.783333));
    }

    private void filter(String text) {
        List<Branch> filteredList = new ArrayList<>();
        for (Branch item : originalBranchList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getAddress().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    private void showBottomSheet(Branch branch) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        View view = LayoutInflater.from(this).inflate(R.layout.bottom_map_layout, null);
        bottomSheetDialog.setContentView(view);

        TextView tvTitle = view.findViewById(R.id.tvSheetTitle);
        TextView tvAddress = view.findViewById(R.id.tvSheetAddress);
        TextView tvPhone = view.findViewById(R.id.tv_phone_number);
        Button btnDirections = view.findViewById(R.id.btnDirections);

        tvTitle.setText(branch.getName());
        tvAddress.setText(branch.getAddress());
        if (tvPhone != null) tvPhone.setText(branch.getPhoneNumber());

        btnDirections.setOnClickListener(v -> {
            if (settings.getBoolean(KEY_ALLOW_MAPS_PROMPT, false)) {
                openGoogleMapsNavigation(branch.getLat(), branch.getLng());
                bottomSheetDialog.dismiss();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Open Google Maps")
                    .setMessage("Do you want to get walking directions to this branch?")
                    .setPositiveButton("Allow", (dialog, which) -> {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean(KEY_ALLOW_MAPS_PROMPT, true);
                        editor.apply();

                        openGoogleMapsNavigation(branch.getLat(), branch.getLng());
                        bottomSheetDialog.dismiss();
                    })
                    .setNegativeButton("Deny", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        bottomSheetDialog.show();
    }

    private void openGoogleMapsNavigation(double lat, double lng) {
        try {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng + "&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Google Maps is not installed on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    public static class Branch {
        private String name;
        private String address;
        private String phoneNumber;
        private double lat;
        private double lng;

        public Branch(String name, String address, String phoneNumber, double lat, double lng) {
            this.name = name;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.lat = lat;
            this.lng = lng;
        }

        public String getName() { return name; }
        public String getAddress() { return address; }
        public String getPhoneNumber() { return phoneNumber; }
        public double getLat() { return lat; }
        public double getLng() { return lng; }
    }

    public interface OnBranchClickListener {
        void onBranchClick(Branch branch);
    }

    public static class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {
        private List<Branch> branches;
        private OnBranchClickListener listener;

        public BranchAdapter(List<Branch> branches, OnBranchClickListener listener) {
            this.branches = branches;
            this.listener = listener;
        }

        public void filterList(List<Branch> filteredList) {
            this.branches = filteredList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_branch, parent, false);
            return new BranchViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
            Branch branch = branches.get(position);
            holder.tvName.setText(branch.getName());
            holder.tvAddress.setText(branch.getAddress());

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBranchClick(branch);
                }
            });
        }

        @Override
        public int getItemCount() {
            return branches.size();
        }

        public static class BranchViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvAddress;
            public BranchViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvBranchName);
                tvAddress = itemView.findViewById(R.id.tvBranchAddress);
            }
        }
    }
}
