package com.example.astrabank.utils;

import java.security.SecureRandom;

public class AccountNumberGenerator {
    private static final int ACCOUNT_NUMBER_LENGTH = 12;

    public String generateRandomAccountNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(ACCOUNT_NUMBER_LENGTH);

        sb.append(random.nextInt(9) + 1); // [1-9]
        for (int i = 1; i < ACCOUNT_NUMBER_LENGTH; i++) {
            sb.append(random.nextInt(10)); // [0-9]
        }

        return sb.toString();
    }
}
