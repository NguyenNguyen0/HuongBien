package com.huongbien.utils;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class ToastsMessage {
    public static void showToastsMessage(String title, String message) {
        Notifications notifications = Notifications.create();
//        notifications.graphic(new ImageView(image)); //TODO: Có thể thêm biểu tượng ảnh vào thông báo
        notifications.title(title);
        notifications.text(message);
        notifications.hideAfter(Duration.seconds(2));
        notifications.position(Pos.TOP_RIGHT);
        notifications.show();
    }

    public static void showMessage(String message, String type) {
        Notifications notifications = Notifications.create()
                .title("Thông báo")
                .text(message)
                .hideAfter(Duration.seconds(3))
                .position(Pos.TOP_RIGHT);
        switch (type) {
            case "error" -> notifications.showError();
            case "warning" -> notifications.showWarning();
            case "success" -> notifications.showInformation();
            default -> notifications.show();
        }
    }
}
