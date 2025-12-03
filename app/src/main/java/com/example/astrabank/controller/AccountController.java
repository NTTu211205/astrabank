package com.example.astrabank.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.astrabank.constant.AccountType;
import com.example.astrabank.models.Account;
import com.example.astrabank.utils.CallBack;
import com.example.astrabank.utils.RandomGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AccountController {
    private final String LOG_TAG = "AccountService";
    Context context;

    private final CollectionReference db = FirebaseFirestore.getInstance().collection("accounts");

    public AccountController(Context context) {
        this.context = context;
    }

    public void createAccount(String userId, AccountType accountType) {
        RandomGenerator.createAccountNumber(context, new CallBack<String>() {
            @Override
            public void onResult(String result) {
                String accountNumber = result;

                Account newAccount = new Account(userId, accountNumber, true, 0, AccountType.CHECKING);
                addAccountToDB(newAccount);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(LOG_TAG, "Create account number error");
                Toast.makeText(context, "Không thể tạo tài khoản \n " +
                        "Vui lòng thực hiện lại quy trình", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAccountToDB(Account account) {
        db.document(account.getAccountNumber())
                .set(account)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                            Log.d(LOG_TAG, "Adding account to database success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                            Log.d(LOG_TAG, "Adding account to database success");
                            Log.d(LOG_TAG, "Adding account to database have an error");
                            Toast.makeText(context, "Lỗi trong quá trình thêm dữ liệu\n" +
                                                    "Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Kiểm tra tài khoản có đủ tiển chuyển không
    // đủ -> true
    // không đủ -> false
    // các trường hợp khác throw exception
    private void checkBalance(String accountId, long amount, CallBack<Boolean> callBack) {
        db.whereEqualTo("accountNumber", accountId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();

                            if (querySnapshot != null) {
                                List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                                Account account = documents.get(0).toObject(Account.class);

                                if (account == null) {
                                    Log.d(LOG_TAG, "Account not found account");
                                    callBack.onFailure(new Exception("Account not found account"));
                                }
                                else {
                                    if (account.getBalance() > amount) {
                                        callBack.onResult(true);
                                        Log.d(LOG_TAG, "Allow to transfer");
                                    }
                                    else {
                                        callBack.onResult(false);
                                        Log.d(LOG_TAG, "Not allow to transfer");
                                    }
                                }
                            }
                            else {
                                Log.d(LOG_TAG, "Account not found");
                                callBack.onFailure(new Exception("Account not found"));
                            }
                        }
                        else {
                            Log.d(LOG_TAG, "Access account balance error");
                            callBack.onFailure(new Exception("Access account balance error"));
                        }
                    }
                });
    }

    // Kiểm tra số tài khoản đó có tồn tại không
    // có -> true
    // không -> false
    // các trượng hợp còn lại throw exception
    private void checkAccountNumber(String accountNumber, CallBack<Boolean> callBack) {
        db.whereEqualTo("accountNumber", accountNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();

                            if (querySnapshot != null) {
                                Log.d(LOG_TAG, "Account found, allow transfer");
                                callBack.onResult(true);
                            }
                            else {
                                Log.d(LOG_TAG, "Account not found, deny transfer");
                                callBack.onResult(false);
                            }
                        }
                        else {
                            Log.d(LOG_TAG, "Find account error");
                            callBack.onFailure(new Exception("Find account error"));
                        }
                    }
                });
    }

    // tru tien tai khoan hien tai
    // thanh cong -> true
    // khoong thanh cong -> false
    private void subtractBalance(String accountNumber, long amount, CallBack<Boolean> callBack) {
        db.document(accountNumber)
                .update("balance", FieldValue.increment(-amount))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(LOG_TAG, "updated subtract balance success");
                        callBack.onResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(LOG_TAG, "update subtract balance failure");
                        callBack.onFailure(new Exception("Update subtract balance error"));
                    }
                });
    }

    // cong tien vao tai khoan
    // thanh cong -> true
    // khong thanh cong  -> false
    private void addBalance(String accountNumber, long amount, CallBack<Boolean> callBack) {
        db.document(accountNumber)
                .update("balance", FieldValue.increment(amount))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(LOG_TAG, "updated add balance success");
                        callBack.onResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(LOG_TAG, "update add balance failure");
                        callBack.onFailure(new Exception("Update add balance error"));
                    }
                });
    }
}
