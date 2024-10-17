package com.huongbien.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


// class Utils là để những hàm chức năng (hỗ trợ) sài chung cho nhiều nơi cho dự án
public class Utils {

//  chuyển mảng byte thành InputStream để load ảnh lên giao diên javafx
    public static InputStream convertToInputStream (byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

//  hàm băm mật khẩu bằng thuật toán sha256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
