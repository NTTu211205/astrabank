package com.example.astrabank.utils;

import android.util.Log;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptService {
    private static final String TAG = "MindrotBcrypt";

    private static final int COST_FACTOR = 12;

    public static String hashPassword(String plainText) {
        if (plainText == null) return null;
        try {
            String salt = BCrypt.gensalt(COST_FACTOR);

            String hashed = BCrypt.hashpw(plainText, salt);
            return hashed;
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi băm mật khẩu");
            return null;
        }
    }

    public static boolean checkPassword(String plainText, String hashedText) {
        if (plainText == null || hashedText == null) return false;
        try {
            boolean matches = BCrypt.checkpw(plainText, hashedText);
            Log.d(TAG, "Xác minh mật khẩu: " + matches);
            return matches;
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi xác minh mật khẩu");
            return false;
        }
    }
}
