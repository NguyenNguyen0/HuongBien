package com.huongbien.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// class Utils là để những hàm chức năng (hỗ trợ) sài chung cho nhiều nơi cho dự án
public class Utils {

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

//   Hàm random số
    public static int randomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

//    Hàm chuyển membershipLevel từ int sang String
    public static String toStringMembershipLevel (int membershipLevel) {
        return switch (membershipLevel) {
            case 0 -> "Đồng";
            case 1 -> "Bạc";
            case 2 -> "Vàng";
            case 3 -> "Kim cương";
            default -> throw new IllegalArgumentException("Membership must in [0, 1, 2, 3]");
        };
    }

    //    Hàm chuyển membershipLevel từ String sang int
    public static int toIntMembershipLevel (String membershipLevel) {
        return switch (membershipLevel) {
            case "Đồng" -> 0;
            case "Bạc" -> 1;
            case "Vàng" -> 2;
            case "Kim cương" -> 3;
            default -> throw new IllegalArgumentException("Membership must in [0, 1, 2, 3]");
        };
    }
    //  Hàm ghi dữ liệu Binary xuống file text
    public boolean WriteToFile(Object obj, String filePath) throws Exception {
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(new FileOutputStream(filePath));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        return true;
    }

    // Hàm đọc dữ liệu từ file lên
    public Object ReadFromFile(String filePath) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    //Write JSON
    public static void writeJsonToFile(JsonArray jsonArray, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath, false)) {
            fileWriter.write(jsonArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Read JSON
    public static JsonArray readJsonFromFile(String filePath) throws FileNotFoundException {
        return JsonParser.parseReader(new FileReader(filePath)).getAsJsonArray();
    }
}

