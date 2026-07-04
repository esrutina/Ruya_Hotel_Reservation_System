package com.ruyahotel.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Simple SHA-256 password hashing utility.
 * In production, use BCrypt (jBCrypt library) for better security.
 */
public class PasswordHasher {

    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    public static boolean verify(String password, String hash) {
        return hash(password).equals(hash);
    }
}
