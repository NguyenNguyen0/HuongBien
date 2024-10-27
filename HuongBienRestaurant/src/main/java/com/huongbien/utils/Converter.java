package com.huongbien.utils;

import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.Files;

public class Converter {
    // nhận đối tượng file ảnh để convert thành mảng byte
    public static byte[] fileToBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    //  chuyển mảng byte thành InputStream để load ảnh lên giao diên javafx
    public static InputStream bytesToInputStream (byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    //  chuyển đối tượng file thành đối tượng ảnh
    public static Image fileToImage(File file) throws FileNotFoundException {
        return new Image(new FileInputStream(file));
    }

    //  chuyển mảng bytes thành đối tượng Image của javafx
    public static Image bytesToImage(byte[] bytes) {
        return new Image(bytesToInputStream(bytes));
    }
}
