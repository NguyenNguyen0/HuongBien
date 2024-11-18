package com.huongbien.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class OrderPaymentFinalGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/huongbien/fxml/OrderPaymentFinal.fxml"));
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.setTitle("Invoice Pay - Huong Bien Restaurant");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}