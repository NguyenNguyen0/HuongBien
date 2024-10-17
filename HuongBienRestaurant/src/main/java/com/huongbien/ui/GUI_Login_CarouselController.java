package com.huongbien.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;

public class GUI_Login_CarouselController implements Initializable {
    @FXML
    private AnchorPane slide01;

    @FXML
    private AnchorPane slide02;

    @FXML
    private AnchorPane slide03;

    @FXML
    private Button next;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slide01.setTranslateY(0);
        slide02.setTranslateY(700);
        slide03.setTranslateY(1400);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3.5), event -> autoSlide())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private int currentSlide = 0;

    private void autoSlide() {
        if (currentSlide == 0) {
            translateAnimation(0.5, slide01, -700);
            translateAnimation(0.5, slide02, -700);
            translateAnimation(0.5, slide03, -700);
            currentSlide = 1;
        } else if (currentSlide == 1) {
            translateAnimation(0.5, slide01, -700);
            translateAnimation(0.5, slide02, -700);
            translateAnimation(0.5, slide03, -700);
            currentSlide = 2;
        } else if (currentSlide == 2) {
            translateAnimation(0.5, slide01, -700);
            translateAnimation(0.5, slide02, -700);
            translateAnimation(0.5, slide03, -700);
            currentSlide = 3;
        } else if(currentSlide == 3) {
            slide01.setTranslateY(0);
            slide02.setTranslateY(700);
            slide03.setTranslateY(1400);
            currentSlide = 0;
        }
    }

    public void translateAnimation(double duration, Node node, double height) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
        translateTransition.setByY(height);
        translateTransition.play();
    }
}