package com.huongbien.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI_Login extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(com.huongbien.ui.GUI_Login.class.getResource("GUI_Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        stage.setTitle("Huong Bien Restaurant");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
