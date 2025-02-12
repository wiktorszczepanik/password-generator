package PasswordGenerator;

import java.security.SecureRandom;

public class SecureDouble {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static double random() {
        return secureRandom.nextDouble();
    }
}
