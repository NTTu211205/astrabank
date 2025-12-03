package com.example.astrabank.controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.astrabank.models.User;
import com.example.astrabank.utils.BCryptService;
import com.example.astrabank.utils.CallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserController {
    private final String LOG_TAG = "UserController";
    private final String COLLECTION = "users";
    private final CollectionReference db = FirebaseFirestore.getInstance().collection(COLLECTION);
    Context context;

    public UserController(Context context) {
        this.context = context;
    }

    // Kiểm tra số điện thoại có tồn tại trong db không
    // nếu có tồn tại trả về true
    // nếu không tồn tại trả về false
    public void checkPhoneExist(String phone, CallBack<Boolean> callBack) {
        db.whereEqualTo("phone", phone)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();

                            if (querySnapshot != null) {
                                if (querySnapshot.isEmpty()) {
                                    callBack.onResult(false);
                                }
                                else {
                                    callBack.onResult(true);
                                }
                            }
                            else {
                                callBack.onResult(false);
                            }
                        }
                        else {
                            callBack.onFailure(new Exception("Lỗi truy cập đến server"));
                        }
                    }
                });
    }

    // thêm một người dùng mới
    // thêm thành công trả về true
    // thêm thất bạit rả về false
    public void createUser(String userID, String fullName, String dateOfBirth,
                           String nationalID, String email, String phone, String address,
                           String occupation, String companyName, Double averageSalary,
                           Boolean status, String transactionPIN, CallBack<Boolean> callBack) {

        User user = new User(userID, fullName.toUpperCase(), dateOfBirth,
                 nationalID, email, phone, address,
                 occupation, companyName, averageSalary,
                 status, transactionPIN);

        db.document(userID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callBack.onResult(true);
                        Log.d(LOG_TAG, "create user success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.onFailure(new Exception("Lỗi trong quá trình thêm dữ liệu"));
                        Log.d(LOG_TAG, "create user error");
                    }
                });
    }

    // kiểm tra mã giao dịch đúng không trước khi thực hiện giao dịch chuyển tiền
    // đúng -> true
    // sai -> false
    // các trường hợp còn lại exception
    public void checkTransactionPIN(String userId, String transactionPIN, CallBack<Boolean> callBack) {
        db.whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();

                            if (querySnapshot == null) {
                                Log.d(LOG_TAG, "User id not found");
                                callBack.onResult(false);
                            }
                            else {
                                List<DocumentSnapshot> documentSnapshot = querySnapshot.getDocuments();
                                User user = documentSnapshot.get(0).toObject(User.class);

                                boolean checkTransactionPIN = BCryptService.checkPassword(transactionPIN, user.getTransactionPIN());
                                callBack.onResult(checkTransactionPIN);
                            }
                        }
                        else {
                            Log.d(LOG_TAG, "Find user error");
                            callBack.onFailure(new Exception("Can not find user account"));
                        }
                    }
                });
    }
}