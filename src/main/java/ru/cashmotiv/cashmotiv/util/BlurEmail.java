package ru.cashmotiv.cashmotiv.util;

import java.util.Arrays;

public class BlurEmail {
    static public String blur(String email) {
        int at = email.indexOf("@");
        int coma = email.lastIndexOf(".");
        int prefixInd = Math.max(at - 5, 1);
        int suffixLen = coma - at - 1 - 3;
        int suffixInd = suffixLen > 0 ? suffixLen + at + 1 :  coma;

        return email.substring(0, prefixInd) +
                "*".repeat(suffixInd - prefixInd) +
                email.substring(suffixInd);
    }
}