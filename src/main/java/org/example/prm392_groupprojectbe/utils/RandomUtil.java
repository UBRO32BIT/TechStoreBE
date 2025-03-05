package org.example.prm392_groupprojectbe.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtil {
    private RandomUtil() {}
    private static final Random RANDOM = new SecureRandom();
    private static final String DIGITS = "0123456789";

    public static String generateOtp(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("OTP length must be greater than 0");
        }

        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            otp.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }
        return otp.toString();
    }
}
