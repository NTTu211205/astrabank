package com.example.astrabank;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

public class TransferOptionsBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_transaction_options, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.ll_to_other_person).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SelectRecipientActivity.class));
        });
        view.findViewById(R.id.ll_bill_payment).setOnClickListener(v ->{
            startActivity(new Intent(getActivity(), SelectBillPaymentActivity.class));
        });
    }
}