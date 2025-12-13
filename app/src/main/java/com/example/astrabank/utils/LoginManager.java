package com.example.astrabank.utils;

import com.example.astrabank.models.Account;
import com.example.astrabank.models.User;
import com.google.firebase.auth.FirebaseUser;

public class LoginManager {
    private static volatile LoginManager instance;
    private static User user;
    private static Account account;
    private static FirebaseUser firebaseUser;


    public static LoginManager getInstance() {
        if (instance == null) {
            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new LoginManager();
                }
            }
        }
        return instance;
    }

    public static void setInstance(LoginManager instance) {
        LoginManager.instance = instance;
    }

    public static FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public static void setFirebaseUser(FirebaseUser firebaseUser) {
        LoginManager.firebaseUser = firebaseUser;
    }

    public static Account getAccount() {
        return account;
    }

    public static void setAccount(Account account) {
        LoginManager.account = account;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        LoginManager.user = user;
    }

    public static void clearUser() {
        LoginManager.user = null;
        LoginManager.account = null;
        LoginManager.firebaseUser = null;
    }

}
