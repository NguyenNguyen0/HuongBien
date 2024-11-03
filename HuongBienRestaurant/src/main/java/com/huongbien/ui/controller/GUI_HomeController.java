package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Statistics;
import com.huongbien.utils.Utils;
import javafx.application.Platform; // Make sure to import this
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
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GUI_HomeController implements Initializable {

    @FXML
    private MediaView mediaView;
    @FXML
    private Label txt_StartTime;
    @FXML
    private Label txt_TimeWorks;
    @FXML
    private Label txt_TotalCustomers;
    @FXML
    private Label txt_Reservations;
    @FXML
    private Label txt_TotalOfInvoice;
    @FXML
    private Label txt_TotalOfRevenues;


    private LocalDateTime startTime;
    private ScheduledExecutorService scheduler;

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
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        mediaView.setFitWidth(1680.0);
        mediaView.setFitHeight(513.0);

        ((AnchorPane) mediaView.getParent()).setPadding(new Insets(0, 0, 0, 50));
        mediaView.setPreserveRatio(false);

        startTime = LocalDateTime.now();
        displayStartTime();
        updateTimeWorks();
        setStatistics();

        Platform.runLater(() -> {
            Stage stage = (Stage) mediaView.getScene().getWindow();
            stage.setOnCloseRequest(event -> stopScheduler());
        });
    }

    private void displayStartTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
        txt_StartTime.setText(startTime.format(formatter));
    }

    private void updateTimeWorks() {
        LocalDateTime now = LocalDateTime.now();
        long secondsWorked = java.time.Duration.between(startTime, now).getSeconds();
        long hours = secondsWorked / 3600;
        long minutes = (secondsWorked % 3600) / 60;
        long seconds = secondsWorked % 60;

        Platform.runLater(() -> {
            txt_TimeWorks.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        });

        if (scheduler == null) {
            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(this::updateTimeWorks, 0, 1, TimeUnit.SECONDS);
        }
    }

    private void setStatistics() {
        try {
            txt_TotalCustomers.setText(String.valueOf(DAO_Statistics.getTotalCustomers()));
            txt_TotalOfRevenues.setText(Utils.formatMoney(DAO_Statistics.getTotalRevenues()));
            txt_Reservations.setText(String.valueOf(DAO_Statistics.getTotalReservations()));
            txt_TotalOfInvoice.setText(String.valueOf(DAO_Statistics.getTotalInvoices()));
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
