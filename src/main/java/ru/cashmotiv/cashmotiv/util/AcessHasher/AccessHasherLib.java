package ru.cashmotiv.cashmotiv.util.AcessHasher;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class AccessHasherLib {
    public static String createByIdAndLoginAndCreationTime(Long id, String login, Date creationTime, String salt) {
        if (id == null || login == null || creationTime == null) {
            return null;
        }

        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        String str = id + salt + login + salt + creationTime;
        return new String(digest.digest(str.getBytes(StandardCharsets.UTF_8)));
    }
}
