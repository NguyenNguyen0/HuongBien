package com.huongbien.service;

import com.huongbien.config.AppConfig;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.huongbien.entity.Customer;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class QRCodeHandler {
    private final String username;
    private final String appPassword;

    public QRCodeHandler() {
        this.username = AppConfig.getEmailUsername();
        this.appPassword = AppConfig.getEmailPassword();
    }

    public void createQRCode(Customer selectedCustomer, String qrContent) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix matrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 400, 400, hints);

            String customerId = selectedCustomer.getCustomerId();
            String outputFile = "src/main/resources/com/huongbien/qrCode/QrCode_Ma" + customerId + ".png";
            Path path = Paths.get(outputFile);

            Files.createDirectories(path.getParent());

            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "QR code được tạo thành công tại: " + outputFile);
            alert.showAndWait();
        } catch (WriterException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi khi viết mã QR: " + e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi khi ghi tệp: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi không xác định: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
