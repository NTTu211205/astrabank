package com.example.astrabank.utils;

import android.content.Context;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

public class RandomGenerator{
    private static final String LOG_TAG = "RandomGenerator";
    private static final CollectionReference db = FirebaseFirestore.getInstance().collection("accounts");

    private static String generateRandom12DigitString() {
        Random random = new Random();

        StringBuilder sb = new StringBuilder(12);

        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    public static void createAccountNumber(Context context, CallBack<String> callBack) {
        Log.d(LOG_TAG, "processing create account number");
        String tempAccNumber = generateRandom12DigitString();

        db.whereEqualTo("accountNumber", tempAccNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot q = task.getResult();

                            if (q.isEmpty()) {
                                callBack.onResult(tempAccNumber);
                                Log.d(LOG_TAG, "Create account number success");
                            }
                            else {
                                createAccountNumber(context, callBack);
                                Log.d(LOG_TAG, "Create account number failure, create again");
                            }
                        }
                        else {
                            Log.d(LOG_TAG, "Create Account Number Error");
                            Toast.makeText(context, "Xảy ra lỗi trong quá trình tạo tài khoản \n " +
                                    "Vui lòng thực hiện lại quy trình", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
