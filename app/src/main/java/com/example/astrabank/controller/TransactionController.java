package com.example.astrabank.controller;

import android.content.Context;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TransactionController {
    private Context context;
    private final String LOG_TAG = "TransactionController";
    private final CollectionReference db = FirebaseFirestore.getInstance().collection("transactions");

    public TransactionController(Context context) {
        this.context = context;
    }

    // tao giao dich
    // khi tao giao dich thi status luon la PENDING
    public void createTransaction() {

    }

    // update trang thai giao dich
    public void updateTransactionStatus() {

    }


}
