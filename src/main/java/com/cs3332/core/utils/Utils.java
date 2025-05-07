package com.cs3332.core.utils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public final class Utils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRDKey(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            otp.append(CHARACTERS.charAt(index));
        }

        return otp.toString();
    }

    public static String joinString(String... lines){
        StringBuilder stringBuilder = new StringBuilder();
        for(String s:lines) stringBuilder.append(s);
        return stringBuilder.toString();
    }

    public static String generateAccountID() {
        int min = 10000000;
        int max = 99999999;
        return String.valueOf(ThreadLocalRandom.current().nextInt(max - min + 1) + min);
    }

    public static String generateTransactionID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[+]?\\d{1,4}[-\\s]?\\(?\\d{1,3}\\)?[-\\s]?\\d{3}[-\\s]?\\d{4}$";

        Pattern pattern = Pattern.compile(regex);

        return phoneNumber != null && pattern.matcher(phoneNumber).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        return email.matches(emailRegex);
    }

    public static long getTime() {
        return new Date().getTime();
    }
}
