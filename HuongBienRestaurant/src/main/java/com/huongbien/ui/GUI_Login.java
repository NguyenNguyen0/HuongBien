package com.huongbien.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class GUI_Login extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/huongbien/fxml/GUI_Login.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.setTitle("Login - Huong Bien Restaurant");
        primaryStage.setMaximized(true);

        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Chặn thoát");
            alert.setHeaderText(null);
            alert.setContentText("Không thể thoát khỏi ứng dụng bằng phím tắt Alt + F4!");
            alert.showAndWait();
        });

        primaryStage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
