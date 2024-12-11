package com.huongbien.ui.controller;

import com.huongbien.dao.StatisticsDAO;
import com.huongbien.utils.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledExecutorService;

public class RestaurantHomeController implements Initializable {
    @FXML private MediaView mediaView;
    @FXML private Label totalCustomersField;
    @FXML private Label totalReservationField;
    @FXML private Label totalOrderField;
    @FXML private Label totalRevenuesField;

    private LocalDateTime startTime;
    private ScheduledExecutorService scheduler;

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL resource = getClass().getClassLoader().getResource("com/huongbien/img/banner/banner.mp4");
        String mediaPath = null;
        Media media = null;
        if (resource != null) {
            mediaPath = resource.toString();
            media = new Media(mediaPath);
        } else {
            System.out.println("Tệp video không tìm thấy.");
        }
        assert media != null;
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        mediaView.setFitWidth(1680.0);
        mediaView.setFitHeight(513.0);

        ((AnchorPane) mediaView.getParent()).setPadding(new Insets(0, 0, 0, 50));
        mediaView.setPreserveRatio(false);

        startTime = LocalDateTime.now();
        setStatistics();

        Platform.runLater(() -> {
            Stage stage = (Stage) mediaView.getScene().getWindow();
            stage.setOnCloseRequest(event -> stopScheduler());
        });
    }

    private void setStatistics() {
        try {
            totalCustomersField.setText(String.valueOf(StatisticsDAO.getTotalCustomers()));
            totalRevenuesField.setText(Utils.formatMoney(StatisticsDAO.getTotalRevenue()));
            totalReservationField.setText(String.valueOf(StatisticsDAO.getTotalReservations()));
            totalOrderField.setText(String.valueOf(StatisticsDAO.getTotalOrders()));
        } catch (Exception e) {
            System.err.println("Error fetching statistics: " + e.getMessage());
        }
    }

    private void stopScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("Scheduler stopped.");
        }
    }
}
