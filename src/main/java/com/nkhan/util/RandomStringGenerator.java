package com.nkhan.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomStringGenerator {

    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String randomString(int length) {
        final StringBuilder sb = new StringBuilder(length);
        final ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARSET.length());
            sb.append(CHARSET.charAt(index));
        }

        return sb.toString();
    }
}
