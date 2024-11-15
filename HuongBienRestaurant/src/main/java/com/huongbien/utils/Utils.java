package com.huongbien.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.huongbien.entity.Table;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

// class Utils là để những hàm chức năng (hỗ trợ) sài chung cho nhiều nơi cho dự án
public class Utils {
    public final static String LOGINSESSION_PATH = "src/main/resources/com/huongbien/temp/loginSession.json";
    public final static String TEMPORARYCUISINE_PATH = "src/main/resources/com/huongbien/temp/temporaryCuisine.json";
    public final static String TEMPORARYTABLE_PATH = "src/main/resources/com/huongbien/temp/temporaryTable.json";
    public final static String TEMPORARYCUSTOMER_PATH = "src/main/resources/com/huongbien/temp/temporaryCustomer.json";
    public final static String PAYMENTQUEUE_PATH = "src/main/resources/com/huongbien/temp/paymentQueue.json";

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
    public static String toStringMembershipLevel(int membershipLevel) {
        return switch (membershipLevel) {
            case 0 -> "Đồng";
            case 1 -> "Bạc";
            case 2 -> "Vàng";
            case 3 -> "Kim cương";
            default -> throw new IllegalArgumentException("Membership must in [0, 1, 2, 3]");
        };
    }

    //    Hàm chuyển membershipLevel từ String sang int
    public static int toIntMembershipLevel(String membershipLevel) {
        return switch (membershipLevel) {
            case "Đồng" -> 0;
            case "Bạc" -> 1;
            case "Vàng" -> 2;
            case "Kim cương" -> 3;
            default -> throw new IllegalArgumentException("Membership must in [0, 1, 2, 3]");
        };
    }

    //  Hàm ghi dữ liệu Binary xuống file text - Không dùng nhưng vẫn để đó cho tao =))
    public boolean WriteToFile(Object obj, String filePath) throws Exception {
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(new FileOutputStream(filePath));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        return true;
    }

    // Hàm đọc dữ liệu từ file lên - Không dùng nhưng vẫn để đó cho tao =))
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

    // Chuyển danh sách Table về định dạng -> "Bàn 01, Bàn 02" Sài cho phần render
    // Dùng cái này để render mảng bảng trên giao diện
    public static String toStringTables(List<Table> tables) {
        if (tables == null) return "";
        return tables.stream()
                .map(Table::getName)
                .collect(Collectors.joining(", "));
    }

    // Format tiền cho thêm đơn vị dấu ',' và làm tròn lên triệu
    public static String formatMoney(double money) {
        DecimalFormat df = new DecimalFormat("#,###.##"); // Định dạng số, giữ tối đa hai chữ số thập phân

        if (money >= 1_000_000_000) {
            return df.format(money / 1_000_000_000) + " Tỷ VND";
        } else if (money >= 1_000_000) {
            return df.format(money / 1_000_000) + " Triệu VND";
        } else {
            return df.format(money) + " đ";
        }
    }

    //Alert
    public static void showAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

