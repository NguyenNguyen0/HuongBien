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
        //TODO: Còn nhiều option khác có thể thêm vào
    }

    //TODO: Có thể thêm nhiều hàm khác để tùy chỉnh thông báo theo chủ đề cụ thể gọi để dùng
}
